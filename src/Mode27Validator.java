/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author User
 */
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Mode27Validator implements SudokuValidator {

    @Override
    public ValidationResult validate(int[][] board) {
        List<String> initialErrors = new ArrayList<>();
        ValidationResult result = new ValidationResult(true, initialErrors);

        ExecutorService executor = Executors.newFixedThreadPool(27);

        // 9 Row Checkers
        for (int i = 0; i < 9; i++) {
            executor.submit(new RowChecker(board, i, result));
        }

        // 9 Column Checkers
        for (int i = 0; i < 9; i++) {
            executor.submit(new ColumnChecker(board, i, result));
        }

        // 9 Box Checkers
        for (int i = 0; i < 9; i++) {
            executor.submit(new BoxChecker(board, i, result));
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

        // If any errors were added, mark as invalid
        if (!result.getErrors().isEmpty()) {
            result = new ValidationResult(false, result.getErrors());
        }

        return result;
    }
}
