/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package checkers;

import core.ValidationResult;
/**
 *
 * @author monic
 */
import java.util.ArrayList;
import java.util.List;

public class RowChecker3 implements Runnable {

    private final int[][] board;
    private final int rowIndex;
    private final ValidationResult result;

    public RowChecker3(int[][] board, int rowIndex, ValidationResult result) {
        this.board = board;
        this.rowIndex = rowIndex;
        this.result = result;
    }

    @Override
    public void run() {
        int[] row = board[rowIndex];

        int[] count = new int[10];
        for (int v : row) {
            if (v >= 1 && v <= 9) count[v]++;
        }

        for (int d = 1; d <= 9; d++) {
            if (count[d] > 1) {
                List<Integer> pos = new ArrayList<>();
                for (int c = 0; c < 9; c++) {
                    if (row[c] == d) pos.add(c + 1);
                }
                result.addError(String.format(
                        "ROW %d,#%d,%s",
                        rowIndex + 1, d, pos.toString()
                ));
            }
        }
    }
}
