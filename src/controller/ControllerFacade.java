package controller;

import java.io.File;
import java.io.IOException;

import model.UserAction;
import exceptions.InvalidGameException;
import exceptions.NotFoundException;
import exceptions.SolutionInvalidException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import model.Catalog;

import model.DifficultyEnum;
import static model.DifficultyEnum.EASY;
import static model.DifficultyEnum.HARD;
import static model.DifficultyEnum.INCOMPLETE;
import static model.DifficultyEnum.MEDIUM;
import model.Game;
import view.Viewable;

public class ControllerFacade implements Viewable {

    private final SudokuController primitiveController;

    public ControllerFacade() {
        this.primitiveController = new SudokuController();
    }

    @Override
    public Catalog getCatalog() {
        boolean[] flags = primitiveController.getCatalog();

        return new Catalog(flags[0], flags[1]) {
        };
    }

    @Override
    public Game getGame(DifficultyEnum level) throws NotFoundException {

        char ch = switch (level) {
            case EASY ->
                'e';
            case MEDIUM ->
                'm';
            case HARD ->
                'h';
            case INCOMPLETE ->
                'c';
        };

        int[][] board = primitiveController.getGame(ch);
        return new Game(board, level);
    }

    @Override
    public void driveGames(Game sourceGame) throws SolutionInvalidException {

        String tempPath = "games/source-temp.txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(tempPath))) {
            int[][] board = sourceGame.getBoard();
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    bw.write(board[i][j] + " ");
                }
                bw.newLine();
            }
        } catch (IOException e) {
            throw new SolutionInvalidException("Failed to write temp source file: " + e.getMessage());
        }

        primitiveController.driveGames(tempPath);
    }

    @Override
    public String verifyGame(Game game) {
        // Delegate to primitive controller and encode per-cell validity
        boolean[][] grid = primitiveController.verifyGame(game.getBoard());
        StringBuilder sb = new StringBuilder(81);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sb.append(grid[i][j] ? '1' : '0');
            }
        }
        return sb.toString();
    }

    @Override
    public int[] solveGame(Game game) throws InvalidGameException {
        int[][] solved = primitiveController.solveGame(game.getBoard());
        int[] flat = new int[81];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                flat[i * 9 + j] = solved[i][j];
            }
        }
        return flat;
    }

    @Override
    public void logUserAction(String userAction) throws IOException {
        // Append to the moves log in the incomplete directory
        Files.createDirectories(Paths.get("games/incomplete"));
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("games/incomplete/moves.log", true))) {
            bw.write(userAction);
            bw.newLine();
        }
    }

    @Override
    public void clearIncompleteGame() {
        primitiveController.clearIncompleteGame();
    }

    @Override
    public void deleteCompletedGame() {
        primitiveController.deleteCompletedGame();
    }

    private void removeCells(int[][] board, int cellsToRemove) {
        Random rand = new Random();
        int removed = 0;

        while (removed < cellsToRemove) {
            int i = rand.nextInt(9);
            int j = rand.nextInt(9);

            if (board[i][j] != 0) {
                board[i][j] = 0;
                removed++;
            }
        }
    }

    @Override
    public void saveCurrentGame(int[][] board) throws IOException {
        primitiveController.saveCurrentGame(board);
    }
}
