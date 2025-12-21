
import java.io.File;
import java.io.IOException;

public interface Controllable {

    boolean[] getCatalog();

    int[][] getGame(char level) throws NotFoundException;

    int[][] getRandomGame(char level) throws Exception;

    void driveGames(String sourcePath) throws SolutionInvalidException;

    boolean[][] verifyGame(int[][] game);

    int[][] solveGame(int[][] game) throws InvalidGameException;

    void logUserAction(UserAction userAction) throws IOException;

    void clearIncompleteGame();

    int[][] loadSelectedGame(File file) throws Exception;

}
