
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

        for (int row = 0; row < 9; row++) {
            boolean[] seen = new boolean[10];

            for (int col = 0; col < 9; col++) {
                int num = board[row][col];

                if (seen[num]) {
                    valid = false;
                    errors.add("Duplicate " + num +
                               " in ROW " + (row + 1) +
                               " at column " + (col + 1));
                }

                seen[num] = true;
            }
        }

        for (int col = 0; col < 9; col++) {
            boolean[] seen = new boolean[10];

            for (int row = 0; row < 9; row++) {
                int num = board[row][col];

                if (seen[num]) {
                    valid = false;
                    errors.add("Duplicate " + num +
                               " in COLUMN " + (col + 1) +
                               " at row " + (row + 1));
                }

                seen[num] = true;
            }
        }

        for (int boxRow = 0; boxRow < 3; boxRow++) {
            for (int boxCol = 0; boxCol < 3; boxCol++) {

                boolean[] seen = new boolean[10];
                int startRow = boxRow * 3;
                int startCol = boxCol * 3;

                for (int r = startRow; r < startRow + 3; r++) {
                    for (int c = startCol; c < startCol + 3; c++) {

                        int num = board[r][c];

                        if (seen[num]) {
                            valid = false;
                            errors.add("Duplicate " + num +
                                       " in BOX (" + (boxRow + 1) + "," + (boxCol + 1) + ")" +
                                       " at cell (" + (r + 1) + "," + (c + 1) + ")");
                        }

                        seen[num] = true;
                    }
                }
            }
        }

        return new ValidationResult(valid, errors);
    }
}
