/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author monic
 */
public enum DifficultyEnum {
    EASY,
    MEDIUM,
    HARD,
    INCOMPLETE
}import java.io.*;
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

    private void createDirectories() {
        try {
            Files.createDirectories(Paths.get(EASY_DIR));
            Files.createDirectories(Paths.get(MEDIUM_DIR));
            Files.createDirectories(Paths.get(HARD_DIR));
            Files.createDirectories(Paths.get(INCOMPLETE_DIR));
        } catch (IOException e) {
            System.err.println("Error creating directories: " + e.getMessage());
        }
    }


    @Override
    public Catalog getCatalog() {
        boolean hasIncomplete = hasIncompleteGame();
        boolean hasEasy = hasGameInFolder(EASY_DIR);
        boolean hasMedium = hasGameInFolder(MEDIUM_DIR);
        boolean hasHard = hasGameInFolder(HARD_DIR);

        Catalog c = new Catalog();
        c.current = hasIncomplete;
        c.allModesExist = hasEasy && hasMedium && hasHard;
        return c;
    }

    private boolean hasIncompleteGame() {
        File dir = new File(INCOMPLETE_DIR);
        File[] files = dir.listFiles((d, name) ->
                name.endsWith(".txt") && !name.equals("moves.log"));
        return files != null && files.length > 0;
    }

    private boolean hasGameInFolder(String folderPath) {
        File dir = new File(folderPath);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));
        return files != null && files.length > 0;
    }


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
        File[] files = dir.listFiles((d, name) ->
                name.endsWith(".txt") && !name.equals("moves.log"));

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

            return new Game(board);

        } catch (IOException e) {
            throw new NotFoundException("Failed to load game");
        }
    }


    @Override
    public void driveGames(Game sourceGame) throws SolutionInvalidException {

        String result = verifyGame(sourceGame);
        if (!result.equals("valid")) {
            throw new SolutionInvalidException(result);
        }

        RandomPairs rp = new RandomPairs();

        saveGame(generateGame(sourceGame, rp, 10), EASY_DIR);
        saveGame(generateGame(sourceGame, rp, 20), MEDIUM_DIR);
        saveGame(generateGame(sourceGame, rp, 25), HARD_DIR);
    }

    private Game generateGame(Game source, RandomPairs rp, int remove) {
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++) {
            copy[i] = source.board[i].clone();
        }

        for (int[] p : rp.generateDistinctPairs(remove)) {
            copy[p[0]][p[1]] = 0;
        }

        return new Game(copy);
    }

    private void saveGame(Game game, String folder) {
        try {
            saveBoardToFile(
                    game.board,
                    folder + "game_" + System.currentTimeMillis() + ".txt"
            );
        } catch (IOException e) {
            System.err.println("Save error");
        }
    }


    @Override
    public String verifyGame(Game game) {

        List<String> invalid = new ArrayList<>();

        // rows
        for (int i = 0; i < 9; i++) {
            Set<Integer> seen = new HashSet<>();
            for (int j = 0; j < 9; j++) {
                int v = game.board[i][j];
                if (v != 0 && !seen.add(v)) {
                    invalid.add(i + "," + j);
                }
            }
        }

        // columns
        for (int j = 0; j < 9; j++) {
            Set<Integer> seen = new HashSet<>();
            for (int i = 0; i < 9; i++) {
                int v = game.board[i][j];
                if (v != 0 && !seen.add(v)) {
                    invalid.add(i + "," + j);
                }
            }
        }

        // blocks
        for (int br = 0; br < 3; br++) {
            for (int bc = 0; bc < 3; bc++) {
                Set<Integer> seen = new HashSet<>();
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        int r = br * 3 + i;
                        int c = bc * 3 + j;
                        int v = game.board[r][c];
                        if (v != 0 && !seen.add(v)) {
                            invalid.add(r + "," + c);
                        }
                    }
                }
            }
        }

        boolean complete = true;
        for (int[] row : game.board) {
            for (int v : row) {
                if (v == 0) complete = false;
            }
        }

        if (!invalid.isEmpty()) return "invalid " + String.join(" ", invalid);
        if (complete) return "valid";
        return "incomplete";
    }


    @Override
    public int[] solveGame(Game game) throws InvalidGame {
        throw new InvalidGame("Solver not implemented");
    }


    @Override
    public void logUserAction(String userAction) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            bw.write(userAction);
            bw.newLine();
        }
    }


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
}
