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
            throw new RuntimeException(e);
        }
    }

    // ================= CATALOG =================
    @Override
    public boolean[] getCatalog() {
        boolean hasIncomplete = hasGame(INCOMPLETE_DIR);
        boolean hasAll =
                hasGame(EASY_DIR) &&
                hasGame(MEDIUM_DIR) &&
                hasGame(HARD_DIR);

        return new boolean[]{hasIncomplete, hasAll};
    }

    private boolean hasGame(String dirPath) {
        File dir = new File(dirPath);
        File[] files = dir.listFiles((d, n) -> n.endsWith(".txt"));
        return files != null && files.length > 0;
    }

    // ================= LOAD GAME =================
    @Override
    public int[][] getGame(char level) throws NotFoundException {

        String dir;
        switch (level) {
            case 'e': dir = EASY_DIR; break;
            case 'm': dir = MEDIUM_DIR; break;
            case 'h': dir = HARD_DIR; break;
            case 'c': dir = INCOMPLETE_DIR; break;
            default: throw new NotFoundException("Invalid level");
        }

        File[] files = new File(dir).listFiles((d, n) -> n.endsWith(".txt"));
        if (files == null || files.length == 0)
            throw new NotFoundException("No game found");

        return loadBoard(files[0]);
    }

    // ================= GENERATE =================
    @Override
    public void driveGames(String sourcePath) throws SolutionInvalidException {
        GameGenerator generator = new GameGenerator();
        try {
            generator.generateLevels(sourcePath);
        } catch (GameGenerator.SolutionInvalidException e) {
            throw new SolutionInvalidException(e.getMessage());
        }
    }

    // ================= VERIFY =================
    @Override
    public boolean[][] verifyGame(int[][] board) {

        boolean[][] valid = new boolean[9][9];
        for (int i = 0; i < 9; i++)
            Arrays.fill(valid[i], true);

        // rows
        for (int i = 0; i < 9; i++) {
            Set<Integer> s = new HashSet<>();
            for (int j = 0; j < 9; j++) {
                int v = board[i][j];
                if (v != 0 && !s.add(v))
                    valid[i][j] = false;
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
        
        try {
            int[] solution = SudokuSolver.solve(copy);
            if (solution == null) {
                throw new InvalidGameException("No solution found");
            }
            return copy;
        } catch (Exception e) {
            throw new InvalidGameException("Solver error: " + e.getMessage());
        }
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
        UndoLogger logger = new UndoLogger(LOG_FILE);
        return logger.undoLastMove(board);
    }

    // ================= HELPERS =================
    private int[][] loadBoard(File f) throws NotFoundException {
        int[][] b = new int[9][9];
        try (Scanner sc = new Scanner(f)) {
            for (int i = 0; i < 9; i++)
                for (int j = 0; j < 9; j++)
                    b[i][j] = sc.nextInt();
        } catch (Exception e) {
            throw new NotFoundException("Invalid file");
        }
        return b;
    }
}
