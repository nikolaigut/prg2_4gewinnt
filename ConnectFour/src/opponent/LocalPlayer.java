package opponent;

import gameView.ConnectFourGui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ConnectFour LocalPlayer
 *
 * Represäntiert einen Lokalen Spieler
 *
 * @author A. Morard
 *
 */
public class LocalPlayer extends Opponent implements ActionListener {

    //FIELDS--------------------------------------------------------------------
    private int col;
    private transient ConnectFourGui gameui;

    //CONSTRUCTORS--------------------------------------------------------------
    /**
     * Erstellt einen neuen Lokalen Spieler
     *
     * @param id
     * @param gameui
     */
    public LocalPlayer(int id, ConnectFourGui gameui) {
        super(id);
        this.gameui = gameui;
    }

    //PUBLIC METHODS------------------------------------------------------------
    /**
     * Wartet bis der Lokale Spieler einen Zug gemacht hat
     *
     * @return Gibt den Spielzug zurück welcher der Spieler gemacht hat
     */
    @Override
    public int getNextMove() {
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        return this.col;
    }

    /**
     * Listener für die Spalten Buttons des GUI's
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // ActionCommand ist Spaltennummer
        this.col = Integer.valueOf(e.getActionCommand());

        synchronized (this) {
            notifyAll();
        }
    }

    /**
     * Meldet dass der Spielzug ungültig ist
     */
    @Override
    public void invalidMove() {
        //this.gameui.invalidMove();
    }

    /**
     * Meldet dass das Spiel gewonnen ist
     */
    @Override
    public void youWin() {
        //this.gameui.disableColumnButtons();
        //this.gameui.disableSaveButton();
        //this.gameui.youWin();
    }

    /**
     * Meldet dass das Spiel verloren ist
     */
    @Override
    public void youLose() {
        //this.gameui.disableColumnButtons();
        //this.gameui.disableSaveButton();
        //this.gameui.youLose();
    }

    /**
     * Meldet dass das Spiel unentschieden ist
     */
    @Override
    public void draw() {
        //this.gameui.disableColumnButtons();
        //this.gameui.disableSaveButton();
        //this.gameui.draw();
    }

    //GETTER and SETTER---------------------------------------------------------
    public void setGameui(ConnectFourGui gameui) {
        this.gameui = gameui;
    }
}
