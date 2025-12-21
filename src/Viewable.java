/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author monic
 */
import java.io.IOException;

public interface Viewable {
    
    Catalog getCatalog();
    Game getGame(DifficultyEnum level) throws NotFoundException;
    void driveGames(Game sourceGame) throws SolutionInvalidException;
    
    String verifyGame(Game game);
    
    
    int[] solveGame(Game game) throws InvalidGameException;
    
    
    void logUserAction(String userAction) throws IOException;
}