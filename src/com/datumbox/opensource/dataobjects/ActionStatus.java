package com.datumbox.opensource.dataobjects;

/**
 * Перечисление статуса действия.
 */
public enum ActionStatus {
    /**
     * Удачный ход, игра продолжается.
     */
    CONTINUE(0, "Successful move, the game continues."),
    
    /**
     * Игра успешно завершена.
     */
    WIN(1, "You won, the game ended!"),
    
    /**
     * Больше никаких ходов, игра окончена.
     */
    NO_MORE_MOVES(2,"No more moves, the game ended!"),
    
    /**
     * Недопустимый ход, ход не может быть выполнен.
     */
    INVALID_MOVE(3,"Invalid move!");
    

    private final int code;
    

    private final String description;
    

    private ActionStatus(final int code, final String description) {
        this.code = code;
        this.description = description;
    }
    

    public int getCode() {
        return code;
    }
 

    public String getDescription() {
        return description;
    }
}
