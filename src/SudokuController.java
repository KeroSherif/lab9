/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author monic
 */
import java.io.*;
import java.nio.file.*;
import java.util.*;
import sudoku.InvalidGameException;


public class SudokuController implements Viewable {

    private final Path baseDir;
    private final Path easyDir;
    private final Path mediumDir;
    private final Path hardDir;
    private final Path incompleteDir;
    private final Path logFile;

    public SudokuController() {
        baseDir = Paths.get("games");
        easyDir = baseDir.resolve("easy");
        mediumDir = baseDir.resolve("medium");
        hardDir = baseDir.resolve("hard");
        incompleteDir = baseDir.resolve("incomplete");
        logFile = incompleteDir.resolve("moves.log");

        try {
            Files.createDirectories(easyDir);
            Files.createDirectories(mediumDir);
            Files.createDirectories(hardDir);
            Files.createDirectories(incompleteDir);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create game directories");
        }
    }

    // ===================== Catalog =====================

    @Override
    public Catalog getCatalog() {
        Catalog c = new Catalog();
        c.current = hasIncomplete();
        c.allModesExist =
                hasGame(easyDir) &&
                hasGame(mediumDir) &&
                hasGame(hardDir);
        return c;
    }

    private boolean hasIncomplete() {
        return hasGame(incompleteDir);
    }

    private boolean hasGame(Path dir) {
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(dir, "*.txt")) {
            return ds.iterator().hasNext();
        } catch (IOException e) {
            return false;
        }
    }

    // ===================== Load Game =====================

    @Override
    public Game getGame(DifficultyEnum level) throws NotFoundException {
        Path dir;

        switch (level) {
            case EASY -> dir = easyDir;
            case MEDIUM -> dir = mediumDir;
            case HARD -> dir = hardDir;
            case INCOMPLETE -> dir = incompleteDir;
            default -> throw new NotFoundException("Invalid difficulty");
        }

        List<Path> games = listGames(dir);

        if (games.isEmpty()) {
            throw new NotFoundException("No game found");
        }

        Path selected = games.get(new Random().nextInt(games.size()));

        try {
            int[][] board = readBoard(selected);

            if (level != DifficultyEnum.INCOMPLETE) {
                saveCurrent(board);
            }
<<<<<<< HEAD

            return new Game(board);

=======
            
            return new Game(board, DifficultyEnum.INCOMPLETE);
>>>>>>> 1fa8a54d1b531bfc413e1fa63c7fd29dc9f22fec
        } catch (IOException e) {
            throw new NotFoundException("Failed to load game");
        }
    }

    private List<Path> listGames(Path dir) throws NotFoundException {
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(dir, "*.txt")) {
            List<Path> list = new ArrayList<>();
            for (Path p : ds) list.add(p);
            return list;
        } catch (IOException e) {
            throw new NotFoundException("Cannot read folder");
        }
    }

    private void saveCurrent(int[][] board) throws IOException {
        Files.deleteIfExists(logFile);
        writeBoard(incompleteDir.resolve("current.txt"), board);
    }

    // ===================== Drive Games =====================

    @Override
    public void driveGames(Game sourceGame) throws SolutionInvalidException {

        String result = verifyGame(sourceGame);
        if (!result.equals("valid")) {
            throw new SolutionInvalidException(result);
        }

        RandomPairs rp = new RandomPairs();

        saveGenerated(sourceGame, rp, 10, easyDir);
        saveGenerated(sourceGame, rp, 20, mediumDir);
        saveGenerated(sourceGame, rp, 25, hardDir);
    }
<<<<<<< HEAD

    private void saveGenerated(Game src, RandomPairs rp, int remove, Path dir) {
        int[][] copy = copyBoard(src.board);

        for (int[] p : rp.generateDistinctPairs(remove)) {
            copy[p[0]][p[1]] = 0;
        }

        try {
            writeBoard(
                    dir.resolve("game_" + System.currentTimeMillis() + ".txt"),
                    copy
            );
=======
    
    private Game generateGame(Game source, RandomPairs randomPairs, int cellsToRemove) {
        int[][] newBoard = new int[9][9];
        for (int i = 0; i < 9; i++) {
            newBoard[i] = source.getBoard()[i].clone();
        }
        
        List<int[]> positions = randomPairs.generateDistinctPairs(cellsToRemove);
        for (int[] pos : positions) {
            newBoard[pos[0]][pos[1]] = 0;
        }
        
        return new Game(newBoard, source.getLevel());
    }
    
    private void saveGameToFolder(Game game, String folderPath) {
        try {
            String filename = folderPath + "game_" + System.currentTimeMillis() + ".txt";
            saveBoardToFile(game.getBoard(), filename);
>>>>>>> 1fa8a54d1b531bfc413e1fa63c7fd29dc9f22fec
        } catch (IOException e) {
            throw new RuntimeException("Failed to save game");
        }
    }

    // ===================== Verification =====================

    @Override
    public String verifyGame(Game game) {

        List<String> invalid = new ArrayList<>();

        // rows
        for (int i = 0; i < 9; i++) {
            Set<Integer> s = new HashSet<>();
            for (int j = 0; j < 9; j++) {
<<<<<<< HEAD
                int v = game.board[i][j];
                if (v != 0 && !s.add(v)) {
                    invalid.add(i + "," + j);
=======
                int val = game.getBoard()[i][j];
                if (val != 0) {
                    if (!seen.add(val)) {
                        invalidCells.add(i + "," + j);
                    }
>>>>>>> 1fa8a54d1b531bfc413e1fa63c7fd29dc9f22fec
                }
            }
        }

        // columns
        for (int j = 0; j < 9; j++) {
            Set<Integer> s = new HashSet<>();
            for (int i = 0; i < 9; i++) {
<<<<<<< HEAD
                int v = game.board[i][j];
                if (v != 0 && !s.add(v)) {
                    invalid.add(i + "," + j);
=======
                int val = game.getBoard()[i][j];
                if (val != 0) {
                    if (!seen.add(val)) {
                        invalidCells.add(i + "," + j);
                    }
>>>>>>> 1fa8a54d1b531bfc413e1fa63c7fd29dc9f22fec
                }
            }
        }

        // blocks
        for (int br = 0; br < 3; br++) {
            for (int bc = 0; bc < 3; bc++) {
                Set<Integer> s = new HashSet<>();
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
<<<<<<< HEAD
                        int r = br * 3 + i;
                        int c = bc * 3 + j;
                        int v = game.board[r][c];
                        if (v != 0 && !s.add(v)) {
                            invalid.add(r + "," + c);
=======
                        int row = blockRow * 3 + i;
                        int col = blockCol * 3 + j;
                        int val = game.getBoard()[row][col];
                        if (val != 0) {
                            if (!seen.add(val)) {
                                invalidCells.add(row + "," + col);
                            }
>>>>>>> 1fa8a54d1b531bfc413e1fa63c7fd29dc9f22fec
                        }
                    }
                }
            }
        }
<<<<<<< HEAD

        boolean complete = true;
        for (int[] row : game.board)
            for (int v : row)
                if (v == 0) complete = false;

        if (!invalid.isEmpty())
            return "invalid " + String.join(" ", invalid);

        return complete ? "valid" : "incomplete";
=======
        
        boolean isComplete = true;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (game.getBoard()[i][j] == 0) {
                    isComplete = false;
                    break;
                }
            }
        }
        
        if (!invalidCells.isEmpty()) {
            return "invalid " + String.join(" ", invalidCells);
        } else if (isComplete) {
            return "valid";
        } else {
            return "incomplete";
        }
>>>>>>> 1fa8a54d1b531bfc413e1fa63c7fd29dc9f22fec
    }

    // ===================== Solver =====================

    @Override
    public int[] solveGame(Game game) throws InvalidGameException {
    throw new InvalidGameException("Solver not implemented yet");
    }

    // ===================== Logging =====================

    @Override
    public void logUserAction(String userAction) throws IOException {
        Files.writeString(
                logFile,
                userAction + System.lineSeparator(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        );
    }

    // ===================== Helpers =====================

    private int[][] copyBoard(int[][] b) {
        int[][] c = new int[9][9];
        for (int i = 0; i < 9; i++) c[i] = b[i].clone();
        return c;
    }

    private int[][] readBoard(Path p) throws IOException {
        int[][] b = new int[9][9];
        List<String> lines = Files.readAllLines(p);

        for (int i = 0; i < 9; i++) {
            String[] v = lines.get(i).trim().split("\\s+");
            for (int j = 0; j < 9; j++) {
                b[i][j] = Integer.parseInt(v[j]);
            }
        }
        return b;
    }

    private void writeBoard(Path p, int[][] b) throws IOException {
        try (BufferedWriter w = Files.newBufferedWriter(p)) {
            for (int[] row : b) {
                for (int v : row) {
                    w.write(v + " ");
                }
                w.newLine();
            }
        }
    }
}
