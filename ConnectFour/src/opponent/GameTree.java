package opponent;

import java.io.Serializable;

/**
 * ConnectFour GameTree
 *
 * Erstellt einen Spielbaum anhand dessen der KI-Spieler seinen Spielzug
 * berechnet.
 *
 * @author A. Morard
 *
 * @version 1.0
 */
public class GameTree implements Serializable {

    //FIELDS--------------------------------------------------------------------
    private int owner;
    private int baumhoehe;
    private int nextmove = 3;
    private int[][] currentmatrix;
    GameTreeNode root;

    //CONSTRUCTORS--------------------------------------------------------------
    /**
     * Erstellt einen Spielbaum mit angegebenr tiefe
     *
     * @param currentmatrix matrix von welcher der Baum berechnet werden soll
     * @param owner besitzer des Baums
     * @param tiefe Ebene bis zu welcher der Baum berechnet werden soll
     */
    public GameTree(int[][] currentmatrix, int owner, int tiefe) {
        this.currentmatrix = currentmatrix;
        this.owner = owner;
        this.baumhoehe = tiefe;
        root = new GameTreeNode(null, currentmatrix, baumhoehe, 0, owner);
    }

    /**
     * Erstellt einen Spielbaum mit Standardtiefe 5
     *
     * @param currentmatrix matrix von welcher der Baum berechnet werden soll
     * @param owner besitzer des Baums
     */
    public GameTree(int[][] currentmatrix, int owner) {
        this(currentmatrix, owner, 5);
    }

    //PUBLIC METHODS------------------------------------------------------------
    /**
     * Berechnet den bestmöglichen Zug für den aktuellen Baum
     *
     * @return Spalte für den besten Zug
     */
    public int getNextmove() {
        nextmove = root.getBestMove();
        return nextmove;
    }

}
