package modes;

import core.SudokuValidator;
import core.ValidationResult;

import checkers.RowChecker3;
import checkers.ColumnChecker3;
import checkers.BoxChecker3;

import java.util.ArrayList;
import java.util.List;

public class Mode3Validator implements SudokuValidator {

    @Override
    public ValidationResult validate(int[][] board) {

        System.out.println(">>> Running MODE 3 (3 Threads)");

        ValidationResult result = new ValidationResult(true, new ArrayList<>());

        Thread rowThread = new Thread(() -> {
            for (int i = 0; i < 9; i++) {
                new RowChecker3(board, i, result).run();
            }
        });

        Thread colThread = new Thread(() -> {
            for (int j = 0; j < 9; j++) {
                new ColumnChecker3(board, j, result).run();
            }
        });

        Thread boxThread = new Thread(() -> {
            for (int b = 0; b < 9; b++) {
                new BoxChecker3(board, b, result).run();
            }
        });

        // Start Threads
        rowThread.start();
        colThread.start();
        boxThread.start();

        // Wait for them to finish
        try {
            rowThread.join();
            colThread.join();
            boxThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Final decision
        if (!result.getErrors().isEmpty()) {
            return new ValidationResult(false, result.getErrors());
        }

        return result;
    }
}
