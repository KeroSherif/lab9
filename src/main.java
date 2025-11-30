/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Main {

    public static void main(String[] args) {

        // ============================
        //  ARGUMENT HANDLING
        // ============================

        // Case 0: No arguments at all
        if (args == null || args.length == 0) {
            System.out.println("No arguments provided!");
            System.out.println("You must provide a CSV file to read.");
            System.out.println("Example: java Main board.csv 0");
            return;
        }

        // Case 1: Only file provided (missing mode)
        if (args.length == 1) {
            System.out.println(" Missing mode argument!");
            System.out.println("You provided a file path: " + args[0]);
            System.out.println("But you must also specify the mode (0, 3, or 27).");
            return;
        }

        // Case 2: Not enough arguments
        if (args.length < 2) {
            System.out.println(" Not enough arguments!");
            System.out.println("Usage: java Main <board.csv> <mode>");
            return;
        }

        // ============================
        //  PARSE ARGUMENTS
        // ============================

        String filePath = args[0];
        int mode;

        try {
            mode = Integer.parseInt(args[1]);
            if (mode != 0 && mode != 3 && mode != 27) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            System.out.println(" Invalid mode value!");
            System.out.println("Mode must be one of: 0, 3, or 27");
            return;
        }

        // ============================
        //  LOAD BOARD
        // ============================

        int[][] board;
        try {
            board = SudokuBoardManager.loadOrCreateBoard(filePath);
        } catch (Exception e) {
            System.out.println(" Failed to load board: " + e.getMessage());
            return;
        }

        // ============================
        //  SELECT VALIDATION MODE
        // ============================

        SudokuValidator validator = switch (mode) {
            case 0 -> new SequentialValidator();
            case 3 -> new Mode3Validator();
            case 27 -> new Mode27Validator();
            default -> null; // already validated, unreachable
        };

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
