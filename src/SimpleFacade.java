import java.io.IOException;

public class SimpleFacade implements SimpleViewable {
    @Override
    public Catalog getCatalog() {
        return new Catalog(false, true);
    }
    
    @Override
    public Game getGame(DifficultyEnum level) throws NotFoundException {
        int[][] dummyBoard = new int[9][9];
        return new Game(dummyBoard, level);
    }
    
    @Override
    public String verifyGame(Game game) {
        return "VALID";
    }
    
    @Override
    public int[] solveGame(Game game) throws InvalidGameException {
        return new int[81];
    }
    
    @Override
    public void logUserAction(String userAction) throws IOException {
        System.out.println("LOG: " + userAction);
    }
}
