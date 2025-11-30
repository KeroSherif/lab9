/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author monic
 */
import java.util.HashSet;
import java.util.Set;

public class RowChecker3 extends Thread {

    private int[][] board;
    private ValidationResult result;

    public RowChecker3(int[][] board) {
        this.board = board;
        this.result = new ValidationResult(true, new java.util.ArrayList<>());
    }

    @Override
    public void run() {
        for (int row = 0; row < 9; row++) {
            Set<Integer> seen = new HashSet<>();

            for (int col = 0; col < 9; col++) {
                int value = board[row][col];

                if (seen.contains(value)) {
                    result.addError("Duplicate value " + value + " found in ROW " + row +
                            " at column " + col);
                    result = new ValidationResult(false, result.getErrors());
                } else {
                    seen.add(value);
                }
            }
        }
    }

    public ValidationResult getResult() {
        return result;
    }
}
                  

















































