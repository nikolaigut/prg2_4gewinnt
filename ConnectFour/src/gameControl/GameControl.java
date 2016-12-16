package gameControl;

import gameView.ConnectFourGui;
import gameModel.GameModel;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import opponent.KIPlayer;
import opponent.LocalPlayer;
import opponent.Opponent;
import opponent.networkPlayer.ClientPlayer;
import opponent.networkPlayer.ServerPlayer;
import util.GameMode;

/**
 * This class is the control of the connect4 game
 * @author Nikolai Strässle <nikolai.straessle@stud.hslu.ch>
 */

public class GameControl implements Runnable {

    

    private Thread game;

    
    private ConnectFourGui gameUi;

    
    private GameModel gameModel;

    
    private static int networkPort;

    
    public GameControl() {
        this.gameModel = new GameModel();
        this.gameUi = new ConnectFourGui(this);
    }

    
    public void newGame(GameMode gameType) {
        interruptGameCycle();
        switch (gameType) {
            case LOCAL:
                createLocalGame();
                break;
            case LOCAL_COMPUTER:
                createLocalComputerGame();
                break;
            default:
                createLocalComputerGame();
        }
    }

    
    public void saveGame(String path) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(path);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(this.getGameModel());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GameControl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GameControl.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
           if(oos != null) {
               try {
                   oos.close();
               } catch (IOException ex) {
                   Logger.getLogger(GameControl.class.getName()).log(Level.SEVERE, null, ex);
               }
           }
           if(fos != null){
               try {
                   fos.close();
               } catch (IOException ex) {
                   Logger.getLogger(GameControl.class.getName()).log(Level.SEVERE, null, ex);
               }
           }
        }
    }

    
    public void loadGame(String path) {
        interruptGameCycle();
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(path);
            ois = new ObjectInputStream(fis);
            GameModel loadedGameModel = (GameModel)ois.readObject();
            createLoadGame(loadedGameModel);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GameControl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GameControl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GameControl.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if(ois != null){
                try {
                    ois.close();
                } catch (IOException ex) {
                    Logger.getLogger(GameControl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException ex) {
                    Logger.getLogger(GameControl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    
    public void createLoadGame(GameModel gameModel) {
        LocalPlayer playerOne = (LocalPlayer)gameModel.getPlayerOne();
        Opponent playerTwo = gameModel.getPlayerTwo();
        
        this.getGameModel().init(playerTwo, playerTwo, gameModel.getGameMatrix(), gameModel.getCurrentPlayer());
        this.getGameModel().addObserver(getGameUi());
        
        if(playerTwo instanceof KIPlayer){
            KIPlayer kiPlayer = (KIPlayer)playerTwo;
            
        }else if(playerTwo instanceof LocalPlayer){
            LocalPlayer loaclPlayer = (LocalPlayer)playerTwo;
        }
        
        this.game = new Thread(this, "Run loaded game");
        this.getGame().start();
    }

    
    public void createLocalGame() {
        LocalPlayer playerOne = new LocalPlayer(1, gameUi);
        LocalPlayer playerTwo = new LocalPlayer(2, gameUi);
        
        this.gameModel.addObserver(gameUi);
        ActionListener[] actionListeners = {playerOne, playerTwo};
        
        this.gameUi.addColumnButtonListener(actionListeners);
        
        this.gameModel.init(playerOne, playerTwo);
        this.game = new Thread(this, "Run local game");
        this.getGame().start();
    }

    
    public void createLocalComputerGame() {
        LocalPlayer playerOne = new LocalPlayer(1, gameUi);
        KIPlayer playerTwo = new KIPlayer(2, gameUi);
        
        this.gameModel.addObserver(gameUi);
        
        
        
        this.gameModel.init(playerOne, playerTwo);
        this.game = new Thread(this, "Run local game");
        this.getGame().start();
    }

    
    public void createServerGame(Socket clientSocket) {
        LocalPlayer playerOne = new LocalPlayer(1, this.gameUi);
        ServerPlayer playerTwo = new ServerPlayer(2, clientSocket);
        
        this.gameModel.addObserver(gameUi);
        this.gameModel.addObserver(playerTwo);
        
        this.gameUi.addColumnButtonListener(playerOne);
        
        this.gameModel.init(playerOne, playerTwo);
        
        this.gameUi.enableColumnButtons();
        this.gameUi.disableSaveButton();
        
        this.game = new Thread(this, "Server game");
        this.game.start();
        
    }

    
    public void createClientGame(Socket serverSocket) {
        ClientPlayer clientPlayer = new ClientPlayer(serverSocket, this.gameUi);
        this.gameUi.addColumnButtonListener(clientPlayer);
        this.gameUi.enableColumnButtons();
        this.gameUi.disableSaveButton();
        
        
    }

    
    public void run() {
        int columnNr;
        while(!Thread.interrupted()){
            columnNr = this.gameModel.getCurrentPlayer().getNextMove();
            columnNr--;
            this.gameModel.insertDisc(columnNr);
            if(this.gameModel.isDraw()){
                this.gameModel.getPlayerOne().draw();
                this.gameModel.getPlayerTwo().draw();
            }else if(this.gameModel.isWon()){
                this.gameModel.getCurrentPlayer().youWin();
            }else if(this.gameModel.isLose()){
                this.gameModel.getCurrentPlayer().youLose();
            }else{
                this.gameModel.changePlayer();
            }
            this.gameModel.notifyObservers();
        }
    }
    
    private void interruptGameCycle() {
        // Game-Thread unterbrechen
        if (this.getGame() != null) {
            while (this.getGame().isAlive()) {
                this.getGame().interrupt();
            }
        }
    }

    /**
     * @return the game
     */
    public Thread getGame() {
        return game;
    }

    /**
     * @return the gameUi
     */
    public ConnectFourGui getGameUi() {
        return gameUi;
    }

    /**
     * @return the gameModel
     */
    public GameModel getGameModel() {
        return gameModel;
    }

}
