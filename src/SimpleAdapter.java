import java.io.IOException;

public class SimpleAdapter implements SimpleControllable {
    private final SimpleViewable controller;
    
    public SimpleAdapter(SimpleViewable controller) {
        this.controller = controller;
    }
    
    @Override
    public boolean[] getCatalog() {
        Catalog catalog = controller.getCatalog();
        boolean[] result = new boolean[2];
        result[0] = catalog.hasUnfinished();
        result[1] = catalog.areModesReady();
        return result;
    }
    
    @Override
    public int[][] getGame(char level) throws NotFoundException {
        DifficultyEnum difficulty = charToDifficulty(level);
        Game game = controller.getGame(difficulty);
        return game.getBoard();
    }
    
    @Override
    public boolean[][] verifyGame(int[][] game) {
        Game gameObj = new Game(game, DifficultyEnum.EASY);
        String verificationResult = controller.verifyGame(gameObj);
        boolean[][] result = new boolean[9][9];
        boolean isValid = verificationResult.equals("VALID");
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                result[i][j] = isValid;
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
            case 'E': return DifficultyEnum.EASY;
            case 'M': return DifficultyEnum.MEDIUM;
            case 'H': return DifficultyEnum.HARD;
            default: throw new IllegalArgumentException("Invalid difficulty level: " + level);
        }
    }
}
