/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

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

        // If exactly 5 empty cells, use the optimized permutation-based solver
        if (emptyCount == 5) {
            // Create a properly-sized array for FlyweightVerifier
            int[][] emptyForVerifier = new int[5][2];
            for (int i = 0; i < 5; i++) {
                emptyForVerifier[i][0] = empty[i][0];
                emptyForVerifier[i][1] = empty[i][1];
            }
            
            PermutationIterator it = new PermutationIterator(5);
            FlyweightVerifier verifier = new FlyweightVerifier(board, emptyForVerifier);

            while (it.hasNext()) {
                int[] candidate = it.next();
                if (verifier.isValid(candidate))
                    return candidate;
            }
            return null;
        }

        // For other numbers of empty cells, use backtracking
        if (solveBacktrack(board, empty, 0, emptyCount)) {
            int[] solution = new int[emptyCount];
            for (int i = 0; i < emptyCount; i++) {
                solution[i] = board[empty[i][0]][empty[i][1]];
            }
            return solution;
        }
        return null;
    }

    private static boolean solveBacktrack(int[][] board, int[][] empty, int idx, int emptyCount) {
        if (idx == emptyCount) {
            return true;
        }

        int row = empty[idx][0];
        int col = empty[idx][1];

        for (int num = 1; num <= 9; num++) {
            if (isValid(board, row, col, num)) {
                board[row][col] = num;
                if (solveBacktrack(board, empty, idx + 1, emptyCount)) {
                    return true;
                }
                board[row][col] = 0;
            }
        }

        return false;
    }

    private static boolean isValid(int[][] board, int row, int col, int num) {
        // Check row
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num)
                return false;
        }

        // Check column
        for (int i = 0; i < 9; i++) {
            if (board[i][col] == num)
                return false;
        }

        // Check 3x3 box
        int startRow = (row / 3) * 3;
        int startCol = (col / 3) * 3;
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (board[i][j] == num)
                    return false;
            }
        }

        return true;
    }
}
