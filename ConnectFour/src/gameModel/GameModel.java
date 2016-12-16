package gameModel;

import java.io.Serializable;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import opponent.Opponent;

/**
 * This class will describe the game model
 * @author Nikolai Strässle <nikolai.straessle@stud.hslu.ch>
 */

public class GameModel extends Observable implements Serializable {

    
    private int[][] gameMatrix;

    
    private Opponent playerOne;

    
    private Opponent playerTwo;

    
    private int rowLenght;

    
    private int columnLenght;

    
    private boolean draw;

    
    private boolean invalid;

    
    private boolean won;

    
    private boolean lose;

    
    private Opponent currentPlayer;

    
    public GameModel() {
        this.columnLenght = 7;
        this.rowLenght = 6;
    }

    
    public void init(Opponent playerOne, Opponent playerTwo) {
        setChanged();
        this.gameMatrix = new int[this.rowLenght][this.columnLenght];

        this.playerOne = playerOne;
        this.playerTwo = playerTwo;

        this.currentPlayer = playerOne;
        notifyObservers();
    }

    
    public void init(Opponent playerOne, Opponent playerTwo, int[][] gameMatrix, Opponent currentPlayer) {
        setChanged();
        this.gameMatrix = gameMatrix;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;

        this.currentPlayer = currentPlayer;
        notifyObservers();
    }

    
    public void insertDisc(int columnNr) {
        try {
            int row = getRowToInsert(columnNr);

            setChanged();
            this.gameMatrix[row][columnNr] = this.currentPlayer.getId();
            notifyObservers();

            checkState(row, columnNr);
        } catch (Exception ex) {
            Logger.getLogger(GameModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    public void checkState(int row, int col) {
        if (checkDraw()) {
            this.draw = true;
            return;
        }
        if (checkHorizontal(row) || checkVertikal(col) || checkMainDiag(row, col) || checkNextDiag(row, col)) {
            this.won = true;
            return;
        }
    }

    
    public int getRowToInsert(int columnNr) throws Exception {
        int rowToInsert = -1;

        for (int i = 0; i < this.rowLenght; i++) {
            if (this.gameMatrix[i][columnNr] != 0) {
                rowToInsert++;
            } else {
                return i;
            }
        }
        if (rowToInsert >= this.rowLenght || rowToInsert < 0) {
            throw new Exception("Unable to insert disc");
        }
        return rowToInsert;
    }

    
    public void changePlayer() {
        setChanged();
        if (this.currentPlayer == this.playerOne) {
            this.currentPlayer = this.playerTwo;
        } else {
            this.currentPlayer = this.playerOne;
        }
        notifyObservers();
    }

    
    public Opponent getPlayerOne() {
        return this.playerOne;
    }

    
    public Opponent getPlayerTwo() {
        return this.playerTwo;
    }

    
    public int[][] getGameMatrix() {
        return this.gameMatrix;
    }

    
    public Opponent getCurrentPlayer() {
        return this.currentPlayer;
    }

    public boolean isDraw() {
        return draw;
    }

    public boolean isLose() {
        return lose;
    }

    public boolean isWon() {
        return won;
    }

    private boolean checkDraw() {
        for (int row = 0; row < this.rowLenght; row++) {
            for (int col = 0; col < this.columnLenght; col++) {
                if (gameMatrix[row][col] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkHorizontal(int row) {
        int counter = 0;
        int discsInARow = 0;
        for (int i = 0; i < this.columnLenght; i++) {
            if (this.gameMatrix[row][i] == this.currentPlayer.getId()) {
                counter++;
            } else {
                counter = 0;
            }
            if (counter > discsInARow) {
                discsInARow = counter;
            }
        }

        if (discsInARow >= 4) {
            return true;
        }
        return false;
    }

    private boolean checkVertikal(int col) {
        int counter = 0;
        int discsInARow = 0;

        for (int i = 0; i < this.rowLenght; i++) {
            if (this.gameMatrix[i][col] == this.currentPlayer.getId()) {
                counter++;
            } else {
                counter = 0;
            }
            if (counter > discsInARow) {
                discsInARow = counter;
            }
        }

        if (discsInARow >= 4) {
            return true;
        }
        return false;
    }

    private boolean checkMainDiag(int row, int col) {
        // Nach oben links suchen
        int counter = 1;
        int discsInARow = 1;
        for (int i = 1; i <= (Math.min(0 + col, 0 + row)); i++) {
            if (this.gameMatrix[row - i][col - i] == this.currentPlayer.getId()) {
                counter += 1;
            } else {
                counter = 0;
            }
            if (counter > discsInARow) {
                discsInARow = counter;
            }
        }

        // Wenn gegen Links Oben keine Steinreihe gefunden wurde
        if (counter < 1) {
            counter = 1;
        }

        // Nach unten rechts suchen
        for (int i = 1; i <= Math.min(6 - col, 5 - row); i++) {
            if (this.gameMatrix[row + i][col + i] == this.currentPlayer.getId()) {
                counter += 1;
            } else {
                counter = 0;
            }
            if (counter > discsInARow) {
                discsInARow = counter;
            }
        }

        if (discsInARow >= 4) {
            return true;
        }

        return false;
    }

    private boolean checkNextDiag(int row, int col) {
        // Nach oben rechts suchen
        int counter = 1;
        int discsInARow = 1;
        for (int i = 1; i <= (Math.min(6 - col, 0 + row)); i++) {
            if (this.gameMatrix[row - i][col + i] == this.currentPlayer.getId()) {
                counter += 1;
            } else {
                counter = 1;
            }
            if (counter > discsInARow) {
                discsInARow = counter;
            }
        }

        // Wenn gegen Rechts Oben keine Steinreihe gefunden wurde
        if (counter < 1) {
            counter = 1;
        }

        // Nach unten links suchen
        for (int i = 1; i <= Math.min(0 + col, 5 - row); i++) {
            if (this.gameMatrix[row + i][col - i] == this.currentPlayer.getId()) {
                counter += 1;
            } else {
                counter = 1;
            }
            if (counter > discsInARow) {
                discsInARow = counter;
            }
        }

        if (discsInARow >= 4) {
            return true;
        }
        return false;
    }

}
