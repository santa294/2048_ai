package com.datumbox.opensource;

import com.datumbox.opensource.ai.AIsolver;
import com.datumbox.opensource.dataobjects.ActionStatus;
import com.datumbox.opensource.game.Board;
import com.datumbox.opensource.dataobjects.Direction;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;


public class ConsoleGame {


    public static void main(String[] args) throws CloneNotSupportedException {
        
        System.out.println("Игра 2048 на джава!");
        System.out.println("======================");
        System.out.println();
        while(true) {
            printMenu();
            int choice;
            try {
                Scanner sc = new Scanner (System.in);     
                choice = sc.nextInt();
                switch (choice) {
                    case 1:  playGame();
                             break;
                    case 2:  calculateAccuracy();
                             break;
                    case 3:  help();
                             break;
                    case 4:  return;
                    default: throw new Exception();
                }
            }
            catch(Exception e) {
                System.out.println("Wrong choice");
            }
        }
    }
    

    public static void help() {
        System.out.println("Рили?!?!?");
    }
    

    public static void printMenu() {
        System.out.println();
        System.out.println("Выбор:");
        System.out.println("1. Сыграйте в игру 2048");
        System.out.println("2. Оцените точность ИИ");
        System.out.println("3. Помощь");
        System.out.println("4. Выход");
        System.out.println();
        System.out.println("Выбери цифру 1-4:");
    }
    

    public static void calculateAccuracy() throws CloneNotSupportedException {
        int wins=0;
        int total=100;
        System.out.println("Начало "+total+" игры для оценки точности:");
        
        for(int i=0;i<total;++i) {
            int hintDepth = 7;
            Board theGame = new Board();
            Direction hint = AIsolver.findBestMove(theGame, hintDepth);
            ActionStatus result=ActionStatus.CONTINUE;
            while(result==ActionStatus.CONTINUE || result==ActionStatus.INVALID_MOVE) {
                result=theGame.action(hint);

                if(result==ActionStatus.CONTINUE || result==ActionStatus.INVALID_MOVE ) {
                    hint = AIsolver.findBestMove(theGame, hintDepth);
                }
            }

            if(result == ActionStatus.WIN) {
                ++wins;
                System.out.println("Игра "+(i+1)+" - Выиграна");
            }
            else {
                System.out.println("Игра "+(i+1)+" - Проиграна");
            }
        }
        
        System.out.println(wins+" выигрывает из "+total+" игра.");
    }
    

    public static void playGame() throws CloneNotSupportedException {
        System.out.println("Игра 2048!");
        System.out.println("Используйте 8 для перемещения ВВЕРХ, 6 для перемещения ВПРАВО, 2 для перемещения ВНИЗ и 4 для перемещения ВЛЕВО. Введите + для автоматического воспроизведения и q для выхода. Нажмите enter, чтобы подтвердить свой выбор.");
        
        int hintDepth = 7;
        Board theGame = new Board();
        Direction hint = AIsolver.findBestMove(theGame, hintDepth);
        printBoard(theGame.getBoardArray(), theGame.getScore(), hint);
        
        try {
            InputStreamReader unbuffered = new InputStreamReader(System.in, "UTF8");
            char inputChar;
            
            ActionStatus result=ActionStatus.CONTINUE;
            while(result==ActionStatus.CONTINUE || result==ActionStatus.INVALID_MOVE) {
                inputChar = (char)unbuffered.read();
                //inputChar = 'a';
                if(inputChar=='\n' || inputChar=='\r') {
                    continue;
                }
                else if(inputChar=='8') {
                    result=theGame.action(Direction.UP);
                }
                else if(inputChar=='6') {
                    result=theGame.action(Direction.RIGHT);
                }
                else if(inputChar=='2') {
                    result=theGame.action(Direction.DOWN);
                }
                else if(inputChar=='4') {
                    result=theGame.action(Direction.LEFT);
                }
                else if(inputChar=='+') {
                    result=theGame.action(hint);
                }
                else if(inputChar=='q') {
                    System.out.println("Игра закончилась, пользователь вышел.");
                    break;
                }
                else {
                    System.out.println("Неверная клавиша! Используйте 8 для перемещения ВВЕРХ, 6 для перемещения ВПРАВО, 2 для перемещения ВНИЗ и 4 для перемещения ВЛЕВО. Введите + для автоматического воспроизведения и q для выхода. Нажмите enter, чтобы подтвердить свой выбор.");
                    continue;
                }
                
                if(result==ActionStatus.CONTINUE || result==ActionStatus.INVALID_MOVE ) {
                    hint = AIsolver.findBestMove(theGame, hintDepth);
                }
                else {
                    hint = null;
                }
                printBoard(theGame.getBoardArray(), theGame.getScore(), hint);
                
                if(result!=ActionStatus.CONTINUE) {
                    System.out.println(result.getDescription());
                }
            }
        } 
        catch (IOException e) {
            System.err.println(e);
        }
    }
    

    public static void printBoard(int[][] boardArray, int score, Direction hint) {
        System.out.println("-------------------------");
        System.out.println("Score:\t" + String.valueOf(score));
        System.out.println();
        System.out.println("Hint:\t" + hint);
        System.out.println();
        
        for(int i=0;i<boardArray.length;++i) {
            for(int j=0;j<boardArray[i].length;++j) {
                System.out.print(boardArray[i][j] + "\t");
            }
            System.out.println();
        }
        System.out.println("-------------------------");
    }
}
