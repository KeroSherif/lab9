import java.io.IOException;

public interface SimpleViewable {
    Catalog getCatalog();
    Game getGame(DifficultyEnum level) throws NotFoundException;
    String verifyGame(Game game);
    int[] solveGame(Game game) throws InvalidGameException;
    void logUserAction(String userAction) throws IOException;
    void clearIncompleteGame();
    void deleteCompletedGame();
}
