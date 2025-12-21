

import java.io.*;
import java.util.*;

public class SudokuBoardManager {

    public static int[][] loadBoard(String filePath) throws Exception {

        List<int[]> rows = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String line;
            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length != 9)
                    throw new IllegalArgumentException("Each row must have 9 values");

                int[] row = new int[9];

                for (int i = 0; i < 9; i++) {
                    int val = Integer.parseInt(parts[i].trim());

                    // ALLOW 0..9
                    if (val < 0 || val > 9)
                        throw new IllegalArgumentException("Values must be between 0 and 9");

                    row[i] = val;
                }

                rows.add(row);
            }
        }

        if (rows.size() != 9)
            throw new IllegalArgumentException("Sudoku must have exactly 9 rows");

        int[][] board = new int[9][9];
        for (int i = 0; i < 9; i++)
            board[i] = rows.get(i);

        return board;
    }
}
