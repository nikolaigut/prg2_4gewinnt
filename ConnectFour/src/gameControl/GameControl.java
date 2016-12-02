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
            oos.writeObject(this.gameModel);
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
        
        this.gameModel.init(playerTwo, playerTwo, gameModel.getGameMatrix(), gameModel.getCurrentPlayer());
        this.gameModel.addObserver(gameUi);
        
        if(playerTwo instanceof KIPlayer){
            KIPlayer kiPlayer = (KIPlayer)playerTwo;
            
        }else if(playerTwo instanceof LocalPlayer){
            LocalPlayer loaclPlayer = (LocalPlayer)playerTwo;
        }
        
        this.game = new Thread(this, "Run loaded game");
        this.game.start();
    }

    /**
     * <!-- begin-user-doc -->
     * <!--  end-user-doc  --> @generated @ordered
     */
    public void createLocalGame() {
        
    }

    /**
     * <!-- begin-user-doc -->
     * <!--  end-user-doc  --> @generated @ordered
     */
    public void createLocalComputerGame() {
        // TODO implement me
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
        // TODO implement me
    }
    
    private void interruptGameCycle() {
        // Game-Thread unterbrechen
        if (this.game != null) {
            while (this.game.isAlive()) {
                this.game.interrupt();
            }
        }
    }

}
