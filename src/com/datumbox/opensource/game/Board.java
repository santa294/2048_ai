package com.datumbox.opensource.game;

import com.datumbox.opensource.dataobjects.ActionStatus;
import com.datumbox.opensource.dataobjects.Direction;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Board implements Cloneable {
    
    public static final int BOARD_SIZE = 4;
    
    
    public static final int TARGET_POINTS = 2048;
    
    
    public static final int MINIMUM_WIN_SCORE = 18432;
    
   
    private int score=0;
    
    
    private int[][] boardArray;
    
    
    private final Random randomGenerator;
    
   
    private Integer cache_emptyCells=null;
    
    
    public Board() {
        boardArray = new int[BOARD_SIZE][BOARD_SIZE];
        randomGenerator = new Random(System.currentTimeMillis());
        
        addRandomCell();
        addRandomCell();
        
    }
    
   
    @Override
    public Object clone() throws CloneNotSupportedException {
        Board copy = (Board)super.clone();
        copy.boardArray = clone2dArray(boardArray);
        return copy;
    }
    
   
    public int getScore() {
        return score;
    }
    
    
    public int[][] getBoardArray() {
        return clone2dArray(boardArray);
    }
    
   
    public Random getRandomGenerator() {
        return randomGenerator;
    }
    
    
    public int move(Direction direction) {    
        int points = 0;
        
        //rotate the board to make simplify the merging algorithm
        if(direction==Direction.UP) {
            rotateLeft();
        }
        else if(direction==Direction.RIGHT) {
            rotateLeft();
            rotateLeft();
        }
        else if(direction==Direction.DOWN) {
            rotateRight();
        }
        
        for(int i=0;i<BOARD_SIZE;++i) {
            int lastMergePosition=0;
            for(int j=1;j<BOARD_SIZE;++j) {
                if(boardArray[i][j]==0) {
                    continue; //skip moving zeros
                }
                
                int previousPosition = j-1;
                while(previousPosition>lastMergePosition && boardArray[i][previousPosition]==0) { //skip all the zeros
                    --previousPosition;
                }
                
                if(previousPosition==j) {
                    //we can't move this at all
                }
                else if(boardArray[i][previousPosition]==0) {
                    //move to empty value
                    boardArray[i][previousPosition]=boardArray[i][j];
                    boardArray[i][j]=0;
                }
                else if(boardArray[i][previousPosition]==boardArray[i][j]){
                    //merge with matching value
                    boardArray[i][previousPosition]*=2;
                    boardArray[i][j]=0;
                    points+=boardArray[i][previousPosition];
                    lastMergePosition=previousPosition+1;
                    
                }
                else if(boardArray[i][previousPosition]!=boardArray[i][j] && previousPosition+1!=j){
                    boardArray[i][previousPosition+1]=boardArray[i][j];
                    boardArray[i][j]=0;
                }
            }
        }
        
        
        score+=points;
        
        //reverse back the board to the original orientation
        if(direction==Direction.UP) {
            rotateRight();
        }
        else if(direction==Direction.RIGHT) {
            rotateRight();
            rotateRight();
        }
        else if(direction==Direction.DOWN) {
            rotateLeft();
        }
        
        return points;
    }
    
   
    public List<Integer> getEmptyCellIds() {
        List<Integer> cellList = new ArrayList<>();
        
        for(int i=0;i<BOARD_SIZE;++i) {
            for(int j=0;j<BOARD_SIZE;++j) {
                if(boardArray[i][j]==0) {
                    cellList.add(BOARD_SIZE*i+j);
                }
            }
        }
        
        return cellList;
    }
    
   
    public int getNumberOfEmptyCells() {
        if(cache_emptyCells==null) {
            cache_emptyCells = getEmptyCellIds().size();
        }
        return cache_emptyCells;
    }
    
    
    public boolean hasWon() {
        if(score<MINIMUM_WIN_SCORE) { //speed optimization
            return false;
        }
        for(int i=0;i<BOARD_SIZE;++i) {
            for(int j=0;j<BOARD_SIZE;++j) {
                if(boardArray[i][j]>=TARGET_POINTS) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
  
    public boolean isGameTerminated() throws CloneNotSupportedException {
        boolean terminated=false;
        
        if(hasWon()==true) {
            terminated=true;
        }
        else {
            if(getNumberOfEmptyCells()==0) { //if no more available cells
                Board copyBoard = (Board) this.clone();
                                
                if(copyBoard.move(Direction.UP)==0 
                   && copyBoard.move(Direction.RIGHT)==0 
                   && copyBoard.move(Direction.DOWN)==0 
                   && copyBoard.move(Direction.LEFT)==0) {
                    terminated=true;
                }
                
                //copyBoard=null;
            }
        }
        
        return terminated;
    }
    
   
    public ActionStatus action(Direction direction) throws CloneNotSupportedException {
        ActionStatus result = ActionStatus.CONTINUE;
        
        int[][] currBoardArray = getBoardArray();
        int newPoints = move(direction);
        int[][] newBoardArray = getBoardArray();
        
        //add random cell
        boolean newCellAdded = false;
        
        if(!isEqual(currBoardArray, newBoardArray)) {
            newCellAdded = addRandomCell();
        }
        
        if(newPoints==0 && newCellAdded==false) {
            if(isGameTerminated()) {
                result = ActionStatus.NO_MORE_MOVES;
            }
            else {
                result = ActionStatus.INVALID_MOVE;
            }
        }
        else {
            if(newPoints>=TARGET_POINTS) {
                result = ActionStatus.WIN;
            }
            else {
                if(isGameTerminated()) {
                    result = ActionStatus.NO_MORE_MOVES;
                }
            }
        }
        
        return result;
    }
    
   
    public void setEmptyCell(int i, int j, int value) {
        if(boardArray[i][j]==0) {
            boardArray[i][j]=value;
            cache_emptyCells=null;
        }
    }
    
    /**
     * Rotates the board on the left
     */
    private void rotateLeft() {
        int[][] rotatedBoard = new int[BOARD_SIZE][BOARD_SIZE];
        
        for(int i=0;i<BOARD_SIZE;++i) {
            for(int j=0;j<BOARD_SIZE;++j) {
                rotatedBoard[BOARD_SIZE-j-1][i] = boardArray[i][j];
            }
        }
        
        boardArray=rotatedBoard;
    }
   
    private void rotateRight() {
        int[][] rotatedBoard = new int[BOARD_SIZE][BOARD_SIZE];
        
        for(int i=0;i<BOARD_SIZE;++i) {
            for(int j=0;j<BOARD_SIZE;++j) {
                rotatedBoard[i][j]=boardArray[BOARD_SIZE-j-1][i];
            }
        }
        
        boardArray=rotatedBoard;
    }
    
   
    private boolean addRandomCell() {
        List<Integer> emptyCells = getEmptyCellIds();
        
        int listSize=emptyCells.size();
        
        if(listSize==0) {
            return false;
        }
        
        int randomCellId=emptyCells.get(randomGenerator.nextInt(listSize));
        int randomValue=(randomGenerator.nextDouble()< 0.9)?2:4;
        
        int i = randomCellId/BOARD_SIZE;
        int j = randomCellId%BOARD_SIZE;
        
        setEmptyCell(i, j, randomValue);
        
        return true;
    }
    
   
    private int[][] clone2dArray(int[][] original) { 
        int[][] copy = new int[original.length][];
        for(int i=0;i<original.length;++i) {
            copy[i] = original[i].clone();
        }
        return copy;
    }
    

    
  
    public boolean isEqual(int[][] currBoardArray, int[][] newBoardArray) {

    	boolean equal = true;
        
        for(int i=0;i<currBoardArray.length;i++) {
            for(int j=0;j<currBoardArray.length;j++) {
                if(currBoardArray[i][j]!= newBoardArray[i][j]) {
                    equal = false; //The two boards are not same.
                    return equal;
                }
            }
        }
        
        return equal;
    }
}
