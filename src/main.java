/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Main {

    public static void main(String[] args) {

        if (args == null || args.length < 2) {
            System.out.println("Usage: java Main <board.csv> <mode>");
            System.out.println("Mode: 0 (sequential), 3 (3 threads), 27 (27 threads)");
            return;
        }

        String path = args[0];
        int mode;

        try {
            mode = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Mode must be 0, 3, or 27");
            return;
        }

        int[][] board;
        try {
            board = SudokuBoardManager.loadOrCreateBoard(path);
        } catch (Exception e) {
            System.out.println("Failed to load board: " + e.getMessage());
            return;
        }

        SudokuValidator validator = switch (mode) {
            case 0 -> new SequentialValidator();
            case 3 -> new Mode3Validator();
            case 27 -> new Mode27Validator();
            default -> {
                System.out.println("Mode must be 0, 3, or 27");
                yield null;
            }
        };

        if (validator == null) return;

        ValidationResult result = validator.validate(board);

        if (result.isValid()) {
            System.out.println("VALID");
        } else {
            System.out.println("INVALID");
            for (String e : result.getErrors()) {
                System.out.println(e);
            }
        }
    }
}
