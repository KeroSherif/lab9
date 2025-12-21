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
    
    // Track current game difficulty for deletion when completed
    private DifficultyEnum currentGameDifficulty = null;
    private String currentGameFile = null;

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
            case 'e' -> EASY_DIR;
            case 'm' -> MEDIUM_DIR;
            case 'h' -> HARD_DIR;
            case 'c' -> INCOMPLETE_DIR;
            default -> throw new NotFoundException("Invalid level");
        };

        File[] files = new File(dir).listFiles((d, n) -> n.endsWith(".txt"));
        if (files == null || files.length == 0) {
            throw new NotFoundException("No game found");
        }

        try {
            File selectedFile = files[new Random().nextInt(files.length)];
            int[][] board = readBoardFromFile(selectedFile);
            
            // Track difficulty and file for later deletion if completed
            if (level == 'e') {
                currentGameDifficulty = DifficultyEnum.EASY;
                currentGameFile = selectedFile.getName();
            } else if (level == 'm') {
                currentGameDifficulty = DifficultyEnum.MEDIUM;
                currentGameFile = selectedFile.getName();
            } else if (level == 'h') {
                currentGameDifficulty = DifficultyEnum.HARD;
                currentGameFile = selectedFile.getName();
            } else {
                currentGameDifficulty = null;
                currentGameFile = null;
            }
            
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

        // Check rows
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

        // Check columns
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

        // Check 3x3 boxes
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

        return valid;
    }

    // ================= SOLVE =================
    @Override
    public int[][] solveGame(int[][] game) throws InvalidGameException {
        int emptyCount = countEmptyCells(game);
        
        // Check if exactly 5 empty cells
        if (emptyCount != 5) {
            throw new InvalidGameException(
                "Solver only works with exactly 5 empty cells. Current: " + emptyCount
            );
        }

        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(game[i], 0, copy[i], 0, 9);
        }

        // Use MultiThreadedSudokuSolver (permutation-based)
        int[][] solved = MultiThreadedSudokuSolver.solveBoard(copy);

        if (solved == null) {
            throw new InvalidGameException("No solution exists");
        }

        return solved;
    }

    private int countEmptyCells(int[][] board) {
        int count = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0) {
                    count++;
                }
            }
        }
        return count;
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

    @Override
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
                    if (sc.hasNextInt()) {
                        board[i][j] = sc.nextInt();
                    } else {
                        throw new IOException("Invalid board format");
                    }
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
        DifficultyEnum difficulty;
        switch (level) {
            case 'e':
                dir = EASY_DIR;
                difficulty = DifficultyEnum.EASY;
                break;
            case 'm':
                dir = MEDIUM_DIR;
                difficulty = DifficultyEnum.MEDIUM;
                break;
            case 'h':
                dir = HARD_DIR;
                difficulty = DifficultyEnum.HARD;
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
        
        // Track for deletion when completed
        currentGameDifficulty = difficulty;
        currentGameFile = chosen.getName();

        clearIncompleteGame();
        saveBoardToFile(board, INCOMPLETE_DIR + "current.txt");

        return board;
    }

    // ================= LOAD SELECTED =================
    public int[][] loadSelectedGame(File file) throws Exception {
        int[][] board = readBoardFromFile(file);
        
        // Try to determine difficulty from file path
        String path = file.getAbsolutePath();
        if (path.contains("easy")) {
            currentGameDifficulty = DifficultyEnum.EASY;
        } else if (path.contains("medium")) {
            currentGameDifficulty = DifficultyEnum.MEDIUM;
        } else if (path.contains("hard")) {
            currentGameDifficulty = DifficultyEnum.HARD;
        } else {
            currentGameDifficulty = null;
        }
        currentGameFile = file.getName();

        clearIncompleteGame();
        saveBoardToFile(board, INCOMPLETE_DIR + "current.txt");
        return board;
    }
    
    // ================= DELETE COMPLETED GAME =================
    /**
     * Deletes the current game from its difficulty folder and clears incomplete folder.
     * Called when user completes the puzzle successfully.
     */
    public void deleteCompletedGame() {
        if (currentGameDifficulty != null && currentGameFile != null) {
            String dir = switch (currentGameDifficulty) {
                case EASY -> EASY_DIR;
                case MEDIUM -> MEDIUM_DIR;
                case HARD -> HARD_DIR;
                case INCOMPLETE -> INCOMPLETE_DIR; // Should not happen, but handle for completeness
            };
            
            File gameFile = new File(dir + currentGameFile);
            if (gameFile.exists()) {
                gameFile.delete();
                System.out.println("Deleted completed game: " + gameFile.getPath());
            }
        }
        
        // Clear incomplete folder (both game and log files)
        clearIncompleteGame();
        
        // Reset tracking
        currentGameDifficulty = null;
        currentGameFile = null;
    }

}
