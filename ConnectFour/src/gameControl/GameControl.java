package gameControl;

import gameView.ConnectFourGui;
import gameModel.GameModel;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import opponent.KIPlayer;
import opponent.LocalPlayer;
import opponent.Opponent;
import util.GameMode;

/**
 * <!-- begin-user-doc -->
 * <!--  end-user-doc  --> @generated
 */
public class GameControl implements Runnable {

    /**
     * <!-- begin-user-doc -->
     * <!--  end-user-doc  --> @generated @ordered
     */

    private Thread game;

    /**
     * <!-- begin-user-doc -->
     * <!--  end-user-doc  --> @generated @ordered
     */
    private ConnectFourGui gameUi;

    /**
     * <!-- begin-user-doc -->
     * <!--  end-user-doc  --> @generated @ordered
     */
    private GameModel gameModel;

    /**
     * <!-- begin-user-doc -->
     * <!--  end-user-doc  --> @generated @ordered
     */
    private static int networkPort;

    /**
     * <!-- begin-user-doc -->
     * <!--  end-user-doc  --> @generated @ordered
     */
    public GameControl() {
        this.gameModel = new GameModel();
        this.gameUi = new ConnectFourGui(this);
    }

    /**
     * <!-- begin-user-doc -->
     * <!--  end-user-doc  --> @generated @ordered
     */
    public void newGame(GameMode gameType) {
        interruptGameCycle();
        switch (gameType) {
            case LOCAL:
                createLocalGame();
                break;
            case LOCAL_COMPUTER:
                createLocalComputerGame();
                break;
            case NETWORK_SERVER:
                createServerGame();
                break;
            case NETWORK_CLIENT:
                createClientGame();
                break;
            default:
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!--  end-user-doc  --> @generated @ordered
     */
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

    /**
     * <!-- begin-user-doc -->
     * <!--  end-user-doc  --> @generated @ordered
     */
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

    /**
     * <!-- begin-user-doc -->
     * <!--  end-user-doc  --> @generated @ordered
     */
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

    /**
     * <!-- begin-user-doc -->
     * <!--  end-user-doc  --> @generated @ordered
     */
    public void createLocalGame() {
        LocalPlayer playerOne = new LocalPlayer();
        LocalPlayer playerTwo = new LocalPlayer();
        
        this.getGameModel().init(playerOne, playerTwo);
        this.game = new Thread(this, "Run local game");
        this.getGame().start();
    }

    /**
     * <!-- begin-user-doc -->
     * <!--  end-user-doc  --> @generated @ordered
     */
    public void createLocalComputerGame() {
        LocalPlayer playerOne = new LocalPlayer();
        KIPlayer playerTwo = new KIPlayer();
        
        this.getGameModel().init(playerOne, playerTwo);
        this.game = new Thread(this, "Run local game");
        this.getGame().start();
    }

    /**
     * <!-- begin-user-doc -->
     * <!--  end-user-doc  --> @generated @ordered
     */
    public void createServerGame() {
        // TODO implement me
    }

    /**
     * <!-- begin-user-doc -->
     * <!--  end-user-doc  --> @generated @ordered
     */
    public void createClientGame() {
        // TODO implement me
    }

    /**
     * <!-- begin-user-doc -->
     * <!--  end-user-doc  --> @generated @ordered
     */
    public void run() {
        int columnNr;
        while(!Thread.interrupted()){
            columnNr = this.gameModel.getCurrentPlayer().getNextMove();
            this.gameModel.insertDisc(columnNr);
            if(this.gameModel.isDraw()){
                this.gameModel.getPlayerOne().draw();
                this.gameModel.getPlayerTwo().draw();
            }else if(this.gameModel.isWon()){
                this.gameModel.getCurrentPlayer().win();
            }else if(this.gameModel.isLose()){
                this.gameModel.getCurrentPlayer().lose();
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
