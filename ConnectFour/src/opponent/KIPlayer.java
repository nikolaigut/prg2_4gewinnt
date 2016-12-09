package opponent;

import gameModel.GameModel;
import java.util.Observable;
import java.util.Observer;

/**
 * ConnectFour KIPlayer representing a player controlled by the Computer.
 *
 * Repräsentiert einen Computerspieler (Intelligent)
 *
 * @author A. Morard
 */
public class KIPlayer extends Opponent implements Observer {

    //FIELDS--------------------------------------------------------------------
    private int[][] matrix;
    private GameTree gametree;

    //CONSTRUCTORS--------------------------------------------------------------
    /**
     * Erstellt einen neuen KI-Spieler
     *
     * @param id Spieler 1 oder 2
     */
    public KIPlayer(int id) {
        super(id);
    }

    //PUBLIC METHODS------------------------------------------------------------
    /**
     * Berechnet den nächsten Spielzug der KI
     *
     * @return nächster Spielzug
     */
    @Override
    public int getNextMove() {
        gametree = new GameTree(matrix, getId(), 5);
        return gametree.getNextmove();
    }

    /**
     * Zeichnet das Spielfeld anhand des aktuellen Models neu
     *
     * @param o Observer vom Typ GameModel
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        GameModel model = (GameModel) o;
        this.matrix = model.getGameMatrix();
    }

    @Override
    public void invalidMove() {

    }

    @Override
    public void youWin() {

    }

    @Override
    public void youLose() {

    }

    @Override
    public void draw() {

    }

}
