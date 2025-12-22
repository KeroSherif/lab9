/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package validation;

import model.*;
/**
 *
 * @author User
 */
import java.util.*;


public class BoxChecker implements Runnable {
    private final int[][] board;
    private final int boxIndex;
    private final ValidationResult result;

    public BoxChecker(int[][] board, int boxIndex, ValidationResult result) {
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
        List<Integer> duplicates = findDuplicates(box);
        for (int digit : duplicates) {
            List<Integer> positions = new ArrayList<>();
            idx = 0;
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    if (board[startRow + r][startCol + c] == digit) {
                        // Position in 1D (row-major): from 1 to 9
                        int pos = idx + 1;
                        positions.add(pos);
                    }
                    idx++;
                }
            }
            String error = String.format("BOX %d,#%d,[%s]",
                boxIndex + 1, digit, listToString(positions));
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