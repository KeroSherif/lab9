/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class main{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args == null || args.length < 2) {
            System.out.println("Usage: java -jar <app-name>.jar <filepath.csv> <mode>");
            return;
        }

        String path = args[0];
        int mode;
        try {
            mode = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Mode must be one of 0, 3, or 27");
            return;
        }

        int[][] board;
        try {
            board = readCsvBoard(path);
        } catch (IOException e) {
            System.out.println("Failed to read CSV: " + e.getMessage());
            return;
        }

        SudokuValidator validator;
        switch (mode) {
            case 0:
                validator = new SequentialValidator();
                break;
            case 3:
                validator = new Mode3Validator();
                break;
            case 27:
                validator = new Mode27Validator();
                break;
            default:
                System.out.println("Unsupported mode. Use 0, 3, or 27.");
                return;
        }

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
    
    private static int[][] readCsvBoard(String path) throws IOException {
        int[][] board = new int[9][9];
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            int row = 0;
            while ((line = br.readLine()) != null && row < 9) {
                String[] parts = line.split(",");
                if (parts.length != 9) {
                    throw new IOException("Each row must have 9 values");
                }
                for (int col = 0; col < 9; col++) {
                    board[row][col] = Integer.parseInt(parts[col].trim());
                }
                row++;
            }
            if (row != 9) {
                throw new IOException("CSV must contain 9 rows");
            }
        }
        return board;
    }
    
}
