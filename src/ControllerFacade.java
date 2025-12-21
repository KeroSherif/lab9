/*
 * ControllerFacade - The Controller Layer Implementation
 * 
 * This class implements the Viewable interface and represents the Controller side
 * of the MVC architecture. It works with Object types (Game, Catalog, DifficultyEnum).
 * 
 * Role: Business logic layer that manages game operations using rich domain objects.
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ControllerFacade implements Viewable {
    private final SudokuController primitiveController;

    public ControllerFacade() {
        this.primitiveController = new SudokuController();
    }
    
    /**
     * Retrieves the catalog information containing game status metadata.
     * 
     * @return Catalog object with current status and mode availability
     */
    @Override
    public Catalog getCatalog() {
        boolean[] flags = primitiveController.getCatalog();
        // flags[0] = has unfinished, flags[1] = all modes exist
        return new Catalog(flags[0], flags[1]);
    }
    
    /**
     * Retrieves a game board for the specified difficulty level.
     * 
     * @param level The difficulty level (EASY, MEDIUM, HARD)
     * @return Game object containing the board for the requested difficulty
     * @throws NotFoundException if no game exists for the specified level
     */
    @Override
    public Game getGame(DifficultyEnum level) throws NotFoundException {
        char ch = switch (level) {
            case EASY -> 'e';
            case MEDIUM -> 'm';
            case HARD -> 'h';
            case INCOMPLETE -> 'c';
        };
        int[][] board = primitiveController.getGame(ch);
        return new Game(board, level);
    }
    
    /**
     * Drives the game generation process from a source game.
     * 
     * @param sourceGame The source game to generate from
     * @throws SolutionInvalidException if the source game solution is invalid
     */
    @Override
    public void driveGames(Game sourceGame) throws SolutionInvalidException {
        // Write the provided solved board to a temp file and delegate
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
    
    /**
     * Verifies if a game board is valid according to Sudoku rules.
     * 
     * @param game The game to verify
     * @return String description of verification result (e.g., "VALID", "INVALID: reason")
     */
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
    
    /**
     * Solves the given game and returns the solution as a flat array.
     * 
     * @param game The game to solve
     * @return int[] array containing the solved board (flattened)
     * @throws InvalidGameException if the game cannot be solved
     */
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
    
    /**
     * Logs a user action to persistent storage.
     * 
     * @param userAction String representation of the user action
     * @throws IOException if logging fails
     */
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
}
