/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author monic
 */
import java.io.IOException;
public interface Controllable {
    boolean[] getCatalog();
    int[][] getGame(char level) throws NotFoundException;
    void driveGames(String sourcePath) throws SolutionInvalidException;
    boolean[][] verifyGame(int[][] game);
    int[][] solveGame(int[][] game) throws InvalidGameException;
    void logUserAction(UserAction userAction) throws IOException;
}