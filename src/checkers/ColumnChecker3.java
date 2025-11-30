

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

public class ColumnChecker3 implements Runnable {

    private final int[][] board;
    private final int colIndex;
    private final ValidationResult result;

    public ColumnChecker3(int[][] board, int colIndex, ValidationResult result) {
        this.board = board;
        this.colIndex = colIndex;
        this.result = result;
    }

    @Override
    public void run() {
        int[] col = new int[9];
        for (int r = 0; r < 9; r++) col[r] = board[r][colIndex];

        int[] count = new int[10];
        for (int v : col) {
            if (v >= 1 && v <= 9) count[v]++;
        }

        for (int d = 1; d <= 9; d++) {
            if (count[d] > 1) {
                List<Integer> pos = new ArrayList<>();
                for (int r = 0; r < 9; r++) {
                    if (col[r] == d) pos.add(r + 1);
                }
                result.addError(String.format(
                        "COL %d,#%d,%s",
                        colIndex + 1, d, pos.toString()
                ));
            }
        }
    }
}
