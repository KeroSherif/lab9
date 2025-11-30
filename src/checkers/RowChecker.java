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


public class RowChecker implements Runnable {
    private final int[][] board;
    private final int rowIndex;
    private final ValidationResult result;

    public RowChecker(int[][] board, int rowIndex, ValidationResult result) {
        this.board = board;
        this.rowIndex = rowIndex;
        this.result = result;
    }

    @Override
    public void run() {
        int[] row = board[rowIndex];
        List<Integer> duplicates = findDuplicates(row);
        for (int digit : duplicates) {
            List<Integer> positions = new ArrayList<>();
            for (int col = 0; col < 9; col++) {
                if (row[col] == digit) {
                    positions.add(col + 1); // 1-based index
                }
            }
            String error = String.format("ROW %d,#%d,[%s]",
                rowIndex + 1, digit, listToString(positions));
            result.addError(error);
        }
    }

    private List<Integer> findDuplicates(int[] arr) {
        int[] count = new int[10]; // digits 1–9
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