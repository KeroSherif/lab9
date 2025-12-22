/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solver;

import model.*;
import exceptions.*;
import validation.FlyweightVerifier;

/**
 *
 * @author kiro sherif
 */
public class SudokuSolver {

    public static int[] solve(int[][] board) {
        // Find all empty cells
        int[][] empty = new int[81][2];
        int emptyCount = 0;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0) {
                    empty[emptyCount][0] = i;
                    empty[emptyCount][1] = j;
                    emptyCount++;
                }
            }
        }

        if (emptyCount != 5) {
            throw new IllegalStateException(
                    "Solver only works with exactly 5 empty cells. Found: " + emptyCount);
        }

        int[][] emptyCells = new int[emptyCount][2];
        for (int i = 0; i < emptyCount; i++) {
            emptyCells[i][0] = empty[i][0];
            emptyCells[i][1] = empty[i][1];
        }

        PermutationIterator iterator = new PermutationIterator(emptyCount);
        FlyweightVerifier verifier = new FlyweightVerifier(board, emptyCells);

        while (iterator.hasNext()) {
            int[] candidate = iterator.next();

            if (verifier.isValid(candidate)) {

                for (int i = 0; i < emptyCount; i++) {
                    board[emptyCells[i][0]][emptyCells[i][1]] = candidate[i];
                }
                return candidate;
            }
        }

        return null;
    }

    // Keep isValid for compatibility, but it's not used in the new permutation-based solver
    private static boolean isValid(int[][] board, int row, int col, int num) {

        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num) {
                return false;
            }
        }

        for (int i = 0; i < 9; i++) {
            if (board[i][col] == num) {
                return false;
            }
        }

       
        int startRow = (row / 3) * 3;
        int startCol = (col / 3) * 3;
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (board[i][j] == num) {
                    return false;
                }
            }
        }

        return true;
    }
}
