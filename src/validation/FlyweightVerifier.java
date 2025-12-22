/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package validation;

import model.*;
/**
 *
 * @author kiro sherif
 */
public class FlyweightVerifier {

    private final int[][] board;
    private final int[][] emptyCells;

    public FlyweightVerifier(int[][] board, int[][] emptyCells) {
        this.board = board;
        this.emptyCells = emptyCells;
    }

    public boolean isValid(int[] values) {

        for (int i = 0; i < values.length; i++) {
            int r = emptyCells[i][0];
            int c = emptyCells[i][1];
            int val = values[i];

            if (!checkRow(r, c, val)) return false;
            if (!checkCol(r, c, val)) return false;
            if (!checkBox(r, c, val)) return false;
        }
        return true;
    }

    private boolean checkRow(int r, int c, int val) {
        for (int j = 0; j < 9; j++) {
            if (j == c) continue;
            if (board[r][j] == val) return false;
        }
        return true;
    }

    private boolean checkCol(int r, int c, int val) {
        for (int i = 0; i < 9; i++) {
            if (i == r) continue;
            if (board[i][c] == val) return false;
        }
        return true;
    }

    private boolean checkBox(int r, int c, int val) {
        int br = (r / 3) * 3;
        int bc = (c / 3) * 3;

        for (int i = br; i < br + 3; i++) {
            for (int j = bc; j < bc + 3; j++) {
                if (i == r && j == c) continue;
                if (board[i][j] == val) return false;
            }
        }
        return true;
    }
}

