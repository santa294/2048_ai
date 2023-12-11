package com.datumbox.opensource.dataobjects;


public enum Direction {

    UP(0,"Вверх"),
    

    RIGHT(1,"Вправо"),
    

    DOWN(2,"Вниз"),
    

    LEFT(3,"Влево");
    
    

    private final int code;
    

    private final String description;
    

    private Direction(final int code, final String description) {
        this.code = code;
        this.description = description;
    }
    

    public int getCode() {
        return code;
    }
 

    public String getDescription() {
        return description;
    }
    

    @Override
    public String toString() { 
        return description;
    }
}
