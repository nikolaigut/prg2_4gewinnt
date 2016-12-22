package gameView;

import gameControl.GameControl;
import gameModel.GameModel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import opponent.LocalPlayer;
import opponent.Opponent;
import opponent.networkPlayer.ClientThread;
import opponent.networkPlayer.ServerThread;

public class ConnectFourGui implements Observer {

    // Attribute
    private JFrame frame;
    private JPanel mainGround;
    private JPanel playButton;
    private JPanel playBoard;
    private JPanel playerStatus;

    private JMenu menu;
    private JMenuBar menuBar;
    private JMenuItem local;
    private JMenuItem computer;
    private JMenuItem client;
    private JMenuItem server;
    private JMenuItem save;
    private JMenuItem load;

    private JButton column1 = new JButton("1");
    private JButton column2 = new JButton("2");
    private JButton column3 = new JButton("3");
    private JButton column4 = new JButton("4");
    private JButton column5 = new JButton("5");
    private JButton column6 = new JButton("6");
    private JButton column7 = new JButton("7");

    private JTextArea player1 = new JTextArea("Player 1");
    private JTextArea player2 = new JTextArea("Player 2");
    private boolean statusPlayer1;
    private boolean statusPlayer2;

    private JTextField textField = new JTextField(0);

    private Circle[][] circleBoard;
    private int[][] colorBoard;
    private int[][] colorBoardCurrent;
    private int circleBoardRow = 6;
    private int circleBoardColumn = 7;
    private int currentColor = 1;
    private int currentRow = 5;
    private int currentColumn = 5;

    private GameControl gameControl;

    // Konstruktor
    public ConnectFourGui(GameControl gameControl) {

        this.gameControl = gameControl;
        createFrame();
    }

    public void invalidMove() {
        JOptionPane.showMessageDialog(null, "Dies ist ein Ungültiger Zug", "Ungültiger Zug", JOptionPane.ERROR_MESSAGE);
    }

    public void youWin(int playerId) {
        this.frame.revalidate();
        this.frame.repaint();

        JOptionPane.showMessageDialog(null, "Glückwunsch du hast gewonnen Player " + playerId, "Gewonnen!!!", JOptionPane.INFORMATION_MESSAGE);
    }

    public void youLose() {
        this.frame.revalidate();
        this.frame.repaint();

        JOptionPane.showMessageDialog(null, "Du hast leider verloren", "Verloren!!!", JOptionPane.INFORMATION_MESSAGE);
    }

    public void draw() {
        this.frame.revalidate();
        this.frame.repaint();

        JOptionPane.showMessageDialog(null, "Das Spiel endet Unentschieden", "Unentschieden!!!", JOptionPane.INFORMATION_MESSAGE);
    }

    public void disableColumnButtons() {
        Component[] allButtons = playButton.getComponents();

        for (Component button : allButtons) {
            button.setEnabled(false);
        }
    }

    public void enableColumnButtons() {
        Component[] allButtons = playButton.getComponents();
        for (Component button : allButtons) {
            button.setEnabled(true);
        }
    }

    public void disableSaveButton() {
        this.save.setEnabled(false);
    }

    public void enableSaveButton() {
        this.save.setEnabled(true);
    }

    // Frame erzeugen
    private void createFrame() {
        frame = new JFrame();
        frame.setSize(900, 770);
        frame.setResizable(false);
        frame.setTitle("Viergewinnt");

        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        frame.setLayout(new BorderLayout());
        frame.add(createMainGround(), BorderLayout.CENTER);
        frame.add(createPlayerStatus(), BorderLayout.EAST);
        frame.add(new JLabel("    "), BorderLayout.NORTH);
        frame.add(new JLabel("    "), BorderLayout.SOUTH);
        frame.add(new JLabel("    "), BorderLayout.WEST);
        createMenu();
        frame.setJMenuBar(menuBar);

        frame.setVisible(true);
    }

    // Getter Methode Frame
    private JFrame getFrame() {

        return frame;
    }

    // Menu erzeugen 
    private void createMenu() {

        menu = new JMenu("Menu");
        menuBar = new JMenuBar();
        local = new JMenuItem("local");
        computer = new JMenuItem("computer");
        client = new JMenuItem("client");
        server = new JMenuItem("server");
        save = new JMenuItem("save");
        load = new JMenuItem("load");
        menu.add(local);
        menu.add(computer);
        menu.add(client);
        menu.add(server);
        menu.add(save);
        menu.add(load);
        menuBar.add(menu);

        local.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuLocalStart(e);
            }
        });

        computer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuComputerStart(e);
            }
        });

        client.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuClientStart(e);
            }
        });

        server.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuServerStart(e);
            }
        });

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuSave(e);
            }
        });

        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuLoad(e);
            }
        });
    }

    private void menuLocalStart(ActionEvent e) {
        this.gameControl.createLocalGame();
    }

    private void menuComputerStart(ActionEvent e) {
        this.gameControl.createLocalComputerGame();
    }

    private void menuClientStart(ActionEvent e) {
        ServerThread serverThread = new ServerThread(this.gameControl, null);

        serverThread.start();
    }

    private void menuServerStart(ActionEvent e) {
        try {
            ClientThread clientThread = new ClientThread(this.gameControl, null, InetAddress.getLocalHost().getHostName());
            clientThread.start();
        } catch (UnknownHostException ex) {
            Logger.getLogger(ConnectFourGui.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void menuSave(ActionEvent e) {
        this.gameControl.saveGame("c:\\connect4save.save");
    }

    private void menuLoad(ActionEvent e) {
        this.gameControl.loadGame("c:\\connect4save.save");
    }

    // Hauptfeld erzeugen
    private JPanel createMainGround() {

        mainGround = new JPanel(new BorderLayout());
        mainGround.add(createPlayButton(), BorderLayout.NORTH);
        mainGround.add(createPlayBoard(), BorderLayout.CENTER);
        mainGround.setVisible(true);

        return mainGround;
    }

    // Spielsteine-Einwurf erzeugen
    private JPanel createPlayButton() {

        playButton = new JPanel(new GridLayout());

        playButton.add(this.column1);
        playButton.add(this.column2);
        playButton.add(this.column3);
        playButton.add(this.column4);
        playButton.add(this.column5);
        playButton.add(this.column6);
        playButton.add(this.column7);
        disableColumnButtons();
        playButton.setVisible(true);

        return playButton;
    }

    // Spielfeld erzeugen
    private JPanel createPlayBoard() {

        colorBoard = new int[circleBoardRow][circleBoardColumn];
        colorBoardCurrent = new int[circleBoardRow][circleBoardColumn];
        playBoard = new JPanel(new GridLayout(6, 7));
        circleBoard = new Circle[circleBoardRow][circleBoardColumn];
        for (int i = 0; i < circleBoardRow; i++) {
            for (int j = 0; j < circleBoardColumn; j++) {
                switch (colorBoard[i][j]) {
                    case 1:
                        circleBoard[i][j] = new Circle(1);
                        break;
                    case 2:
                        circleBoard[i][j] = new Circle(2);
                        break;
                    default:
                        circleBoard[i][j] = new Circle(3);
                        break;
                }
            }
        }

        for (int i = 0; i < circleBoardRow; i++) {
            for (int j = 0; j < circleBoardColumn; j++) {
                playBoard.add(circleBoard[i][j]);
            }
        }
        playBoard.setVisible(true);

        return playBoard;
    }


    // Spielstatus (Wer ist dran) erzeugen
    private JPanel createPlayerStatus() {

        playerStatus = new JPanel(new FlowLayout());
        playerStatus.setPreferredSize(new Dimension(100, 100));
        playerStatus.add(player1);
        playerStatus.add(player2);
        player1.setBackground(Color.red);
        player2.setBackground(Color.yellow);
        player1.setFont(new Font("Dialog", 0, 25));
        player2.setFont(new Font("Dialog", 0, 25));

        statusPlayer1 = false;
        statusPlayer2 = false;

        if (currentColor == 1) {
            statusPlayer1 = true;
        } else if (currentColor == 2) {
            statusPlayer2 = true;
        }
        player1.setVisible(statusPlayer1);
        player2.setVisible(statusPlayer2);
        this.playerStatus.setVisible(true);

        return playerStatus;
    }

    public void addColumnButtonListener(ActionListener listener) {
        removeColumnButtonListener();
        // Liest alle Spalten Buttons aus
        Component[] allButtons = playButton.getComponents();

        // Fügt allen Spalten Buttons einen ActionListener hinzu
        for (Component component : allButtons) {
            JButton button = (JButton) component;
            button.addActionListener(listener);
        }
    }

    /**
     * Fügt eine Liste von ActionListener zu jedem Spaltenbutton hinzu
     *
     * @param allListener Liste mit ActionListener
     */
    public void addColumnButtonListener(ActionListener[] allListener) {
        removeColumnButtonListener();
        // Liest alle Spalten Buttons aus
        Component[] allButtons = playButton.getComponents();

        // Fügt allen Spalten Buttons eine Liste von ActionListener hinzu
        for (Component component : allButtons) {
            JButton button = (JButton) component;
            for (ActionListener listener : allListener) {
                button.addActionListener(listener);
            }
        }
    }

    private void removeColumnButtonListener() {
        // Liest alle Spalten Buttons aus
        Component[] allButtons = playButton.getComponents();

        // Fügt allen Spalten Buttons einen ActionListener hinzu
        for (Component component : allButtons) {
            JButton button = (JButton) component;
            for (ActionListener actionListener : button.getActionListeners()) {
                if (actionListener != null) {
                    button.removeActionListener(actionListener);
                }
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        GameModel model = (GameModel) o;
        int[][] gameMatrix = model.getGameMatrix();

        playBoard.removeAll();
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                switch (gameMatrix[row][col]) {
                    case 1:
                        circleBoard[5 - row][col] = new Circle(1);
                        break;
                    case 2:
                        circleBoard[5 - row][col] = new Circle(2);
                        break;
                    default:
                        circleBoard[5 - row][col] = new Circle(3);
                }
            }
        }
        for (int i = 0; i < circleBoardRow; i++) {
            for (int j = 0; j < circleBoardColumn; j++) {
                playBoard.add(circleBoard[i][j]);
            }
        }
        playBoard.updateUI();

        if (model.getPlayerOne() != null) {
            updatePlayer(model.getPlayerOne(), model.getPlayerTwo(), model.getCurrentPlayer());

            frame.remove(playerStatus);
            frame.add(createPlayerStatus(), BorderLayout.EAST);
        } else {
            disableColumnButtons();
        }

    }

    public void updatePlayer(Opponent playerOne, Opponent playerTwo, Opponent currentPlayer) {
        if (currentPlayer.getId() != playerOne.getId()) {
            currentColor = 2;

            if (!(playerTwo instanceof LocalPlayer)) {
                disableColumnButtons();
            }
        } else {
            currentColor = 1;
            if (!(playerTwo instanceof LocalPlayer)) {
                enableColumnButtons();
            }
        }
    }

}
