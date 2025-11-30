package modes;

import core.SudokuValidator;
import core.ValidationResult;

import checkers.RowChecker3;
import checkers.ColumnChecker3;
import checkers.BoxChecker3;

public class Mode3Validator implements SudokuValidator {

    
    //   SINGLETON INSTANCE
    private static Mode3Validator instance;

    private Mode3Validator() {}   

    public static synchronized Mode3Validator getInstance() {
        if (instance == null)
            instance = new Mode3Validator();
        return instance;
    }

    
    //   MAIN VALIDATION LOGIC
   
    @Override
    public ValidationResult validate(int[][] board) {

        System.out.println(">>> Running MODE 3 (3 Threads)");
        System.out.println(">>> Using MODE-3 checkers");

        ValidationResult result = new ValidationResult(true);

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

        // Wait for completion
        try {
            rowThread.join();
            colThread.join();
            boxThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return result;
    }
}
