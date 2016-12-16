package opponent.networkPlayer;

import gameView.ConnectFourGui;
import gameModel.GameModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Repräsentiert einen Client welcher über Netzwerk Spielt.
 *
 * @author A. Morard
 */
public class ClientPlayer implements ActionListener, Runnable {
    private final Socket socket;
    private PrintWriter out;
    private ObjectInputStream in;
    private final ConnectFourGui clientGui;

    /**
     * Erstellt einen neuen ClientSpieler.
     *
     * @param serverSocket Socket des Hosts
     * @param gamegui die Instanz des Spiels
     */
    public ClientPlayer(final Socket serverSocket, final ConnectFourGui gamegui) {
        this.socket = serverSocket;
        this.clientGui = gamegui;
        try {
            this.out = new PrintWriter(this.socket.getOutputStream(), true);
            this.in = new ObjectInputStream(this.socket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(ClientPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        new Thread(this, "clientThread").start();
    }

    /**
     * Übermittlet den Spielzug des Clients an den Host.
     *
     * @param e das Event das übermittelt werden soll
     */
    @Override
    public final void actionPerformed(final ActionEvent e) {
        String col = e.getActionCommand();
        out.println(col);
        out.flush();
    }

    /**
     * Beinhaltet den Ablauf des Clients.
     *
     */
    @Override
    public final void run() {
        while (!Thread.interrupted()) {
            try {
                GameModel tempModel = (GameModel) in.readObject();
                tempModel.changePlayer();
                if (tempModel.getPlayerTwo().equals(tempModel.getCurrentPlayer())) {
                    this.clientGui.disableColumnButtons();
                } else {
                    this.clientGui.enableColumnButtons();
                }
                this.clientGui.update(tempModel, null);
                /**
                if (tempModel.isInvalid()) {
                    this.clientGui.invalidMove();
                }
                */
                if (tempModel.isDraw()) {
                    this.clientGui.draw();
                }
                if (tempModel.isLose()) {
                    this.clientGui.youLose();
                }
                if (tempModel.isWon()) {
                    this.clientGui.youWin();
                }
            } catch (IOException ex) {
                System.out.println("IOException");
                Logger.getLogger(ClientPlayer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClientPlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            this.socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
