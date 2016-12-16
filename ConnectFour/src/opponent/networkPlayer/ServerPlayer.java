package opponent.networkPlayer;

import gameView.ConnectFourGui;
import gameModel.GameModel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import opponent.Opponent;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Repräsentiert einen Spieler, der mit dem Client über das Netzwerk Spielt.
 *
 * @author A. Morard
 */
public class ServerPlayer extends Opponent implements Observer {
    private transient ObjectOutputStream out;
    private transient BufferedReader in;
    private transient ConnectFourGui gameui;

    /**
     * Erstellt einen neuen Server Spieler.
     *
     * @param id die ID des Server Spielers
     * @param clientSocket das Socket des Clients
     */
    public ServerPlayer(final int id, final Socket clientSocket) {
        super(id);
        try {
            this.out = new ObjectOutputStream(clientSocket.getOutputStream());
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("Partner: " + clientSocket.getInetAddress().getHostAddress());
        } catch (IOException ex) {
            Logger.getLogger(ServerPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Fragt beim Client nach dem nächsten Zug.
     *
     * @return Gibt den nächsten Spielzug des Clients zurück
     */
    @Override
    public final int getNextMove() {
        int col = 0;
        try {
            col = Integer.parseInt(in.readLine());
        } catch (IOException ex) {
            Logger.getLogger(ServerPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return col;
    }

    /**
     * Zeichnet das Spielfeld anhand des aktuellen Models neu.
     *
     * @param o Observer vom Typ GameModel
     * @param arg Argumentobjekt
     */
    @Override
    public final void update(final Observable o, final Object arg) {
        GameModel tempModel = (GameModel) o;
        try {
            out.reset();
            out.writeObject(tempModel);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(ServerPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Kontrolliert ob ein Zug gültig ist oder nicht.
     *
     */
    @Override
    public void invalidMove() {
        this.gameui.invalidMove();
    }

    /**
     * Kontroliert ob das Spiel gewonnen wurde.
     *
     */
    @Override
    public void youWin() {
        this.gameui.disableColumnButtons();
        this.gameui.disableSaveButton();
        this.gameui.youWin();
    }

    /**
     * Kontrolliert ob das Spiel verloren wurde.
     *
     */
    @Override
    public void youLose() {
        this.gameui.disableColumnButtons();
        this.gameui.disableSaveButton();
        this.gameui.youLose();
    }

    /**
     * Kontrolliert ob ein Unentschieden erreicht wurde.
     *
     */
    @Override
    public void draw() {
        this.gameui.disableColumnButtons();
        this.gameui.disableSaveButton();
        this.gameui.draw();
    }
}
