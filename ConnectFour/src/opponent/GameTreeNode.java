package opponent;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * ConnectFour GameTreeNode
 *
 * Knoten eines GameTrees
 *
 * @author A. Morard
 *
 * @version 1.0
 */
public class GameTreeNode implements Serializable {

    //FIELDS--------------------------------------------------------------------
    private final int owner;
    private final int enemy;
    private int[][] curentmodel;
    private int movenr;
    private int bestmove = 3;
    private int value;
    private int knotentiefe = 0;  //ungerad maximierung  gerade minimierung
    private final int baumhoehe;
    private int[] validemoves;
    ArrayList<GameTreeNode> childnodes;  //max 7  0=Leaf
    GameTreeNode parentnode;

    //CONSTRUCTORS--------------------------------------------------------------
    /**
     * Erstellt einen neuen Knoten für den GameTree
     *
     * @param parent Elternknoten
     * @param curentmodel aktuelles Model welches der Knoten enthalten soll
     * @param baumhoehe gesamte Baumhöhe
     * @param movenr welcher Spielzug der Knoten repräsentiert (1-7)
     * @param owner wem swe Knoten gehört
     */
    public GameTreeNode(GameTreeNode parent, int[][] curentmodel, int baumhoehe, int movenr, int owner) {
        this.enemy = owner == 1 ? 2 : 1;
        this.owner = owner;
        this.movenr = movenr;
        this.curentmodel = curentmodel;
        this.parentnode = parent;
        this.baumhoehe = baumhoehe;
        knotentiefe++;
        this.validemoves = checkValideMoves(curentmodel);

        if (checkOwnWin(curentmodel, owner)) {
            //System.out.println("KI kann gewinnen!");
            return;
        } else if (checkEnemyWin(curentmodel, enemy)) {
            //System.out.println("Spieler kann gewinnen!");
            return;
        } else {
            if (knotentiefe <= baumhoehe) {
                //System.out.println("Erstelle neue Nodes für jeden gültigen Spielzug Ebene: "+knotentiefe );
                childnodes = new ArrayList<>(7);

                for (int i : validemoves) {
                    //System.out.println("Node für Spalte: "+i);
                    int[][] newmodel = new int[6][7];

                    for (int row = 0; row < 6; row++) {
                        for (int column = 0; column < 7; column++) {
                            newmodel[row][column] = curentmodel[row][column];
                        }
                    }

                    newmodel = insertMove(newmodel, i);
                    if (knotentiefe % 2 == 0) {
                        childnodes.add(new GameTreeNode(this, newmodel, baumhoehe - 1, i, this.owner));
                    } else {
                        childnodes.add(new GameTreeNode(this, newmodel, baumhoehe - 1, i, enemy));
                    }

                }
            }
            this.value = calcValue(curentmodel, movenr, owner);
        }
    }

    //GETTER and SETTER---------------------------------------------------------
    public int getValue() {
        return value;
    }

    public int getMoveNr() {
        return movenr;
    }

    public int getBestMove() {
        return bestmove;
    }

    //PRIVATE METHODS-----------------------------------------------------------
    /*
     * Prüft ob der Besitzer de Knotens gewinnen kann.
     *
     * @param matrix Matrix des Knotens
     * @param owner Besitzer des Knotens
     * @return true=Besitzern kann mit diesem Spielzug gewinnen
     */
    private boolean checkOwnWin(int[][] matrix, int owner) {

        for (int i : validemoves) {
            if (getScoreHorizontal(matrix, i, owner) >= 100000 || getScoreVertical(matrix, i, owner) >= 100000
                    || getScoreDiag1(matrix, i, owner) >= 100000 || getScoreDiag2(matrix, i, owner) >= 100000) {
                value = 1000000;
                bestmove = i;
                //System.out.println("Gewinnspalte KI: "+i);
                return true;
            }
        }
        return false;
    }

    /*
     * Prüft ob der Gegner dieses Knotens gewinnen kann.
     *
     * @param matrix Matrix des Knotens
     * @param enemy Gegner des Knotens
     * @return true=Gegner kann mit diesem Spielzug gewinnen
     */
    private boolean checkEnemyWin(int[][] matrix, int enemy) {
        for (int i : validemoves) {
            if (getScoreHorizontal(matrix, i, enemy) >= 100000 || getScoreVertical(matrix, i, enemy) >= 100000
                    || getScoreDiag1(matrix, i, enemy) >= 100000 || getScoreDiag2(matrix, i, enemy) >= 100000) {
                value = 1000000;
                bestmove = i;
                //System.out.println("Gewinnspalte Spieler: "+i);
                return true;
            }
        }
        return false;
    }

    /*
     * Überprüft alle noch möglichen Züge für die KI
     *
     * @param matrix Matrix für welche die möglichen Züge berechnet werden
     * sollen
     * @return Array mit allen möglichen Zügen
     */
    private int[] checkValideMoves(int[][] matrix) {
        //ANZAHL gültiger züge ermittlen
        int counter = 0;
        for (int i = 0; i < 7; i++) {
            if (validateMove(matrix, i)) {
                counter++;
            }
        }

        int[] tempValidemoves = new int[counter];
        int arraypos = 0;
        for (int i = 0; i < 7; i++) {
            if (validateMove(matrix, i)) {
                tempValidemoves[arraypos] = i;
                arraypos++;
            }
        }
        return tempValidemoves;
    }

    /*
     * Überprüft ob der SPielzug gültig ist
     * 
     * @param matrix
     * @param rowToInsert
     * @return 
     */
    private boolean validateMove(int[][] matrix, int rowToInsert) {
        return getRowToInsert(matrix, rowToInsert) > 0;
    }

    /*
     * Fügt einen virtuellen Stein ein
     *
     * @param matrix matrix in welche der Stein eingefügt werden soll
     * @param column Spalte in welche der Stein eingefügt werden soll
     * @return Neue "virtuelle" Matrix
     */
    private int[][] insertMove(int[][] matrix, int column) {
        //System.out.println("Füge einen Stein in Spalte " + (column+1) + " ein");

        int rowToInsert = getRowToInsert(matrix, column);

        matrix[rowToInsert][column] = owner;

        return matrix;
    }

    /*
     * Ermittelt Zeile in welcher der Stein zu liegen kommt
     *
     * @param matrix 
     * @param column
     * @return 
     */
    private int getRowToInsert(int[][] matrix, int column) {
        //Lokale Variable um die Einzufügende Zeile zu ermitteln (Initialisiert auf die unterste Reihe)
        int rowToInsert = 5;

        //Ermittelt die einzufügende Zeile (die unterste Reihe welche nicht belegt ist)
        for (int i = 5; i >= 0; i--) {
            if (matrix[i][column] != 0) {
                rowToInsert = i - 1;
            }
        }
        return rowToInsert;
    }

    /*
     * Ermittlet den Feldwert Horizontal anhand der bereits liegenden Spielsteine
     * 
     * @param matrix
     * @param spalte
     * @param playerid
     * @return 
     */
    private int getScoreHorizontal(int[][] matrix, int spalte, int playerid) {
        int tempScore = 0;
        int actualrow = getRowToInsert(matrix, spalte);
        // nach rechts suchen
        for (int i = spalte + 1; i < 6; i++) {
            if (matrix[actualrow][i] == playerid) {
                tempScore += 1;
            } else {
                break;
            }
        }
        // nach links suchen
        for (int i = spalte - 1; i >= 0; i--) {
            if (matrix[actualrow][i] == playerid) {
                tempScore += 1;
            } else {
                break;
            }
        }

        return calcScore(tempScore);
    }

    /*
     * Ermittlet den Feldwert Vertikal anhand der bereits liegenden Spielsteine
     * 
     * @param matrix
     * @param spalte
     * @param playerid
     * @return 
     */
    private int getScoreVertical(int[][] matrix, int spalte, int playerid) {
        int tempScore = 0;

        // nach unten suchen
        for (int i = getRowToInsert(matrix, spalte) + 1; i <= 5; i++) {
            if (matrix[i][spalte] == playerid) {
                tempScore += 1;
            } else {
                break;
            }
        }

        return calcScore(tempScore);
    }

    /*
     * Ermittlet den Feldwert Diagonal (Links oben nach rechts unten)
     * anhand der bereits liegenden Spielsteine
     * 
     * @param matrix
     * @param spalte
     * @param playerid
     * @return 
     */
    private int getScoreDiag1(int[][] matrix, int spalte, int playerid) {
        int tempScore = 0;
        int actualrow = getRowToInsert(matrix, spalte);

        // nach oben links suchen
        for (int i = 1; i <= (Math.min(0 + spalte, 0 + actualrow)); i++) {
            if (matrix[actualrow - i][spalte - i] == playerid) {
                tempScore += 1;
            } else {
                break;
            }
        }
        // nach unten rechts suchen
        for (int i = 1; i <= Math.min(6 - spalte, 5 - actualrow); i++) {
            if (matrix[actualrow + i][spalte + i] == playerid) {
                tempScore += 1;
            } else {
                break;
            }
        }

        return calcScore(tempScore);
    }

    /*
     * Ermittlet den Feldwert Diagonal (Rechts oben nach links unten)
     * anhand der bereits liegenden Spielsteine
     * 
     * @param matrix
     * @param spalte
     * @param playerid
     * @return 
     */
    private int getScoreDiag2(int[][] matrix, int spalte, int playerid) {
        int tempScore = 0;
        int actualrow = getRowToInsert(matrix, spalte);

        // nach oben rechts suchen
        for (int i = 1; i <= (Math.min(6 - spalte, 0 + actualrow)); i++) {
            if (matrix[actualrow - i][spalte + i] == playerid) {
                tempScore += 1;
            } else {
                break;
            }
        }
        // nach unten links suchen
        for (int i = 1; i <= Math.min(0 + spalte, 5 - actualrow); i++) {
            if (matrix[actualrow + i][spalte - i] == playerid) {
                tempScore += 1;
            } else {
                break;
            }
        }

        return calcScore(tempScore);
    }

    private int calcScore(int connected) {
        switch (connected) {
            case 2:
                return 100;
            case 1:
                return 10;
            case 0:
                return 0;
            default:
                return 100000;
        }
    }

    /*
     * Ermittelt den Gesamtfeldwert eines Spielzugs
     * 
     * @param matrix
     * @param spalte
     * @param owner
     * @return 
     */
    private int getFieldScore(int[][] matrix, int spalte, int owner) {
        int finalScore = 0;
        int enemy = owner == 1 ? 2 : 1;

        //Vor dem Maximierenmieren prüfen auf Sieg
        if (getScoreHorizontal(matrix, spalte, owner) >= 100000 || getScoreVertical(matrix, spalte, owner) >= 100000
                || getScoreDiag1(matrix, spalte, owner) >= 100000 || getScoreDiag2(matrix, spalte, owner) >= 100000) {
            return 1000000;
        }

        // Suche auf der horizontalen Ebene
        finalScore += getScoreHorizontal(matrix, spalte, owner);
        finalScore -= getScoreHorizontal(matrix, spalte, enemy);

        // Suche auf der vertikalen Ebene
        finalScore += getScoreVertical(matrix, spalte, owner);
        finalScore -= getScoreVertical(matrix, spalte, enemy);

        // Diagonale Suche
        finalScore += getScoreDiag1(matrix, spalte, owner);
        finalScore -= getScoreDiag1(matrix, spalte, enemy);
        finalScore += getScoreDiag2(matrix, spalte, owner);
        finalScore -= getScoreDiag2(matrix, spalte, enemy);

        return finalScore;
    }

    /*
     * Ermittlet welcher Wert an die Nächst höhere Ebene gegeben werden soll
     * (MIN/MAX)
     * 
     * @param matrix
     * @param spalte
     * @param owner
     * @return 
     */
    private int calcValue(int[][] matrix, int spalte, int owner) {
        int minvalue = 0;
        int maxvalue = 0;

        //Wenn nicht auf der Untersten EBENE suche den Wert der Childnodes
        if (knotentiefe <= baumhoehe) {

            //Wenn Maximierungsschicht hole minvalue von Childnodes
            if (knotentiefe % 2 == 0) {
                for (GameTreeNode n : childnodes) {
                    if (n.getValue() < minvalue) {
                        minvalue = n.getValue();
                        this.bestmove = n.getMoveNr();
                    }

                }
                return minvalue;
            } //Wenn Minimierungsfeld hole maxvalue von Childnodes
            else {
                for (GameTreeNode n : childnodes) {
                    if (n.getValue() > maxvalue) {
                        maxvalue = n.getValue();
                        this.bestmove = n.getBestMove();
                    }

                }
                return maxvalue;
            }
        } //Wenn auf der Untersten Ebene. Bewerte Spielfeld
        else {
            if (knotentiefe % 2 == 0) {
                return getFieldScore(matrix, spalte, owner);
            } else {
                return getFieldScore(matrix, spalte, enemy);
            }

        }
    }
}
