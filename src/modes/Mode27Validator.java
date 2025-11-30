/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 *  @author kiro sherif
 */
package modes;

import core.SudokuValidator;
import core.ValidationResult;

import checkers.RowChecker;
import checkers.ColumnChecker;
import checkers.BoxChecker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;


public class Mode27Validator implements SudokuValidator {

    @Override
    public ValidationResult validate(int[][] board) {

        System.out.println(">>> Running MODE 27 (27 Threads)");

        List<String> errors = new ArrayList<>();
        ValidationResult result = new ValidationResult(true, errors);
        CountDownLatch latch = new CountDownLatch(27);

        // rows
        for (int i = 0; i < 9; i++) {
            int row = i;
            new Thread(() -> {
                new RowChecker(board, row, result).run();
                latch.countDown();
            }).start();
        }

        // columns
        for (int j = 0; j < 9; j++) {
            int col = j;
            new Thread(() -> {
                new ColumnChecker(board, col, result).run();
                latch.countDown();
            }).start();
        }

        // boxes
        for (int b = 0; b < 9; b++) {
            int box = b;
            new Thread(() -> {
                new BoxChecker(board, box, result).run();
                latch.countDown();
            }).start();
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (!errors.isEmpty())
            return new ValidationResult(false, errors);

        return result;
    }
}
