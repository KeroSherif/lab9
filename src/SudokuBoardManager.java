import java.io.*;
import java.util.Random;

public class SudokuBoardManager {

    public static int[][] loadOrCreateBoard(String path) throws Exception {

        File file = new File(path);

        if (file.exists()) {
            return readCsvBoard(path);
        }

        System.out.println("File not found. Creating new random board: " + path);

        int[][] newBoard = generateRandomValidBoard();
        saveToCsv(path, newBoard);
        return newBoard;
    }

    public static int[][] readCsvBoard(String path) throws IOException {
        int[][] board = new int[9][9];

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {

            String line;
            int row = 0;

            while ((line = br.readLine()) != null && row < 9) {

                String[] parts = line.split(",");

                if (parts.length != 9) {
                    throw new IOException("Each row must contain 9 values");
                }

                for (int col = 0; col < 9; col++) {
                    board[row][col] = Integer.parseInt(parts[col].trim());
                }

                row++;
            }

            if (row != 9) {
                throw new IOException("CSV must contain exactly 9 rows");
            }
        }

        return board;
    }

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

        // Shuffle digits
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
