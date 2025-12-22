/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package validation;

import model.*;

/**
 *
 * @author DANAH
 */
public class SudokuVerifier {
    public String verify(int[][] board) {
        StringBuilder duplicates = new StringBuilder();
        boolean hasZeros = false;
        
      for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                int val = board[r][c];
                if (val == 0) {
                    hasZeros = true; 
                    } else if (!isSafe(board, r, c, val)) {
                    duplicates.append(r).append(",").append(c).append(" ");
                }
            }
        }
        if (duplicates.length() > 0) return duplicates.toString().trim();
        return hasZeros ? "INCOMPLETE" : "VALID";
    }
    
public boolean isSafe(int[][] board, int row, int col, int num) {
        for (int i = 0; i < 9; i++) {
            if (i != col && board[row][i] == num) return false;
            if (i != row && board[i][col] == num) return false;
        }
        int startRow = (row / 3) * 3, startCol = (col / 3) * 3;
        for (int r = startRow; r < startRow + 3; r++) {
            for (int c = startCol; c < startCol + 3; c++) {
                if ((r != row || c != col) && board[r][c] == num) return false;
            }
        }
        return true;
    }
}
                
             