
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class SudokuController implements Controllable {

    private static final String GAMES_DIR = "games/";
    private static final String EASY_DIR = GAMES_DIR + "easy/";
    private static final String MEDIUM_DIR = GAMES_DIR + "medium/";
    private static final String HARD_DIR = GAMES_DIR + "hard/";
    private static final String INCOMPLETE_DIR = GAMES_DIR + "incomplete/";
    private static final String LOG_FILE = INCOMPLETE_DIR + "moves.log";

    public SudokuController() {
        try {
            Files.createDirectories(Paths.get(EASY_DIR));
            Files.createDirectories(Paths.get(MEDIUM_DIR));
            Files.createDirectories(Paths.get(HARD_DIR));
            Files.createDirectories(Paths.get(INCOMPLETE_DIR));
        } catch (IOException e) {
            throw new RuntimeException("Failed to create directories");
        }
    }

    // ================= CATALOG =================
    @Override
    public boolean[] getCatalog() {
        return new boolean[]{
            hasGame(INCOMPLETE_DIR),
            hasGame(EASY_DIR) && hasGame(MEDIUM_DIR) && hasGame(HARD_DIR)
        };
    }

    private boolean hasGame(String dirPath) {
        File[] files = new File(dirPath).listFiles((d, n) -> n.endsWith(".txt"));
        return files != null && files.length > 0;
    }

    // ================= LOAD GAME =================
    @Override
    public int[][] getGame(char level) throws NotFoundException {
        String dir = switch (level) {
            case 'e' ->
                EASY_DIR;
            case 'm' ->
                MEDIUM_DIR;
            case 'h' ->
                HARD_DIR;
            case 'c' ->
                INCOMPLETE_DIR;
            default ->
                throw new NotFoundException("Invalid level");
        };

        File[] files = new File(dir).listFiles((d, n) -> n.endsWith(".txt"));
        if (files == null || files.length == 0) {
            throw new NotFoundException("No game found");
        }

        try {
            int[][] board = readBoardFromFile(files[new Random().nextInt(files.length)]);
            if (level != 'c') {
                saveIncompleteGame(board);
            }
            return board;
        } catch (IOException e) {
            throw new NotFoundException("Invalid file");
        }
    }

    // ================= GENERATE =================
    @Override
    public void driveGames(String sourcePath) throws SolutionInvalidException {
        try {
            new GameGenerator().generateLevels(sourcePath);
        } catch (GameGenerator.SolutionInvalidException e) {
            throw new SolutionInvalidException(e.getMessage());
        }
    }

    // ================= VERIFY =================
    @Override
    public boolean[][] verifyGame(int[][] board) {
        boolean[][] valid = new boolean[9][9];
        for (int i = 0; i < 9; i++) {
            Arrays.fill(valid[i], true);
        }

        // rows
        for (int i = 0; i < 9; i++) {
            Map<Integer, Integer> counts = new HashMap<>();
            for (int j = 0; j < 9; j++) {
                int v = board[i][j];

                if (v != 0) {
                    counts.put(v, counts.getOrDefault(v, 0) + 1);
                }
            }
            for (int j = 0; j < 9; j++) {
                int v = board[i][j];
                if (v != 0 && counts.get(v) > 1) {
                    valid[i][j] = false;
                }
            }
        }

        // columns
        for (int j = 0; j < 9; j++) {
            Map<Integer, Integer> counts = new HashMap<>();
            for (int i = 0; i < 9; i++) {
                int v = board[i][j];
                if (v != 0) {
                    counts.put(v, counts.getOrDefault(v, 0) + 1);
                }
            }
            for (int i = 0; i < 9; i++) {
                int v = board[i][j];
                if (v != 0 && counts.get(v) > 1) {
                    valid[i][j] = false;
                }
            }
        }

        // 3x3 boxes
        for (int boxRow = 0; boxRow < 3; boxRow++) {
            for (int boxCol = 0; boxCol < 3; boxCol++) {
                Map<Integer, Integer> counts = new HashMap<>();
                for (int i = boxRow * 3; i < boxRow * 3 + 3; i++) {
                    for (int j = boxCol * 3; j < boxCol * 3 + 3; j++) {
                        int v = board[i][j];
                        if (v != 0) {
                            counts.put(v, counts.getOrDefault(v, 0) + 1);
                        }
                    }
                }
                for (int i = boxRow * 3; i < boxRow * 3 + 3; i++) {
                    for (int j = boxCol * 3; j < boxCol * 3 + 3; j++) {
                        int v = board[i][j];
                        if (v != 0 && counts.get(v) > 1) {
                            valid[i][j] = false;
                        }
                    }
                }
            }
        }

        // columns
        for (int j = 0; j < 9; j++) {
            Set<Integer> s = new HashSet<>();
            for (int i = 0; i < 9; i++) {
                int v = board[i][j];
                if (v != 0 && !s.add(v)) {
                    valid[i][j] = false;
                }
            }
        }

        // blocks
        for (int br = 0; br < 3; br++) {
            for (int bc = 0; bc < 3; bc++) {
                Set<Integer> s = new HashSet<>();
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        int r = br * 3 + i;
                        int c = bc * 3 + j;
                        int v = board[r][c];
                        if (v != 0 && !s.add(v)) {
                            valid[r][c] = false;
                        }
                    }
                }
            }
        }

        return valid;
    }

    // ================= SOLVE =================
    @Override

    public int[][] solveGame(int[][] game) throws InvalidGameException {

        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(game[i], 0, copy[i], 0, 9);
        }

        int[] result = SudokuSolver.solve(copy);

        if (result == null) {
            throw new InvalidGameException("No solution exists");
        }

        return copy;
    }

    // ================= LOG =================
    @Override
    public void logUserAction(UserAction action) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            bw.write(action.toString());
            bw.newLine();
        }
    }

    public boolean undoLastMove(int[][] board) throws IOException {
        return new UndoLogger(LOG_FILE).undoLastMove(board);
    }

    // ================= SAVE / LOAD =================
    public void saveIncompleteGame(int[][] board) throws IOException {
        clearIncompleteGame();
        saveBoardToFile(board, INCOMPLETE_DIR + "current.txt");
    }

    public void clearIncompleteGame() {
        File dir = new File(INCOMPLETE_DIR);
        if (dir.exists()) {
            for (File f : Objects.requireNonNull(dir.listFiles())) {
                f.delete();
            }
        }
    }

    // ================= HELPERS =================
    private int[][] readBoardFromFile(File file) throws IOException {
        int[][] board = new int[9][9];
        try (Scanner sc = new Scanner(file)) {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    board[i][j] = sc.nextInt();
                }
            }
        }
        return board;
    }

    private void saveBoardToFile(int[][] board, String path) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for (int[] row : board) {
                for (int v : row) {
                    bw.write(v + " ");
                }
                bw.newLine();
            }
        }
    }

    @Override
    public int[][] getRandomGame(char level) throws Exception {

        String dir;
        switch (level) {
            case 'e':
                dir = EASY_DIR;
                break;
            case 'm':
                dir = MEDIUM_DIR;
                break;
            case 'h':
                dir = HARD_DIR;
                break;
            default:
                throw new Exception("Invalid level");
        }

        File folder = new File(dir);
        File[] files = folder.listFiles((d, n) -> n.endsWith(".txt"));

        if (files == null || files.length == 0) {
            throw new Exception("No games available");
        }

        File chosen = files[new Random().nextInt(files.length)];

        int[][] board = readBoardFromFile(chosen);

        clearIncompleteGame();
        saveBoardToFile(board, INCOMPLETE_DIR + "current.txt");

        return board;
    }
  // ================= LOAD =================
    public int[][] loadSelectedGame(File file) throws Exception {

        int[][] board = readBoardFromFile(file);

        
        clearIncompleteGame();
        saveBoardToFile(board, INCOMPLETE_DIR + "current.txt");

        return board;
    }

}
