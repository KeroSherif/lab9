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
interface Validator {
    void validate();
    boolean isValid();
    void printReport();
}

public class SequentialValidator implements Validator {

    private final int[][] board;       
    private final List<String> errors; 
    private boolean valid = true;     

    public SequentialValidator(int[][] board) {
        this.board = board;
        this.errors = new ArrayList<>();
    }
    
    @Override
    public void validate() {
        checkRows();
        checkColumns();
        checkBoxes();
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public void printReport() {
        if (valid) {
            System.out.println("VALID");
        } else {
            System.out.println("INVALID");
            System.out.println("Details:");
            errors.forEach(System.out::println);
        }
    }

private void checkRows() {
        for (int row = 0; row < 9; row++) {
            boolean[] seen = new boolean[10]; // digits 1–9

            for (int col = 0; col < 9; col++) {
                int num = board[row][col];

                if (seen[num]) {
                    valid = false;
                    errors.add(
                        "Duplicate number " + num +
                        " found in ROW " + (row + 1) +
                        " at column " + (col + 1)
                    );
                }

                seen[num] = true;
            }
        }
    }

private void checkColumns() {
        for (int col = 0; col < 9; col++) {
            boolean[] seen = new boolean[10];

            for (int row = 0; row < 9; row++) {
                int num = board[row][col];

                if (seen[num]) {
                    valid = false;
                    errors.add(
                        "Duplicate number " + num +
                        " found in COLUMN " + (col + 1) +
                        " at row " + (row + 1)
                    );
                }

                seen[num] = true;
            }
        }
    }

 private void checkBoxes() {
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
                            errors.add(
                                "Duplicate number " + num +
                                " found in BOX (" + (boxRow + 1) + "," + (boxCol + 1) + ")" +
                                " at cell (" + (r + 1) + "," + (c + 1) + ")"
                            );
                        }

                        seen[num] = true;
                    }
                }
            }
        }
    }
}

   