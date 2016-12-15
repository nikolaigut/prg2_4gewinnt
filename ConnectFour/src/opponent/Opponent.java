package opponent;

import java.io.Serializable;

/**
 * Repräsentiert einen Spieler.
 *
 * @author A. Morard
 */
public abstract class Opponent implements Serializable {
    private final int id;
    private String name;

    /**
     * Konstruktor für einen Spieler.
     *
     * @param id die ID des Spielers
     */
    public Opponent(final int id) {
        this.id = id;
    }

    /**
     * Wartet bis der Lokale Spieler einen Zug gemacht hat.
     *
     * @return Gibt den Spielzug zurück welcher der Spieler gemacht hat
     */
    public abstract int getNextMove();

    /**
     * Kontrolliert ob ein Zug gültig ist oder nicht.
     *
     */
    public abstract void invalidMove();

    /**
     * Kontroliert ob das Spiel gewonnen wurde.
     *
     */
    public abstract void youWin();

    /**
     * Kontrolliert ob das Spiel verloren wurde.
     *
     */
    public abstract void youLose();

    /**
     * Kontrolliert ob ein Unentschieden erreicht wurde.
     *
     */
    public abstract void draw();

    /**
     * Wartet bis der Lokale Spieler einen Zug gemacht hat.
     *
     * @param currentMatrix die Matrix des Spiels
     * @return Gibt den Spielzug zurück welcher der Spieler gemacht hat
     */
    public final int getNextMove(final int[][] currentMatrix) {
        return 0;
    }

    /**
     * Getter-Methode für die ID.
     *
     * @return die ID des Spielers
     */
    public final int getId() {
        return id;
    }
}
