package gameView;
import gameControl.GameControl;
import gameModel.GameModel;
import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
//import java.awt.event.*;

public class ConnectFourGui implements Observer { // implements ActionListener {
    
    // Attribute
    
    private JFrame frame;
    private JPanel mainGround;
    private JPanel playButton;
    private JPanel playBoard;
    private JPanel textBoard;
    private JPanel playerStatus;
       
    private JButton column1 = new JButton("col 1");
    private JButton column2 = new JButton("col 2");
    private JButton column3 = new JButton("col 3");
    private JButton column4 = new JButton("col 4");
    private JButton column5 = new JButton("col 5");
    private JButton column6 = new JButton("col 6");
    private JButton column7 = new JButton("col 7");
    
    private JTextArea player1 = new JTextArea("Player 1");
    private JTextArea player2 = new JTextArea("Player 2");
    private boolean statusPlayer1;
    private boolean statusPlayer2;
    
    private JTextField textField = new JTextField(0);
    
    private Circle[][] circleBoard;
    private int [][] colorBoard;
    private int [][] colorBoardCurrent;
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
        
        //column1.addActionListener(this);
        //column2.addActionListener(this);
        //column3.addActionListener(this);
        //column4.addActionListener(this);
        //column5.addActionListener(this);
        //column6.addActionListener(this);
        //column7.addActionListener(this);
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
        
        frame.setVisible(true);
    }
    
    // Getter Methode Frame
    
    private JFrame getFrame() {
        return frame;
    }
    
    // Menu erzeugen 
    
    // Hauptfeld erzeugen
    
    private JPanel createMainGround() {
        
        mainGround = new JPanel(new BorderLayout());
        mainGround.add(createPlayButton(), BorderLayout.NORTH);
        mainGround.add(createPlayBoard(), BorderLayout.CENTER);
        mainGround.add(createTextBoard(), BorderLayout.SOUTH);
        
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
        playButton.setVisible(true);
        
        return playButton;
    }
    
    // Spielfeld erzeugen
    
    private JPanel createPlayBoard() {
        
        colorBoard = new int[circleBoardRow][circleBoardColumn];
        colorBoardCurrent = new int[circleBoardRow][circleBoardColumn];
        playBoard = new JPanel(new GridLayout(6,7));
        circleBoard = new Circle[circleBoardRow][circleBoardColumn];
        for(int i = 0; i < circleBoardRow; i++) {
            for(int j = 0; j < circleBoardColumn; j++) {
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
        
        for(int i = 0; i < circleBoardRow; i++) {
            for(int j = 0; j < circleBoardColumn; j++) {
                playBoard.add(circleBoard[i][j]);
            }
        }
        playBoard.setVisible(true);
        
        return playBoard;
    }
    // Textfeld am Board erzeugen
     
    private JPanel createTextBoard() {
        
        textBoard = new JPanel(new BorderLayout());
        textBoard.add(this.textField);
        textBoard.setVisible(true);
        textField.setFont(new Font("Dialog", 0, 20));
        
        if(currentColor == 1) {
            textField.setText("Player 1 ist an der Reihe");
        } else if(currentColor == 2){
            textField.setText("Player 2 ist an der Reihe");
        }
        
        return textBoard;
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
        
        if(currentColor == 1){
            statusPlayer1 = true;
        } else if(currentColor == 2) {
            statusPlayer2 = true;
        }
        player1.setVisible(statusPlayer1);
        player2.setVisible(statusPlayer2);
        this.playerStatus.setVisible(true);
        
        return playerStatus;
    }
    
    //@Override
    //public void actionPerformed(ActionEvent e) {
    //    if(e.getSource() == this.column1) {
    //        colorBoard[2][2] = 1;
    //    }
    //}

    @Override
    public void update(Observable o, Object arg) {
        GameModel model = (GameModel) o;
    }
    
}