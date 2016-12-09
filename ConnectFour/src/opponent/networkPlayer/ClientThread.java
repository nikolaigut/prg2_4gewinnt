package opponent.networkPlayer;

import gameControl.GameControl;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JDialog;

/**
 * ConnectFour ClientThread
 *
 * Thread zum Verbindungsaufbau mit einem Server
 *
 * @author A. Morard
 *
 * @version 1.0
 */
public class ClientThread extends Thread {

    //FIELDS--------------------------------------------------------------------
    private final GameControl control;
    private final JDialog dialog;
    private Socket socket;
    private final String hostname;
    private boolean exit;
    private boolean isConnected;
    private int port;

    //CONSTRUCTORS--------------------------------------------------------------
    /**
     * Erstellt einen neuen Thread zum Verbindungsaufbau
     *
     * @param control
     * @param panel
     * @param hostname
     */
    public ClientThread(GameControl control, JDialog panel, String hostname) {
        super("Client Thread");

        this.control = control;
        this.dialog = panel;
        this.hostname = hostname;
    }

    //PUBLIC METHODS------------------------------------------------------------
    /**
     * Bricht den Verbindunsaufbau ab
     */
    public void exit() {
        this.exit = true;
        this.dialog.setVisible(false);
    }

    /**
     * Ablauf des Verbindungsaufbaus zum Server
     */
    @Override
    public void run() {
        try{
            do {
                this.socket = new Socket(hostname, port);
                this.isConnected = true;
            } while (!this.isConnected && !this.exit);
        } catch (UnknownHostException uhe) {
            System.out.println(uhe + " Host not found");
        }
        catch (IOException ioe) {
            System.out.println(ioe);
        }
        if (this.isConnected) {
            this.dialog.setVisible(false);
            System.out.println(this.socket instanceof Socket);
            this.control.createClientGame();
        }
    }
}
