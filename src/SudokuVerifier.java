/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab9;

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
                    hasZeros = true; // State: INCOMPLETE [cite: 34]
                    continue;
                } 
                
             if (!isPlacementValid(board, r, c, val)) {
                    duplicates.append(r).append(",").append(c).append(" "); // State: INVALID [cite: 34]
                }
            }
        }
      if (duplicates.length() > 0) return duplicates.toString().trim();
        return hasZeros ? "INCOMPLETE" : "VALID";
    }
}
