package opponent;

import gameView.ConnectFourGui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Represäntiert einen Lokalen Spieler.
 *
 * @author A. Morard
 */
public class LocalPlayer extends Opponent implements ActionListener {
    private int col;
    private transient ConnectFourGui gameui;

    /**
     * Konstruktor für einen neuen Lokalen Spieler.
     *
     * @param id Die ID des Spielers
     * @param gameui Die Instanz des Spiels für den Spieler
     */
    public LocalPlayer(final int id, final ConnectFourGui gameui) {
        super(id);
        this.gameui = gameui;
    }

    /**
     * Wartet bis der Lokale Spieler einen Zug gemacht hat.
     *
     * @return Gibt den Spielzug zurück welcher der Spieler gemacht hat
     */
    @Override
    public final int getNextMove() {
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println(ex);
            }
        }
        return this.col;
    }

    /**
     * Listener für die Spalten Buttons des GUI's.
     *
     * @param e das Event des GUI's
     */
    @Override
    public final void actionPerformed(final ActionEvent e) {
        this.col = Integer.valueOf(e.getActionCommand());
        synchronized (this) {
            notifyAll();
        }
    }

    /**
     * Kontrolliert ob ein Zug gültig ist oder nicht.
     *
     */
    @Override
    public final void invalidMove() {
        this.gameui.invalidMove();
    }

    /**
     * Kontroliert ob das Spiel gewonnen wurde.
     *
     */
    @Override
    public final void youWin() {
        this.gameui.disableColumnButtons();
        this.gameui.disableSaveButton();
        this.gameui.youWin(this.getId());
    }

    /**
     * Kontrolliert ob das Spiel verloren wurde.
     *
     */
    @Override
    public final void youLose() {
        this.gameui.disableColumnButtons();
        this.gameui.disableSaveButton();
        this.gameui.youLose();
    }

    /**
     * Kontrolliert ob ein Unentschieden erreicht wurde.
     *
     */
    @Override
    public final void draw() {
        this.gameui.disableColumnButtons();
        this.gameui.disableSaveButton();
        this.gameui.draw();
    }

    /**
     * Kontrolliert ob ein Unentschieden erreicht wurde.
     *
     * @param gui die Instanz der Spielanzeige
     */
    public final void setGameui(final ConnectFourGui gui) {
        this.gameui = gui;
    }
}
