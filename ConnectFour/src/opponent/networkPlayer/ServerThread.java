package opponent.networkPlayer;

import gameControl.GameControl;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;

/**
 * Thread zum Verbindungsaufbau mit einem Server aus Serversicht.
 *
 * @author A. Morard
 */
public class ServerThread extends Thread {
    private final GameControl control;
    private final JDialog panel;
    private ServerSocket serverSocket;
    private int port;

    /**
     * Erstellt einen neuen Thread für den Verbindungsaufbau.
     *
     * @param control die Instanz des GameControllers
     * @param panel das Panel für die Eingabe
     */
    public ServerThread(final GameControl control, final JDialog panel) {
        super("Server Thread");
        this.control = control;
        this.panel = panel;
    }

    /**
     * Bricht den Verbindunsaufbau ab.
     */
    public final void exit() {
        if (this.serverSocket instanceof ServerSocket) {
            try {
                this.serverSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Ablauf des Verbindungsaufbaus zum Server.
     */
    @Override
    public final void run() {
        try {
            this.serverSocket = new ServerSocket(port);
            Socket clientSocket = serverSocket.accept();
            this.panel.setVisible(false);
            this.control.createServerGame();
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
