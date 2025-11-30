import core.SudokuValidator;
import core.ValidationResult;
import core.SudokuBoardManager;

import utils.ValidatorFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Sudoku {

    public static void main(String[] args) {

        if (args == null || args.length == 0) {
            System.out.println("No arguments provided!");
            System.out.println("Usage: java Main <board.csv> <mode>");
            return;
        }

        if (args.length == 1) {
            System.out.println("Missing mode argument!");
            System.out.println("Usage: java Main <board.csv> <mode>");
            return;
        }

        String filePath = args[0];
        int mode;

        try {
            mode = Integer.parseInt(args[1]);
            if (mode != 0 && mode != 3 && mode != 27) {
                System.out.println("Invalid mode value! Must be 0, 3, or 27.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid mode! Must be 0, 3, or 27.");
            return;
        }

        int[][] board;

        try {
            board = SudokuBoardManager.loadOrCreateBoard(filePath);
        } catch (Exception e) {
            System.out.println("Failed to load board: " + e.getMessage());
            return;
        }

        // ============================
        //  SHOW USER WHICH MODE IS RUNNING
        // ============================
        switch (mode) {
            case 0 ->
                System.out.println(" Running Sudoku Validator in MODE 0 (Sequential)...");
            case 3 ->
                System.out.println(" Running Sudoku Validator in MODE 3 (3 Threads)...");
            case 27 ->
                System.out.println("Running Sudoku Validator in MODE 27 (27 Threads)...");
        }
        // ============================
        //  SELECT VALIDATION MODE
        // ============================

        SudokuValidator validator;
        try {
            validator = ValidatorFactory.get(mode);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid mode!");
            return;
        }

        // ============================
        //  RUN VALIDATION
        // ============================
        ValidationResult result = validator.validate(board);

        if (result.isValid()) {
            System.out.println("VALID");
        } else {
            System.out.println("INVALID");
            for (String err : result.getErrors()) {
                System.out.println(err);
            }
        }
    }
}
