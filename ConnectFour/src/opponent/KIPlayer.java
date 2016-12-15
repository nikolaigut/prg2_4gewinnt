package opponent;

import gameModel.GameModel;
import java.util.Observable;
import java.util.Observer;

/**
 * Repräsentiert einen Computergegner für den lokalen Spieler.
 *
 * @author A. Morard
 */
public class KIPlayer extends Opponent implements Observer {
    private int[][] matrix;
    private GameTree gametree;

    /**
     * Erstellt einen neuen Computergegner.
     *
     * @param id die ID des Computergegners
     */
    public KIPlayer(final int id) {
        super(id);
    }

    /**
     * Berechnet den nächsten Spielzug der KI.
     *
     * @return der nächste Spielzug
     */
    @Override
    public final int getNextMove() {
        gametree = new GameTree(matrix, getId(), 5);
        return gametree.getNextmove();
    }

    /**
     * Zeichnet das Spielfeld anhand des aktuellen Models neu.
     *
     * @param o Observer vom Typ GameModel
     * @param arg Argumentobjekt
     */
    @Override
    public final void update(final Observable o, final Object arg) {
        GameModel model = (GameModel) o;
        this.matrix = model.getGameMatrix();
    }

    /**
     * Kontrolliert ob ein Zug gültig ist oder nicht.
     *
     */
    @Override
    public void invalidMove() {
        //this.gameui.invalidMove();
    }

    /**
     * Kontroliert ob das Spiel gewonnen wurde.
     *
     */
    @Override
    public void youWin() {
        //this.gameui.disableColumnButtons();
        //this.gameui.disableSaveButton();
        //this.gameui.youWin();
    }

    /**
     * Kontrolliert ob das Spiel verloren wurde.
     *
     */
    @Override
    public void youLose() {
        //this.gameui.disableColumnButtons();
        //this.gameui.disableSaveButton();
        //this.gameui.youLose();
    }

    /**
     * Kontrolliert ob ein Unentschieden erreicht wurde.
     *
     */
    @Override
    public void draw() {
        //this.gameui.disableColumnButtons();
        //this.gameui.disableSaveButton();
        //this.gameui.draw();
    }

}
