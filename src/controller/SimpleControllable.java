package controller;
import exceptions.InvalidGameException;
import exceptions.NotFoundException;
import java.io.IOException;
import model.UserAction;

public interface SimpleControllable {
    boolean[] getCatalog();
    int[][] getGame(char level) throws NotFoundException;
    boolean[][] verifyGame(int[][] game);
    int[][] solveGame(int[][] game) throws InvalidGameException;
    void logUserAction(UserAction userAction) throws IOException;
}
