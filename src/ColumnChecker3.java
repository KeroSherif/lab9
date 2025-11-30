/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author monic
 */
public class ColumnChecker3 extends Thread {
    private int[][] board;
    private int col;
    private List<String> errors;

    public ColumnChecker3(int[][] board, int col, List<String> errors) {
        this.board = board;
        this.col = col;
        this.errors = errors;
    }

    @Override
    public void run() {
        boolean[] seen = new boolean[9];
        for (int i = 0; i < 9; i++) {
            int num = board[i][col];
            if (num < 1 || num > 9 || seen[num - 1]) {
                errors.add("Column " + (col + 1) + " error at row " + (i + 1));
                return;
            }
            seen[num - 1] = true;
        }
    }
}      
   














































