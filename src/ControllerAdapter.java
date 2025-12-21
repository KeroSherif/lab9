/*
 * ControllerAdapter - The Adapter Pattern Implementation
 * 
 * This class implements the Controllable interface (View side) and adapts it to
 * work with the Viewable interface (Controller side). It acts as a bridge between
 * the primitive-based View layer and the Object-based Controller layer.
 * 
 * Role: Converts primitive types (int[][], char, boolean) to/from Objects
 * (Game, Catalog, DifficultyEnum) to enable communication between incompatible layers.
 */

import java.io.IOException;

public class ControllerAdapter implements Controllable {
    
    // Reference to the Controller (which implements Viewable)
    private final Viewable controller;
    
    /**
     * Constructs the adapter with a reference to the Controller.
     * 
     * @param controller The controller facade implementing Viewable interface
     */
    public ControllerAdapter(Viewable controller) {
        this.controller = controller;
    }
    
    /**
     * Gets catalog information as primitive boolean array.
     * Converts from Catalog object to boolean[].
     * 
     * @return boolean[] where [0] = hasUnfinished, [1] = allModesExist
     */
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
        // TODO: Load game from file path and convert to Game object
        // For now, create a dummy game to pass to controller
        int[][] dummyBoard = new int[9][9];
        Game sourceGame = new Game(dummyBoard, DifficultyEnum.EASY);
        
        // Call controller with object
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
        
        // Convert String result to primitive boolean[][]
        // Simplified: return all true if "VALID", all false otherwise
        boolean[][] result = new boolean[9][9];
        boolean isValid = verificationResult.equals("VALID");
        
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                result[i][j] = isValid;
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
        
        // Call controller with object (returns flat int[])
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
            default:
                throw new IllegalArgumentException("Invalid difficulty level: " + level);
        }
    }

    @Override
    public int[][] getRandomGame(char level) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void clearIncompleteGame() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
