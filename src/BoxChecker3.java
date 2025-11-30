
import java.util.List;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author monic
 */
public class BoxChecker3 extends Thread {
    private int[][] board;
    private int rowStart, colStart;
    private List<String> errors;

    public BoxChecker3(int[][] board, int rowStart, int colStart, List<String> errors) {
        this.board = board;
        this.rowStart = rowStart;
        this.colStart = colStart;
        this.errors = errors;
    }

    @Override
    public void run() {
        boolean[] seen = new boolean[9];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int num = board[rowStart + i][colStart + j];
                if (num < 1 || num > 9 || seen[num - 1]) {
                    errors.add("Box starting at (" + (rowStart+1) + "," + (colStart+1) + ") has error");
                    return;
                }
                seen[num - 1] = true;
            }
        }
    }
}
                

























