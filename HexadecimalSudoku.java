package edu.ics211.h09;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class for recursively finding a solution to a Hexadecimal Sudoku problem.
 *
 * @author Biagioni, Edoardo, Cam Moore
 *     date August 5, 2016
 *     missing solveSudoku, to be implemented by the students in ICS 211
 */
public class HexadecimalSudoku {

    /**
     * My code was tested with the following main method.
     * @param args
     */
    public static void main(String[] args) {
        int[][] example4 = { { 4, -1, -1, 9, -1, 14, -1, 0, -1, -1, -1, 6, -1, -1, -1, -1 },
                { 3, -1, -1, 2, -1, -1, -1, -1, -1, 8, 5, 11, 10, 0, -1, 14 },
                { 13, -1, -1, -1, 10, 2, 8, -1, 1, 12, -1, -1, -1, -1, 9, -1 },
                { 10, 7, -1, -1, 4, -1, 3, 15, -1, -1, -1, -1, -1, 8, -1, 12 },
                { 5, -1, 3, -1, -1, 12, 4, -1, 13, -1, -1, -1, -1, 11, -1, -1 },
                { 14, -1, -1, -1, -1, 0, -1, 13, 15, -1, 9, -1, 6, 3, 8, -1 },
                { 7, 8, -1, 15, -1, 3, 1, 10, 14, -1, -1, 4, -1, 5, -1, -1 },
                { 11, 10, 1, -1, -1, -1, 9, -1, -1, -1, -1, -1, -1, -1, 0, 4 },
                { 9, 3, 13, -1, 7, 8, 15, -1, 6, -1, -1, 0, -1, 14, -1, -1 },
                { 8, -1, 15, 1, -1, -1, -1, -1, 5, -1, -1, 14, 0, 12, 10, -1 },
                { 6, -1, -1, 14, 12, 10, -1, -1, 3, -1, 15, 13, 8, -1, 1, 7 },
                { 0, -1, -1, 7, -1, -1, 2, 1, -1, -1, -1, 8, 15, -1, -1, -1 },
                { 12, 0, 7, -1, 8, -1, 11, -1, 10, -1, 1, -1, 5, -1, -1, -1 },
                { 1, 6, -1, -1, -1, -1, 5, 2, -1, -1, -1, 7, 11, 10, 15, -1 },
                { 2, -1, 14, 5, 13, -1, 10, -1, -1, -1, 4, -1, 9, -1, 7, 8 },
                { 15, -1, 9, 10, -1, 1, -1, -1, -1, 2, -1, -1, -1, 6, 4, -1 } };


        System.out.println(toString(example4, false));
        HexadecimalSudoku.solveSudoku(example4);
        System.out.println(toString(example4, false));
    }


    /**
     * Find an assignment of values to sudoku cells that makes the sudoku valid.
     * This algorithsm
     * @param board the sudoku to be solved.
     * @return whether a solution was found if a solution was found, the sudoku is
     *         filled in with the solution if no solution was found, restores the
     *         sudoku to its original value.
     */
    public static boolean solveSudoku(int[][] board) {
        int SIZE = board.length;
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == -1) {
                    ArrayList<Integer> legal = legalValues(board, row, col);
                    for (int n = 0; n < 16; n++) {
                        if (legal.contains(n)) {
                            board[row][col] = n;
                            if (solveSudoku(board)) { //recurse
                                return true;
                            } else {
                                board[row][col] = -1;
                            }
                        }
                    }
                    return false;
                }
            }
        }
        //solved
        return true;
    }

    /**
     * Find the legal values for the given sudoku and cell.
     *
     * @param sudoku the sudoku being solved.
     * @param row the row of the cell to get values for.
     * @param column the column of the cell.
     * @return an ArrayList of the valid values.
     */
    public static ArrayList<Integer> legalValues(int[][] sudoku, int row, int column) {
        int width = sudoku[0].length;
        int height = sudoku.length;
        int numSubGrids = (int) Math.sqrt(width);

        //all of these values are taken
        ArrayList<Integer> illegalValues = new ArrayList<>();

        //first check all values in row
        //skip if same row or unassigned
        for (int x = 0; x < width; x++) {
            if (x == column) continue;
            if (sudoku[row][x] == -1) continue;
            if (!illegalValues.contains(sudoku[row][x])) illegalValues.add(sudoku[row][x]);
        }
        //then check all values in column
        for (int y = 0; y < height; y++) {
            if (y == row) continue;
            if (sudoku[y][column] == -1) continue;
            if (!illegalValues.contains(sudoku[y][column])) illegalValues.add(sudoku[y][column]);
        }
        //then check all values in subgrid
        int[][] d = findSubgrid(sudoku, row, column);
        int subRow = d[0][0];
        int subCol = d[0][1];
        for (int y = 0; y < numSubGrids; y++) {
            for (int x = 0; x < numSubGrids; x++) {
                int r = subRow + y;
                int c = subCol + x;
                if (sudoku[r][c] == -1) {
                    continue;
                }
                if (r == row && c == column) continue;
                if (!illegalValues.contains(sudoku[r][c])) illegalValues.add(sudoku[r][c]);
            }
        }

        //return all values that do not exist in illegal values
        ArrayList<Integer> legalValues = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            if (!illegalValues.contains(i)) {
                legalValues.add(i);
            }
        }
        return legalValues;
    }

    /**
     * Returns true if a cell with a value is allowed in the board.
     * @param sudoku board to be checked.
     * @param row of cell
     * @param column of cell
     * @param v of cell, 0-15
     * @return true if it is legally placed.
     */
    public static boolean isAllowed(int[][] sudoku, int row, int column, int v) {
        return legalValues(sudoku, row, column).contains(v);
    }

    /**
     * Returns the top left corner
     * @param soduko
     * @param row
     * @param column
     * @return
     */
    public static int[][] findSubgrid(int[][] soduko, int row, int column) {
        int width = soduko[0].length;
        int height = soduko.length;
        int numSubGrids = Math.round(width);
        int k = (int) Math.sqrt(numSubGrids);
        int newRow = 0;
        int newColumn = 0;
        //get row
        for (int i = 0; i < numSubGrids; i++) {
            newRow += k;
            if (newRow > row) {
                newRow -= k;
            }
        }
        for (int i = 0; i < numSubGrids; i++) {
            newColumn += k;
            if (newColumn > column) {
                newColumn -= k;
            }
        }
        int[][] d = new int[1][2];
        d[0][0] = newRow;
        d[0][1] = newColumn;
        return d;
    }


    /**
     * checks that the sudoku rules hold in this sudoku puzzle. cells that contain
     * 0 are not checked.
     *
     * @param sudoku the sudoku to be checked.
     * @param printErrors whether to print the error found, if any.
     * @return true if this sudoku obeys all of the sudoku rules, otherwise false.
     */
    public static boolean checkSudoku(int[][] sudoku, boolean printErrors) {
        if (sudoku.length != 16) {
            if (printErrors) {
                System.out.println("sudoku has " + sudoku.length + " rows, should have 16");
            }
            return false;
        }
        for (int i = 0; i < sudoku.length; i++) {
            if (sudoku[i].length != 16) {
                if (printErrors) {
                    System.out.println("sudoku row " + i + " has "
                            + sudoku[i].length + " cells, should have 16");
                }
                return false;
            }
        }
        /* check each cell for conflicts */
        for (int i = 0; i < sudoku.length; i++) {
            for (int j = 0; j < sudoku.length; j++) {
                int cell = sudoku[i][j];
                if (cell == -1) {
                    continue; /* blanks are always OK */
                }
                if ((cell < 0) || (cell > 16)) {
                    if (printErrors) {
                        System.out.println("sudoku row " + i + " column " + j
                                + " has illegal value " + String.format("%02X", cell));
                    }
                    return false;
                }
                /* does it match any other value in the same row? */
                for (int m = 0; m < sudoku.length; m++) {
                    if ((j != m) && (cell == sudoku[i][m])) {
                        if (printErrors) {
                            System.out.println("sudoku row " + i + " has " + String.format("%X", cell)
                                    + " at both positions " + j + " and " + m);
                        }
                        return false;
                    }
                }
                /* does it match any other value it in the same column? */
                for (int k = 0; k < sudoku.length; k++) {
                    if ((i != k) && (cell == sudoku[k][j])) {
                        if (printErrors) {
                            System.out.println("sudoku column " + j + " has " + String.format("%X", cell)
                                    + " at both positions " + i + " and " + k);
                        }
                        return false;
                    }
                }
                /* does it match any other value in the 4x4? */
                for (int k = 0; k < 4; k++) {
                    for (int m = 0; m < 4; m++) {
                        int testRow = (i / 4 * 4) + k; /* test this row */
                        int testCol = (j / 4 * 4) + m; /* test this col */
                        if ((i != testRow) && (j != testCol) && (cell == sudoku[testRow][testCol])) {
                            if (printErrors) {
                                System.out.println("sudoku character " + String.format("%X", cell) + " at row "
                                        + i + ", column " + j + " matches character at row " + testRow + ", column "
                                        + testCol);
                            }
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Returns true if the soduku is filled.
     * @param sudoku
     * @return
     */
    public static boolean isFilled(int[][] sudoku) {
        for (int x = 0; x < sudoku[0].length; x++) {
            for (int y = 0; y < sudoku.length; y++) {
                if (sudoku[x][y] == -1) return false;
            }
        }
        return true;
    }

    /**
     * Creates a copy of our any int[][]
     * @param data
     * @return
     */
    public static int[][] copyOf(int[][] data) {
        int[][] newData = new int[data.length][data[0].length];
        for (int y = 0; y < data[0].length; y++) {
            for (int x = 0; x < data.length; x++) {
                newData[x][y] = data[x][y];
            }
        }
        return newData;
    }


    /**
     * Converts the sudoku to a printable string.
     *
     * @param sudoku the sudoku to be converted.
     * @param debug whether to check for errors.
     * @return the printable version of the sudoku.
     */
    public static String toString(int[][] sudoku, boolean debug) {
        if ((!debug) || (checkSudoku(sudoku, true))) {
            String result = "";
            for (int i = 0; i < sudoku.length; i++) {
                if (i % 4 == 0) {
                    result = result + "+---------+---------+---------+---------+\n";
                }
                for (int j = 0; j < sudoku.length; j++) {
                    if (j % 4 == 0) {
                        result = result + "| ";
                    }
                    if (sudoku[i][j] == -1) {
                        result = result + "  ";
                    } else {
                        result = result + String.format("%X", sudoku[i][j]) + " ";
                    }
                }
                result = result + "|\n";
            }
            result = result + "+---------+---------+---------+---------+\n";
            return result;
        }
        return "illegal sudoku";
    }
}