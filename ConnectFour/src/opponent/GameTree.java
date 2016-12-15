package opponent;

import java.io.Serializable;

/**
 * Erstellt einen Spielbaum anhand dessen der Computergegener seinen Spielzug
 * berechnet.
 *
 * @author A. Morard
 */
public class GameTree implements Serializable {
    private int owner;
    private int baumhoehe;
    private int nextmove = 3;
    private int[][] currentmatrix;
    private GameTreeNode root;

    /**
     * Erstellt einen Spielbaum mit angegebener Tiefe.
     *
     * @param currentmatrix die Matrix des zu berechnenden Baumes
     * @param owner der Besitzer des Baumes
     * @param tiefe Edie bene bis zu welcher der Baum berechnet werden soll
     */
    public GameTree(final int[][] currentmatrix, final int owner, final int tiefe) {
        this.currentmatrix = currentmatrix;
        this.owner = owner;
        this.baumhoehe = tiefe;
        root = new GameTreeNode(null, currentmatrix, baumhoehe, 0, owner);
    }

    /**
     * Erstellt einen Spielbaum mit Standardtiefe 5.
     *
     * @param currentmatrix die Matrix des zu berechnenden Baumes
     * @param owner der Besitzer des Baumes
     */
    public GameTree(final int[][] currentmatrix, final int owner) {
        this(currentmatrix, owner, 5);
    }

    /**
     * Berechnet den bestmöglichen Zug für den aktuellen Baum.
     *
     * @return die Spalte für den besten Zug
     */
    public final int getNextmove() {
        nextmove = root.getBestMove();
        return nextmove;
    }

}
