package opponent.networkPlayer;

import gameModel.GameModel;
import opponent.Opponent;
import java.io.*;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ConnectFour ServerPlayer representing a connecting player in a network game.
 *
 * Dient zur Kommunikation mit dem ClientPlayer
 *
 * @author A. Morard
 *
 */
public class ServerPlayer extends Opponent implements Observer {

    //FIELDS--------------------------------------------------------------------
    private transient ObjectOutputStream out;
    private transient BufferedReader in;

    //CONSTRUCTORS--------------------------------------------------------------
    /**
     * Erstellt einen neuen Server Spieler (zur Kommunikation mit dem Client)
     *
     * @param id
     * @param clientSocket
     */
    public ServerPlayer(int id, Socket clientSocket) {
        super(id);

        try {

            this.out = new ObjectOutputStream(clientSocket.getOutputStream());
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            System.out.println("Partner: " + clientSocket.getInetAddress().getHostAddress());
        } catch (IOException ex) {
            Logger.getLogger(ServerPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //PUBLIC METHODS------------------------------------------------------------
    /**
     * Fragt beim Client nach dem nächsten Zug
     *
     * @return Gibt den nächsten Spielzug des Clients zurück
     */
    @Override
    public int getNextMove() {
        int col = 0;
        try {
            //Spaltennummer vom Client erhalten
            col = Integer.parseInt(in.readLine());
        } catch (IOException ex) {
            Logger.getLogger(ServerPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return col;
    }

    /**
     * Sorgt dafür dass das GUI beim Client neu gezeichnet wird
     *
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        //Aktuelle Matrix an den Client schicken
        GameModel tempModel = (GameModel) o;

        try {
            out.reset();
            out.writeObject(tempModel);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(ServerPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
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
