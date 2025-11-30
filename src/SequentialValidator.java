
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author DANAH
 */


public class SequentialValidator implements SudokuValidator {
    @Override
    public ValidationResult validate(int[][] board) {
        List<String> errors = new ArrayList<>();
        boolean valid = true;

        // Check rows
        for (int row = 0; row < 9; row++) {
            boolean[] seen = new boolean[10]; // 1–9 فقط
            for (int col = 0; col < 9; col++) {
                int num = board[row][col];
                if (num == 0) continue; // تجاهل الخانات الفارغة
                if (num < 1 || num > 9) {
                    valid = false;
                    errors.add("Invalid value " + num + " in ROW " + (row + 1) + " at column " + (col + 1));
                } else if (seen[num]) {
                    valid = false;
                    errors.add("Duplicate " + num + " in ROW " + (row + 1) + " at column " + (col + 1));
                } else {
                    seen[num] = true;
                }
            }
        }

        // Check columns
        for (int col = 0; col < 9; col++) {
            boolean[] seen = new boolean[10];
            for (int row = 0; row < 9; row++) {
                int num = board[row][col];
                if (num == 0) continue;
                if (num < 1 || num > 9) {
                    valid = false;
                    errors.add("Invalid value " + num + " in COLUMN " + (col + 1) + " at row " + (row + 1));
                } else if (seen[num]) {
                    valid = false;
                    errors.add("Duplicate " + num + " in COLUMN " + (col + 1) + " at row " + (row + 1));
                } else {
                    seen[num] = true;
                }
            }
        }

        // Check boxes
        for (int boxRow = 0; boxRow < 3; boxRow++) {
            for (int boxCol = 0; boxCol < 3; boxCol++) {
                boolean[] seen = new boolean[10];
                int startRow = boxRow * 3;
                int startCol = boxCol * 3;
                for (int r = startRow; r < startRow + 3; r++) {
                    for (int c = startCol; c < startCol + 3; c++) {
                        int num = board[r][c];
                        if (num == 0) continue;
                        if (num < 1 || num > 9) {
                            valid = false;
                            errors.add("Invalid value " + num + " in BOX (" + (boxRow + 1) + "," + (boxCol + 1) + ") at cell (" + (r + 1) + "," + (c + 1) + ")");
                        } else if (seen[num]) {
                            valid = false;
                            errors.add("Duplicate " + num + " in BOX (" + (boxRow + 1) + "," + (boxCol + 1) + ") at cell (" + (r + 1) + "," + (c + 1) + ")");
                        } else {
                            seen[num] = true;
                        }
                    }
                }
            }
        }

        return new ValidationResult(valid, errors);
    }
}