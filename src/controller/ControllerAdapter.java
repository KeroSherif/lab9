package controller;

import model.*;
import exceptions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import model.Catalog;
import storage.UndoLogger;
import view.Viewable;

public class ControllerAdapter implements Controllable {

    // Reference to the Controller (which implements Viewable)
    private final Viewable controller;

    public ControllerAdapter(Viewable controller) {
        this.controller = controller;
    }

    @Override
    public boolean[] getCatalog() {
        Catalog catalog = controller.getCatalog();

        // Convert Catalog object to primitive boolean array
        boolean[] result = new boolean[2];
        result[0] = catalog.hasUnfinished();  // current/unfinished status
        result[1] = catalog.areModesReady();   // allModesExist

        return result;
    }

    @Override
    public int[][] getGame(char level) throws NotFoundException {

        DifficultyEnum difficulty = charToDifficulty(level);
        Game game = controller.getGame(difficulty);

        String result = controller.verifyGame(game);

        if (result.length() != 81 || result.contains("0")) {
            throw new NotFoundException("Loaded board is INVALID or INCOMPLETE");
        }

        return game.getBoard();
    }

    @Override
    public void driveGames(String sourcePath) throws SolutionInvalidException {
        int[][] board = parseBoardFile(new File(sourcePath));
        Game sourceGame = new Game(board);
        controller.driveGames(sourceGame);
    }

    @Override
    public boolean[][] verifyGame(int[][] game) {

        Game gameObj = new Game(game, DifficultyEnum.EASY);

        String verificationResult = controller.verifyGame(gameObj);

        boolean[][] result = new boolean[9][9];
        if (verificationResult.length() != 81) {

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    result[i][j] = true;
                }
            }
            return result;
        }

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                char ch = verificationResult.charAt(i * 9 + j);
                result[i][j] = (ch == '1');
            }
        }
        return result;
    }

    @Override
    public int[][] solveGame(int[][] game) throws InvalidGameException {

        Game gameObj = new Game(game, DifficultyEnum.EASY);

        int[] flatSolution = controller.solveGame(gameObj);

        int[][] solution = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                solution[i][j] = flatSolution[i * 9 + j];
            }
        }

        return solution;
    }

    @Override
    public void logUserAction(UserAction userAction) throws IOException {

        String actionString = userAction.toString();

        controller.logUserAction(actionString);
    }

    private DifficultyEnum charToDifficulty(char level) {
        switch (Character.toUpperCase(level)) {
            case 'E':
                return DifficultyEnum.EASY;
            case 'M':
                return DifficultyEnum.MEDIUM;
            case 'H':
                return DifficultyEnum.HARD;
            case 'C':
                return DifficultyEnum.INCOMPLETE;
            default:
                throw new IllegalArgumentException("Invalid difficulty level: " + level);
        }
    }

    @Override
    public int[][] getRandomGame(char level) throws Exception {
        // Delegate to facade's underlying primitive controller via added methods
        if (controller instanceof ControllerFacade) {
            ControllerFacade facade = (ControllerFacade) controller;
            // Use the primitive controller through facade.getGame with difficulty
            DifficultyEnum diff = charToDifficulty(level);
            Game g = facade.getGame(diff);
            return g.getBoard();
        }
        throw new UnsupportedOperationException("Random game not supported by controller");
    }

    @Override
    public void clearIncompleteGame() {
        controller.clearIncompleteGame();
    }

    @Override
    public int[][] loadSelectedGame(File file) throws Exception {

        int[][] board = parseBoardFile(file);

        // ❌ INCOMPLETE = فيها 0
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0) {
                    throw new Exception("Board is INCOMPLETE");
                }
            }
        }

        // ❌ INVALID = تكرار
        Game tempGame = new Game(board, DifficultyEnum.EASY);
        String result = controller.verifyGame(tempGame);

        if (result.length() != 81 || result.contains("0")) {
            throw new Exception("Board is INVALID");
        }

        // ✅ VALID فقط
        controller.clearIncompleteGame();
        return board;
    }

    @Override
    public void deleteCompletedGame() {
        controller.deleteCompletedGame();
    }

    @Override
    public boolean undoLastMove(int[][] board) throws IOException {

        String logPath = "games/incomplete/moves.log";
        if (!Files.exists(Paths.get(logPath))) {
            return false;
        }
        UndoLogger ul = new UndoLogger(logPath);
        return ul.undoLastMove(board);
    }

    @Override
    public void saveCurrentGame(int[][] board) throws IOException {

        if (controller instanceof ControllerFacade) {
            ControllerFacade facade = (ControllerFacade) controller;
            facade.saveCurrentGame(board);
        }
    }

    private int[][] parseBoardFile(File file) throws SolutionInvalidException {
        int[][] board = new int[9][9];
        try (Scanner sc = new Scanner(file)) {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (!sc.hasNextInt()) {
                        throw new SolutionInvalidException("Invalid board format in file: " + file.getName());
                    }
                    board[i][j] = sc.nextInt();
                }
            }
        } catch (IOException e) {
            throw new SolutionInvalidException("Failed to read file: " + e.getMessage());
        }
        return board;
    }
}
