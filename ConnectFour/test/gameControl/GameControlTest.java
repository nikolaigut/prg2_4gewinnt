/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameControl;

import org.junit.Test;
import static org.junit.Assert.*;
import util.GameMode;

/**
 *
 * @author STNIK
 */
public class GameControlTest {
    
    public GameControlTest() {
        
    }

    /**
     * Test of newGame method, of class GameControl.
     */
    @Test
    public void testNewGame() {
        GameControl gameControl = new GameControl();
        gameControl.newGame(GameMode.LOCAL);
        gameControl.getGameModel().insertDisc(1);
        assertTrue(gameControl.getGame().isAlive());
    }

    /**
     * Test of saveGame method, of class GameControl.
     */
    @Test
    public void testSaveGame() {
    }

    /**
     * Test of loadGame method, of class GameControl.
     */
    @Test
    public void testLoadGame() {
    }

    /**
     * Test of createLoadGame method, of class GameControl.
     */
    @Test
    public void testCreateLoadGame() {
    }

    /**
     * Test of createLocalGame method, of class GameControl.
     */
    @Test
    public void testCreateLocalGame() {
    }

    /**
     * Test of createLocalComputerGame method, of class GameControl.
     */
    @Test
    public void testCreateLocalComputerGame() {
    }

    /**
     * Test of createServerGame method, of class GameControl.
     */
    @Test
    public void testCreateServerGame() {
    }

    /**
     * Test of createClientGame method, of class GameControl.
     */
    @Test
    public void testCreateClientGame() {
    }

    /**
     * Test of run method, of class GameControl.
     */
    @Test
    public void testRun() {
    }
    
}
