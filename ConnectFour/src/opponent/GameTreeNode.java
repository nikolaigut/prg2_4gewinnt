package opponent;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Der Knoten eines GameTrees.
 *
 * @author A. Morard
 */
public class GameTreeNode implements Serializable {
    private final int owner;
    private final int enemy;
    private final int[][] currentmodel;
    private final int movenr;
    private int bestmove = 3;
    private int value;
    private int knotentiefe = 0;  //ungerad maximierung  gerade minimierung
    private final int baumhoehe;
    private final int[] validemoves;
    private ArrayList<GameTreeNode> childnodes;  //max 7  0=Leaf
    private final GameTreeNode parentnode;

    /**
     * Erstellt einen neuen Knoten für den GameTree.
     *
     * @param parent Elternknoten
     * @param currentmodel aktuelles Model welches der Knoten enthalten soll
     * @param baumhoehe gesamte Baumhöhe
     * @param movenr welcher Spielzug der Knoten repräsentiert (1-7)
     * @param owner wem der Knoten gehört
     */
    public GameTreeNode(
            final GameTreeNode parent,
            final int[][] currentmodel,
            final int baumhoehe,
            final int movenr,
            final int owner) {
        this.enemy = owner == 1 ? 2 : 1;
        this.owner = owner;
        this.movenr = movenr;
        this.currentmodel = currentmodel;
        this.parentnode = parent;
        this.baumhoehe = baumhoehe;
        knotentiefe++;
        this.validemoves = checkValideMoves(currentmodel);

        if (checkOwnWin(currentmodel, owner)) {
            //Computer kann gewinnen.
        } else if (checkEnemyWin(currentmodel, enemy)) {
            //Spieler kann gewinnen.
        } else {
            if (knotentiefe <= baumhoehe) {
                childnodes = new ArrayList<>(7);

                for (int i : validemoves) {
                    int[][] newmodel = new int[6][7];

                    for (int row = 0; row < 6; row++) {
                        for (int column = 0; column < 7; column++) {
                            newmodel[row][column] = currentmodel[row][column];
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
            this.value = calcValue(currentmodel, movenr, owner);
        }
    }

    /**
    * Getter-Methode für den Value.
    *
    * @return der value
    */
    public final int getValue() {
        return value;
    }

    /**
    * Getter-Methode für die Zugnummer.
    *
    * @return die zugnummer
    */
    public final int getMoveNr() {
        return movenr;
    }

    /**
    * Getter-Methode für den besten Zug.
    *
    * @return Spaltennummer für den besten Zug
    */
    public final int getBestMove() {
        return bestmove;
    }

    /**
     * Prüft ob der Besitzer des Knotens gewinnen kann.
     *
     * @param matrix Matrix des Knotens
     * @param own Besitzer des Knotens
     * @return true=Besitzern kann mit diesem Spielzug gewinnen
     */
    private boolean checkOwnWin(final int[][] matrix, final int own) {

        for (int i : validemoves) {
            if (getScoreHorizontal(matrix, i, own) >= 100000 || getScoreVertical(matrix, i, own) >= 100000
                    || getScoreDiag1(matrix, i, own) >= 100000 || getScoreDiag2(matrix, i, own) >= 100000) {
                value = 1000000;
                bestmove = i;
                //System.out.println("Gewinnspalte KI: "+i);
                return true;
            }
        }
        return false;
    }

    /**
     * Prüft ob der Gegner des Knotens gewinnen kann.
     *
     * @param matrix Matrix des Knotens
     * @param opponent Gegner des Knotens
     * @return Wenn true kann der Gegner kann mit diesem Spielzug gewinnen
     */
    private boolean checkEnemyWin(final int[][] matrix, final int opponent) {
        for (int i : validemoves) {
            if (getScoreHorizontal(matrix, i, opponent) >= 100000 || getScoreVertical(matrix, i, opponent) >= 100000
                    || getScoreDiag1(matrix, i, opponent) >= 100000 || getScoreDiag2(matrix, i, opponent) >= 100000) {
                value = 1000000;
                bestmove = i;
                //System.out.println("Gewinnspalte Spieler: "+i);
                return true;
            }
        }
        return false;
    }

    /**
     * Überprüft alle noch möglichen Züge für den Computergegner.
     *
     * @param matrix die Matrix für welche die möglichen Züge berechnet werden sollen
     * @return Array mit allen möglichen Zügen
     */
    private int[] checkValideMoves(final int[][] matrix) {
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

    /**
     * Überprüft ob der Spielzug gültig ist.
     *
     * @param matrix die Matrix für den zu validierenden Zug
     * @param rowToInsert die Reihe in der der Zug gemacht wird
     * @return Wahrheitswert ob Zug valide ist
     */
    private boolean validateMove(final int[][] matrix, final int rowToInsert) {
        return getRowToInsert(matrix, rowToInsert) > 0;
    }

    /**
     * Macht einen temporären Zug zur Simulation.
     *
     * @param matrix die Matrix in welche der Stein eingefügt werden soll
     * @param column die Spalte in welche der Stein eingefügt werden soll
     * @return die neue simulierte Matrix
     */
    private int[][] insertMove(final int[][] matrix, final int column) {
        int rowToInsert = getRowToInsert(matrix, column);
        matrix[rowToInsert][column] = owner;
        return matrix;
    }

    /**
     * Ermittelt Zeile in welcher der Stein zu liegen kommt.
     *
     * @param matrix die Matrix in welche der Stein eingefügt werden soll
     * @param column die Spalte in welche der Stein eingefügt werden soll
     * @return die Zeile des einzusetzenden Steines
     */
    private int getRowToInsert(final int[][] matrix, final int column) {
        int rowToInsert = 5;
        for (int i = 5; i >= 0; i--) {
            if (matrix[i][column] != 0) {
                rowToInsert = i - 1;
            }
        }
        return rowToInsert;
    }

    /**
     * Ermittelt den Feldwert Horizontal anhand der bereits liegenden Spielsteine.
     *
     * @param matrix die Matrix in der der Feldwert berechnet werden soll
     * @param column die Spalte in der der Feldwert berechnet werden soll
     * @param playerid die ID des Spielers
     * @return der Feldwert
     */
    private int getScoreHorizontal(final int[][] matrix, final int column, final int playerid) {
        int tempScore = 0;
        int actualrow = getRowToInsert(matrix, column);
        for (int i = column + 1; i < 6; i++) {
            if (matrix[actualrow][i] == playerid) {
                tempScore += 1;
            } else {
                break;
            }
        }
        for (int i = column - 1; i >= 0; i--) {
            if (matrix[actualrow][i] == playerid) {
                tempScore += 1;
            } else {
                break;
            }
        }
        return calcScore(tempScore);
    }

    /**
     * Ermittlet den Feldwert Vertikal anhand der bereits liegenden Spielsteine.
     *
     * @param matrix die Matrix in der der Feldwert berechnet werden soll
     * @param column die Spalte in der der Feldwert berechnet werden soll
     * @param playerid die ID des Spielers
     * @return der Feldwert
     */
    private int getScoreVertical(final int[][] matrix, final int column, final int playerid) {
        int tempScore = 0;
        for (int i = getRowToInsert(matrix, column) + 1; i <= 5; i++) {
            if (matrix[i][column] == playerid) {
                tempScore += 1;
            } else {
                break;
            }
        }
        return calcScore(tempScore);
    }

    /**
     * Ermittlet den Feldwert Diagonal (Links oben nach rechts unten)
     * anhand der bereits liegenden Spielsteine.
     *
     * @param matrix die Matrix in der der Feldwert berechnet werden soll
     * @param column die Spalte in der der Feldwert berechnet werden soll
     * @param playerid die ID des Spielers
     * @return der Feldwert
     */
    private int getScoreDiag1(final int[][] matrix, final int column, final int playerid) {
        int tempScore = 0;
        int actualrow = getRowToInsert(matrix, column);
        for (int i = 1; i <= (Math.min(0 + column, 0 + actualrow)); i++) {
            if (matrix[actualrow - i][column - i] == playerid) {
                tempScore += 1;
            } else {
                break;
            }
        }
        for (int i = 1; i <= Math.min(6 - column, 5 - actualrow); i++) {
            if (matrix[actualrow + i][column + i] == playerid) {
                tempScore += 1;
            } else {
                break;
            }
        }
        return calcScore(tempScore);
    }

    /**
     * Ermittlet den Feldwert Diagonal (Rechts oben nach links unten)
     * anhand der bereits liegenden Spielsteine.
     *
     * @param matrix die Matrix in der der Feldwert berechnet werden soll
     * @param column die Spalte in der der Feldwert berechnet werden soll
     * @param playerid die ID des Spielers
     * @return der Feldwert
     */
    private int getScoreDiag2(final int[][] matrix, final int column, final int playerid) {
        int tempScore = 0;
        int actualrow = getRowToInsert(matrix, column);
        for (int i = 1; i <= (Math.min(6 - column, 0 + actualrow)); i++) {
            if (matrix[actualrow - i][column + i] == playerid) {
                tempScore += 1;
            } else {
                break;
            }
        }
        for (int i = 1; i <= Math.min(0 + column, 5 - actualrow); i++) {
            if (matrix[actualrow + i][column - i] == playerid) {
                tempScore += 1;
            } else {
                break;
            }
        }
        return calcScore(tempScore);
    }

    /**
     * Gibt den Feldwert aufgrund des übergebenen Werts zurück.
     *
     * @param connected der zu übergebende Wert
     * @return der Feldwert
     */
    private int calcScore(final int connected) {
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

    /**
     * Ermittelt den Gesamtfeldwert eines Spielzugs.
     *
     * @param matrix die Matrix in der der Feldwert berechnet werden soll
     * @param column die Spalte in der der Feldwert berechnet werden soll
     * @param own der Besitzer für welchen der Feldwert berechnet werden soll
     * @return der Feldwert
     */
    private int getFieldScore(final int[][] matrix, final int column, final int own) {
        int finalScore = 0;
        int enemy = own == 1 ? 2 : 1;

        //Vor dem Maximierenmieren prüfen auf Sieg
        if (getScoreHorizontal(matrix, column, own) >= 100000 || getScoreVertical(matrix, column, own) >= 100000
                || getScoreDiag1(matrix, column, own) >= 100000 || getScoreDiag2(matrix, column, own) >= 100000) {
            return 1000000;
        }

        // Suche auf der horizontalen Ebene
        finalScore += getScoreHorizontal(matrix, column, own);
        finalScore -= getScoreHorizontal(matrix, column, enemy);

        // Suche auf der vertikalen Ebene
        finalScore += getScoreVertical(matrix, column, own);
        finalScore -= getScoreVertical(matrix, column, enemy);

        // Diagonale Suche
        finalScore += getScoreDiag1(matrix, column, own);
        finalScore -= getScoreDiag1(matrix, column, enemy);
        finalScore += getScoreDiag2(matrix, column, own);
        finalScore -= getScoreDiag2(matrix, column, enemy);

        return finalScore;
    }

    /**
     * Ermittlet welcher Wert an die Nächst höhere Ebene gegeben werden soll
     * (MIN/MAX).
     *
     * @param matrix die Matrix in der der Feldwert berechnet werden soll
     * @param column die Spalte in der der Feldwert berechnet werden soll
     * @param own der Besitzer für den der Feldwert berechnet wird
     * @return der ermittelte Wert
     */
    private int calcValue(final int[][] matrix, final int column, final int own) {
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
            } else {
                //Wenn Minimierungsfeld hole maxvalue von Childnodes
                for (GameTreeNode n : childnodes) {
                    if (n.getValue() > maxvalue) {
                        maxvalue = n.getValue();
                        this.bestmove = n.getBestMove();
                    }
                }
                return maxvalue;
            }
        } else {
            //Wenn auf der Untersten Ebene, dann bewerte Spielfeld
            if (knotentiefe % 2 == 0) {
                return getFieldScore(matrix, column, own);
            } else {
                return getFieldScore(matrix, column, enemy);
            }
        }
    }
}
