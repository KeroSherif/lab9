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
        // Make a deep copy to avoid modifying the original
        int[][] boardCopy = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                boardCopy[i][j] = board[i][j];
            }
        }

        // Find all empty cells
        int[][] empty = new int[81][2];
        int emptyCount = 0;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (boardCopy[i][j] == 0) {
                    empty[emptyCount][0] = i;
                    empty[emptyCount][1] = j;
                    emptyCount++;
                }
            }
        }

        // Validate empty cells
        for (int i = 0; i < emptyCount; i++) {
            if (empty[i][0] < 0 || empty[i][0] >= 9 || empty[i][1] < 0 || empty[i][1] >= 9) {
                throw new IllegalStateException("Invalid empty cell coordinates");
            }
        }

        // Use backtracking for all cases
        if (solveBacktrack(boardCopy, empty, 0, emptyCount)) {
            // Copy solved board back to original
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    board[i][j] = boardCopy[i][j];
                }
            }
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

        if (idx >= empty.length || idx < 0) {
            throw new IllegalStateException("Index out of bounds: idx=" + idx + ", length=" + emptyCount);
        }

        int row = empty[idx][0];
        int col = empty[idx][1];

        if (row < 0 || row >= 9 || col < 0 || col >= 9) {
            throw new IllegalStateException("Invalid cell coordinates: row=" + row + ", col=" + col);
        }

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
