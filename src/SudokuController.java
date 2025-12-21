import java.io.*;
import java.nio.file.*;
import java.util.*;

public class SudokuController implements Viewable {

    private static final String GAMES_DIR = "games/";
    private static final String EASY_DIR = GAMES_DIR + "easy/";
    private static final String MEDIUM_DIR = GAMES_DIR + "medium/";
    private static final String HARD_DIR = GAMES_DIR + "hard/";
    private static final String INCOMPLETE_DIR = GAMES_DIR + "incomplete/";
    private static final String LOG_FILE = INCOMPLETE_DIR + "moves.log";

    public SudokuController() {
        createDirectories();
    }

    // ===================== Directories =====================

    private void createDirectories() {
        try {
            Files.createDirectories(Paths.get(EASY_DIR));
            Files.createDirectories(Paths.get(MEDIUM_DIR));
            Files.createDirectories(Paths.get(HARD_DIR));
            Files.createDirectories(Paths.get(INCOMPLETE_DIR));
        } catch (IOException e) {
            throw new RuntimeException("Error creating directories");
        }
    }

    // ===================== Catalog =====================

    @Override
    public Catalog getCatalog() {
        boolean hasIncomplete = hasGameInFolder(INCOMPLETE_DIR);
        boolean hasAllLevels =
                hasGameInFolder(EASY_DIR) &&
                hasGameInFolder(MEDIUM_DIR) &&
                hasGameInFolder(HARD_DIR);

        return new Catalog(hasIncomplete, hasAllLevels);
    }

    private boolean hasGameInFolder(String folderPath) {
        File dir = new File(folderPath);
        File[] files = dir.listFiles(
                (d, name) -> name.endsWith(".txt") && !name.equals("moves.log")
        );
        return files != null && files.length > 0;
    }

    // ===================== Load Game =====================

    @Override
    public Game getGame(DifficultyEnum level) throws NotFoundException {

        String folderPath;

        switch (level) {
            case EASY -> folderPath = EASY_DIR;
            case MEDIUM -> folderPath = MEDIUM_DIR;
            case HARD -> folderPath = HARD_DIR;
            case INCOMPLETE -> folderPath = INCOMPLETE_DIR;
            default -> throw new NotFoundException("Invalid difficulty");
        }

        File dir = new File(folderPath);
        File[] files = dir.listFiles(
                (d, name) -> name.endsWith(".txt") && !name.equals("moves.log")
        );

        if (files == null || files.length == 0) {
            throw new NotFoundException("No game found");
        }

        File selected = files[new Random().nextInt(files.length)];

        try {
            int[][] board = loadBoardFromFile(selected);

            if (level != DifficultyEnum.INCOMPLETE) {
                saveBoardToFile(board, INCOMPLETE_DIR + "current.txt");
                new File(LOG_FILE).delete();
            }

            return new Game(board, level);

        } catch (IOException e) {
            throw new NotFoundException("Failed to load game");
        }
    }

    // ===================== Drive Games =====================

    @Override
    public void driveGames(Game sourceGame) throws SolutionInvalidException {

        String result = verifyGame(sourceGame);
        if (!result.equals("valid")) {
            throw new SolutionInvalidException(result);
        }

        RandomPairs rp = new RandomPairs();

        saveGameToFolder(generateGame(sourceGame, rp, 10), EASY_DIR);
        saveGameToFolder(generateGame(sourceGame, rp, 20), MEDIUM_DIR);
        saveGameToFolder(generateGame(sourceGame, rp, 25), HARD_DIR);
    }

    private Game generateGame(Game source, RandomPairs rp, int remove) {

        int[][] newBoard = new int[9][9];
        for (int i = 0; i < 9; i++) {
            newBoard[i] = source.getBoard()[i].clone();
        }

        for (int[] pos : rp.generateDistinctPairs(remove)) {
            newBoard[pos[0]][pos[1]] = 0;
        }

        return new Game(newBoard, source.getLevel());
    }

    private void saveGameToFolder(Game game, String folderPath) {
        try {
            String filename = folderPath + "game_" + System.currentTimeMillis() + ".txt";
            saveBoardToFile(game.getBoard(), filename);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save game");
        }
    }

    // ===================== Verification =====================

    @Override
    public String verifyGame(Game game) {

        List<String> invalid = new ArrayList<>();
        int[][] board = game.getBoard();

        // rows
        for (int i = 0; i < 9; i++) {
            Set<Integer> s = new HashSet<>();
            for (int j = 0; j < 9; j++) {
                int v = board[i][j];
                if (v != 0 && !s.add(v)) {
                    invalid.add(i + "," + j);
                }
            }
        }

        // columns
        for (int j = 0; j < 9; j++) {
            Set<Integer> s = new HashSet<>();
            for (int i = 0; i < 9; i++) {
                int v = board[i][j];
                if (v != 0 && !s.add(v)) {
                    invalid.add(i + "," + j);
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
                            invalid.add(r + "," + c);
                        }
                    }
                }
            }
        }

        boolean complete = true;
        for (int[] row : board) {
            for (int v : row) {
                if (v == 0) {
                    complete = false;
                    break;
                }
            }
        }

        if (!invalid.isEmpty())
            return "invalid " + String.join(" ", invalid);

        return complete ? "valid" : "incomplete";
    }

    // ===================== Solver =====================

    @Override
    public int[] solveGame(Game game) throws InvalidGameException {
        throw new InvalidGameException("Solver not implemented yet");
    }

    // ===================== Logging =====================

    @Override
    public void logUserAction(String userAction) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            bw.write(userAction);
            bw.newLine();
        }
    }

    // ===================== Helpers =====================

    private int[][] loadBoardFromFile(File file) throws IOException {
        int[][] board = new int[9][9];

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            for (int i = 0; i < 9; i++) {
                String[] vals = br.readLine().trim().split("\\s+");
                for (int j = 0; j < 9; j++) {
                    board[i][j] = Integer.parseInt(vals[j]);
                }
            }
        }
        return board;
    }

    private void saveBoardToFile(int[][] board, String filename) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (int[] row : board) {
                for (int v : row) {
                    bw.write(v + " ");
                }
                bw.newLine();
            }
        }
    }
}
