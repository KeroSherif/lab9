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

import java.util.concurrent.CountDownLatch;

public class Mode27Validator implements SudokuValidator {

    private static Mode27Validator instance;

    private Mode27Validator() {}

    public static synchronized Mode27Validator getInstance() {
        if (instance == null)
            instance = new Mode27Validator();
        return instance;
    }

    @Override
    public ValidationResult validate(int[][] board) {

        System.out.println(">>> Running MODE 27 (27 Threads)");
        System.out.println(">>> Using STANDARD checkers");

        ValidationResult result = new ValidationResult(true);

        CountDownLatch latch = new CountDownLatch(27);

        // 9 row threads
        for (int i = 0; i < 9; i++) {
            final int r = i;
            new Thread(() -> {
                new RowChecker(board, r, result).run();
                latch.countDown();
            }).start();
        }

        // 9 column threads
        for (int j = 0; j < 9; j++) {
            final int c = j;
            new Thread(() -> {
                new ColumnChecker(board, c, result).run();
                latch.countDown();
            }).start();
        }

        // 9 box threads
        for (int b = 0; b < 9; b++) {
            final int box = b;
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

        return result;
    }
}
