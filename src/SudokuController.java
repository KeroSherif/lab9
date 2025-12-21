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
        c.setUnfinished(hasGame(incompleteDir));
        c.setAllModesExist(
                hasGame(easyDir) &&
                hasGame(mediumDir) &&
                hasGame(hardDir)
        );
        return c;
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

            return new Game(board, level);

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
        for (int[] row : board)
            for (int v : row)
                if (v == 0) complete = false;

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
        Files.writeString(
                logFile,
                userAction + System.lineSeparator(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        );
    }

    // ===================== Helpers =====================

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

    @Override
    public void driveGames(Game sourceGame) throws SolutionInvalidException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}

