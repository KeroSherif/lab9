/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package checkers;

import core.ValidationResult;
/**
 *
 *  @author kiro sherif
 */
import java.util.*;


public class ColumnChecker implements Runnable {
    private final int[][] board;
    private final int colIndex;
    private final ValidationResult result;

    public ColumnChecker(int[][] board, int colIndex, ValidationResult result) {
        this.board = board;
        this.colIndex = colIndex;
        this.result = result;
    }

    @Override
    public void run() {
        int[] column = new int[9];
        for (int row = 0; row < 9; row++) {
            column[row] = board[row][colIndex];
        }
        List<Integer> duplicates = findDuplicates(column);
        for (int digit : duplicates) {
            List<Integer> positions = new ArrayList<>();
            for (int row = 0; row < 9; row++) {
                if (board[row][colIndex] == digit) {
                    positions.add(row + 1); // 1-based
                }
            }
            String error = String.format("COL %d,#%d,[%s]",
                colIndex + 1, digit, listToString(positions));
            result.addError(error);
        }
    }

    private List<Integer> findDuplicates(int[] arr) {
        int[] count = new int[10];
        List<Integer> duplicates = new ArrayList<>();
        for (int val : arr) {
            if (val >= 1 && val <= 9) {
                count[val]++;
            }
        }
        for (int d = 1; d <= 9; d++) {
            if (count[d] > 1) {
                duplicates.add(d);
            }
        }
        return duplicates;
    }

    private String listToString(List<Integer> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(list.get(i));
        }
        return sb.toString();
    }
}