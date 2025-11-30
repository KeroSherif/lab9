package modes;

import core.SudokuValidator;
import core.ValidationResult;

import checkers.RowChecker;
import checkers.ColumnChecker;
import checkers.BoxChecker;

import java.util.ArrayList;
import java.util.List;

public class SequentialValidator implements SudokuValidator {

    // Singleton instance
    private static SequentialValidator instance;

    private SequentialValidator() {}

    public static synchronized SequentialValidator getInstance() {
        if (instance == null)
            instance = new SequentialValidator();
        return instance;
    }

    @Override
    public ValidationResult validate(int[][] board) {

        System.out.println(">>> Running MODE 0 (Sequential)");
        System.out.println(">>> Using STANDARD checkers");

        List<String> errors = new ArrayList<>();
        ValidationResult result = new ValidationResult(true);

        // Run row checkers
        for (int r = 0; r < 9; r++) {
            new RowChecker(board, r, result).run();
        }

        // Run column checkers
        for (int c = 0; c < 9; c++) {
            new ColumnChecker(board, c, result).run();
        }

        // Run box checkers
        for (int b = 0; b < 9; b++) {
            new BoxChecker(board, b, result).run();
        }

        // result already holds errors
        return result;
    }
}
