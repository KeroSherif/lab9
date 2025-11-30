package core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class SudokuBoardManager {

    public static int[][] loadOrCreateBoard(String filePath) throws Exception {

        List<int[]> rows = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();

                // Skip empty lines (allowed)
                if (line.isEmpty()) continue;

                // ================ VALIDATION 1: No double commas ,, ================
                if (line.contains(",,")) {
                    throw new IllegalArgumentException(
                        "Invalid format at line " + lineNumber + ": Found consecutive commas (,,)"
                    );
                }

                // Split
                String[] parts = line.split(",");

                // ================ VALIDATION 2: Must be exactly 9 columns ================
                if (parts.length != 9) {
                    throw new IllegalArgumentException(
                        "Invalid column count at line " + lineNumber +
                        ". Expected 9 columns, found: " + parts.length
                    );
                }

                int[] row = new int[9];

                for (int i = 0; i < 9; i++) {
                    String token = parts[i].trim();

                    // ============ VALIDATION 3: no empty values ============
                    if (token.isEmpty()) {
                        throw new IllegalArgumentException(
                                "Empty cell found at row " + lineNumber + ", column " + (i + 1)
                        );
                    }

                    // ============ VALIDATION 4: must be numeric 1–9 ============
                    int value;
                    try {
                        value = Integer.parseInt(token);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException(
                            "Non-numeric value '" + token + "' at row " + lineNumber +
                                    ", column " + (i + 1)
                        );
                    }

                    if (value < 1 || value > 9) {
                        throw new IllegalArgumentException(
                            "Invalid number " + value + " at row " + lineNumber +
                            ", column " + (i + 1) + " — Sudoku values must be 1–9 only."
                        );
                    }

                    row[i] = value;
                }

                rows.add(row);
            }

        } catch (IOException e) {
            throw new Exception("Failed to load file: " + e.getMessage());
        }

        // ================ VALIDATION 5: Must be exactly 9 rows ================
        if (rows.size() != 9) {
            throw new IllegalArgumentException(
                "Invalid row count. Sudoku must have exactly 9 rows, found: " + rows.size()
            );
        }

        // Convert list → 2D array
        int[][] board = new int[9][9];
        for (int i = 0; i < 9; i++) {
            board[i] = rows.get(i);
        }

        System.out.println("Loaded board from: " + filePath);
        return board;
    }
}
