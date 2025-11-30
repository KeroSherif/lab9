import java.io.*;
import java.util.*;

public class SudokuBoardManager {

    public static int[][] loadOrCreateBoard(String path) {

        File file = new File(path);

        // 1. If file DOES NOT exist => create new
        if (!file.exists()) {
            System.out.println("⚠ File not found. Creating new board: " + path);
            int[][] board = generateRandomValidBoard();
            saveSafely(path, board);
            return board;
        }

        // 2. File exists → Try reading it
        try {
            int[][] board = readCsvBoard(path);
            System.out.println("Loaded board from: " + path);
            return board;
        } catch (Exception e) {
            // 3. File exists but CORRUPTED → create new one
            System.out.println("⚠ Invalid CSV format. Creating new board: " + path);
            int[][] fresh = generateRandomValidBoard();
            saveSafely(path, fresh);
            return fresh;
        }
    }

    // --- CSV Reader ---
    public static int[][] readCsvBoard(String path) throws IOException {
        int[][] board = new int[9][9];

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            int row = 0;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) throw new IOException("Empty line detected.");

                String[] parts = line.split(",");

                if (parts.length != 9)
                    throw new IOException("Row must have 9 values exactly.");

                for (int col = 0; col < 9; col++) {
                    try {
                        int value = Integer.parseInt(parts[col].trim());
                        if (value < 0 || value > 9)
                            throw new IOException("Invalid value " + value);

                        board[row][col] = value;

                    } catch (NumberFormatException nfe) {
                        throw new IOException("Non-numeric value detected.");
                    }
                }

                row++;
            }

            if (row != 9)
                throw new IOException("CSV must have 9 rows.");

        }

        return board;
    }

    // --- Safe Save ---
    private static void saveSafely(String path, int[][] board) {
        try {
            saveToCsv(path, board);
        } catch (IOException e) {
            System.out.println("❌ Failed to save new board: " + e.getMessage());
        }
    }

    // --- Random Valid Board Generator ---
    private static int[][] generateRandomValidBoard() {
        int[][] base = {
                {1,2,3,4,5,6,7,8,9},
                {4,5,6,7,8,9,1,2,3},
                {7,8,9,1,2,3,4,5,6},
                {2,3,4,5,6,7,8,9,1},
                {5,6,7,8,9,1,2,3,4},
                {8,9,1,2,3,4,5,6,7},
                {3,4,5,6,7,8,9,1,2},
                {6,7,8,9,1,2,3,4,5},
                {9,1,2,3,4,5,6,7,8}
        };

        int[][] board = deepCopy(base);
        Random r = new Random();

        // shuffle digits
        int[] map = new int[10];
        boolean[] used = new boolean[10];

        for (int d = 1; d <= 9; d++) {
            int newD;
            do newD = r.nextInt(9) + 1;
            while (used[newD]);
            used[newD] = true;
            map[d] = newD;
        }

        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                board[i][j] = map[board[i][j]];

        return board;
    }

    private static int[][] deepCopy(int[][] src) {
        int[][] dst = new int[9][9];
        for (int i = 0; i < 9; i++)
            System.arraycopy(src[i], 0, dst[i], 0, 9);
        return dst;
    }

    private static void saveToCsv(String path, int[][] board) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (j > 0) pw.print(",");
                    pw.print(board[i][j]);
                }
                pw.println();
            }
        }
    }
}
