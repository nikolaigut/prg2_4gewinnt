package opponent;

import gameView.ConnectFourGui;
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
    private final transient ConnectFourGui gameui;

    /**
     * Erstellt einen neuen Computergegner.
     *
     * @param id die ID des Computergegners
     * @param gameui Die Instanz des Spiels für den Spieler
     */
    public KIPlayer(final int id, final ConnectFourGui gameui) {
        super(id);
        this.gameui = gameui;
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
     * Wird ausgeführt wenn ein unglütiger Zug gemacht wurde.
     *
     */
    @Override
    public final void invalidMove() {
        this.gameui.invalidMove();
    }

    /**
     * Wird ausgeführt wenn der Computerspieler gewonnen hat.
     *
     */
    @Override
    public final void youWin() {
        this.gameui.disableColumnButtons();
        this.gameui.disableSaveButton();
        this.gameui.youWin();
    }

    /**
     * Wird ausgeführt wenn der Computerspieler verloren hat.
     *
     */
    @Override
    public final void youLose() {
        this.gameui.disableColumnButtons();
        this.gameui.disableSaveButton();
        this.gameui.youLose();
    }

    /**
     * Wird ausgeführt wenn ein Unentschieden erreicht wurde.
     *
     */
    @Override
    public final void draw() {
        this.gameui.disableColumnButtons();
        this.gameui.disableSaveButton();
        this.gameui.draw();
    }

}
