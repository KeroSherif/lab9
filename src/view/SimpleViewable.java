package view;
import exceptions.InvalidGameException;
import exceptions.NotFoundException;
import java.io.IOException;
import model.Catalog;
import model.DifficultyEnum;
import model.Game;

public interface SimpleViewable {
    Catalog getCatalog();
    Game getGame(DifficultyEnum level) throws NotFoundException;
    String verifyGame(Game game);
    int[] solveGame(Game game) throws InvalidGameException;
    void logUserAction(String userAction) throws IOException;
    void clearIncompleteGame();
    void deleteCompletedGame();
}
