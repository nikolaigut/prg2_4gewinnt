package opponent;

import java.io.Serializable;

/**
 * ConnectFour Opponent representing a player.
 *
 * Repräsentiert einen Spieler.
 *
 * @author A. Morard
 *
 */
public abstract class Opponent implements Serializable {

    //FIELDS--------------------------------------------------------------------
    private final int id;
    private String name;

    //CONSTRUCTORS--------------------------------------------------------------
    public Opponent(int id) {
        this.id = id;
    }

    //ABSTRACT METHODS----------------------------------------------------------
    public abstract int getNextMove();

    public abstract void invalidMove();

    public abstract void youWin();

    public abstract void youLose();

    public abstract void draw();

    //PUBLIC METHODS------------------------------------------------------------
    public int getNextMove(int[][] currentMatrix) {
        return 0;
    }

    //GETTER and SETTER---------------------------------------------------------
    public int getId() {
        return id;
    }
}
