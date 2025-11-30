

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

public class BoxChecker3 implements Runnable {

    private final int[][] board;
    private final int boxIndex;
    private final ValidationResult result;

    public BoxChecker3(int[][] board, int boxIndex, ValidationResult result) {
        this.board = board;
        this.boxIndex = boxIndex;
        this.result = result;
    }

    @Override
    public void run() {
        int startRow = (boxIndex / 3) * 3;
        int startCol = (boxIndex % 3) * 3;

        int[] box = new int[9];
        int idx = 0;

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                box[idx++] = board[startRow + r][startCol + c];
            }
        }

        int[] count = new int[10];
        for (int v : box) {
            if (v >= 1 && v <= 9) count[v]++;
        }

        for (int d = 1; d <= 9; d++) {
            if (count[d] > 1) {
                List<Integer> pos = new ArrayList<>();
                idx = 0;
                for (int r = 0; r < 3; r++) {
                    for (int c = 0; c < 3; c++) {
                        if (board[startRow + r][startCol + c] == d)
                            pos.add(idx + 1);
                        idx++;
                    }
                }

                result.addError(String.format(
                        "BOX %d,#%d,%s",
                        boxIndex + 1, d, pos.toString()
                ));
            }
        }
    }
}
