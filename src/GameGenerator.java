/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab9;
import java.util.Random;
import java.io.File;
/**
 *
 * @author DANAH
 */
public class GameGenerator {
    private SudokuVerifier verifier = new SudokuVerifier();
    private StorageManager storage = new StorageManager();
    
 public void generateLevels(String sourcePath) throws SolutionInvalidException {
        int[][] solvedBoard = loadBoardFromFile(sourcePath);   
    
    if (!verifier.verify(solvedBoard).equals("VALID")) {
            throw new SolutionInvalidException("The source file is not a valid solved Sudoku!");
        }
    
    Random random = new Random(System.currentTimeMillis());
    
    saveLevel(solvedBoard, 10, DifficultyEnum.EASY, random);
        saveLevel(solvedBoard, 20, DifficultyEnum.MEDIUM, random);
        saveLevel(solvedBoard, 25, DifficultyEnum.HARD, random);
    }
 
 
}
