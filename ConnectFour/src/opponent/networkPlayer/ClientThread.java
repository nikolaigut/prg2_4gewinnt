package opponent.networkPlayer;

import gameControl.GameControl;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JDialog;

/**
 * Thread zum Verbindungsaufbau mit einem Server aus Clientsicht.
 *
 * @author A. Morard
 */
public class ClientThread extends Thread {
    private final GameControl control;
    private final JDialog dialog;
    private Socket socket;
    private final String hostname;
    private boolean exit;
    private boolean isConnected;
    private int port;

    /**
     * Erstellt einen neuen Thread für den Verbindungsaufbau.
     *
     * @param control die Instanz des GameControllers
     * @param panel das Panel für die Eingabe
     * @param hostname der Hostname
     */
    public ClientThread(final GameControl control, final JDialog panel, final String hostname) {
        super("Client Thread");

        this.control = control;
        this.dialog = panel;
        this.hostname = hostname;
    }

    /**
     * Bricht den Verbindunsaufbau ab.
     */
    public final void exit() {
        this.exit = true;
        this.dialog.setVisible(false);
    }

    /**
     * Ablauf des Verbindungsaufbaus zum Server.
     */
    @Override
    public final void run() {
        try {
            do {
                this.socket = new Socket(hostname, port);
                this.isConnected = true;
            } while (!this.isConnected && !this.exit);
        } catch (UnknownHostException uhe) {
            System.out.println(uhe + " Host not found");
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
        if (this.isConnected) {
            this.dialog.setVisible(false);
            System.out.println(this.socket instanceof Socket);
            this.control.createClientGame();
        }
    }
}
