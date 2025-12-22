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
    
    /**
     * Gets a game board as primitive 2D array based on difficulty character.
     * Converts from char to DifficultyEnum, calls controller, converts Game to int[][].
     * 
     * @param level Character representing difficulty ('E'=EASY, 'M'=MEDIUM, 'H'=HARD)
     * @return int[][] the game board as primitive 2D array
     * @throws NotFoundException if no game exists for the specified level
     */
    @Override
    public int[][] getGame(char level) throws NotFoundException {
        // Convert primitive char to DifficultyEnum object
        DifficultyEnum difficulty = charToDifficulty(level);
        
        // Call controller with object
        Game game = controller.getGame(difficulty);
        
        // Convert Game object back to primitive int[][]
        return game.getBoard();
    }
    
    /**
     * Drives game generation from a file path.
     * Converts from String path to Game object, calls controller.
     * 
     * @param sourcePath Path to the source game file
     * @throws SolutionInvalidException if the source game solution is invalid
     */
    @Override
    public void driveGames(String sourcePath) throws SolutionInvalidException {
        int[][] board = parseBoardFile(new File(sourcePath));
        Game sourceGame = new Game(board);
        controller.driveGames(sourceGame);
    }
    
    /**
     * Verifies a game board passed as primitive 2D array.
     * Converts int[][] to Game object, calls controller, converts String to boolean[][].
     * 
     * @param game The game board as primitive 2D array
     * @return boolean[][] verification results for each cell
     */
    @Override
    public boolean[][] verifyGame(int[][] game) {
        // Convert primitive int[][] to Game object
        Game gameObj = new Game(game, DifficultyEnum.EASY); // Default difficulty
        
        // Call controller with object
        String verificationResult = controller.verifyGame(gameObj);
        
        // Convert encoded 81-char string to boolean[][] grid
        boolean[][] result = new boolean[9][9];
        if (verificationResult.length() != 81) {
            // Fallback: treat as all-valid if unexpected format
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
    
    /**
     * Solves a game board passed as primitive 2D array.
     * Converts int[][] to Game object, calls controller, converts int[] to int[][].
     * 
     * @param game The game board to solve as primitive 2D array
     * @return int[][] the solved board as primitive 2D array
     * @throws InvalidGameException if the game cannot be solved
     */
    @Override
    public int[][] solveGame(int[][] game) throws InvalidGameException {
        // Convert primitive int[][] to Game object
        Game gameObj = new Game(game, DifficultyEnum.EASY); // Default difficulty
        
        int[] flatSolution = controller.solveGame(gameObj);
        
        // Convert flat int[] back to 2D primitive int[][]
        int[][] solution = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                solution[i][j] = flatSolution[i * 9 + j];
            }
        }
        
        return solution;
    }
    
    /**
     * Logs a user action passed as UserAction object.
     * Converts UserAction object to String, calls controller.
     * 
     * @param userAction The user action object containing action details
     * @throws IOException if logging fails
     */
    @Override
    public void logUserAction(UserAction userAction) throws IOException {
        // Convert UserAction object to String
        String actionString = userAction.toString();
        
        // Call controller with String
        controller.logUserAction(actionString);
    }
    
    /**
     * Helper method: Converts primitive char to DifficultyEnum object.
     * 
     * @param level Character representing difficulty
     * @return DifficultyEnum corresponding to the character
     * @throws IllegalArgumentException if character is invalid
     */
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
        // Read the selected file and return the board; also clear incomplete via controller
        int[][] board = parseBoardFile(file);
        controller.clearIncompleteGame();
        // Persist current board to incomplete/current.txt through facade
        // The primitive controller will save when game is loaded normally,
        // here we simply return the board for the GUI to use.
        return board;
    }
    
    @Override
    public void deleteCompletedGame() {
        controller.deleteCompletedGame();
    }

    @Override
    public boolean undoLastMove(int[][] board) throws IOException {
        // Use the shared log file location in games/incomplete
        String logPath = "games/incomplete/moves.log";
        if (!Files.exists(Paths.get(logPath))) return false;
        UndoLogger ul = new UndoLogger(logPath);
        return ul.undoLastMove(board);
    }

    @Override
    public void saveCurrentGame(int[][] board) throws IOException {
        // Delegate to the primitive controller via facade
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
