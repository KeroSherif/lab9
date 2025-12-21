/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab9;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
/**
 *
 * @author DANAH
 */
public class GameGenerator {
    private  SudokuVerifier verifier = new SudokuVerifier();
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
 
 private void saveLevel(int[][] solved, int cellsToRemove, DifficultyEnum diff, Random rand) {
        int[][] puzzle = copyBoard(solved);
        int removed = 0;

        while (removed < cellsToRemove) {
            int r = rand.nextInt(9);
            int c = rand.nextInt(9);
            if (puzzle[r][c] != 0) {
                puzzle[r][c] = 0; 
                removed++;
            }
        }
        
     storage.saveDifficultyFile(puzzle, diff);
    }
 
 public void saveDifficultyFile(int[][] board, DifficultyEnum diff) {
    String folderPath;

    switch (diff) {
        case EASY:
            folderPath = "easy/";
            break;
        case MEDIUM:
            folderPath = "medium/";
            break;
        case HARD:
            folderPath = "hard/";
            break;
        default:
            folderPath = "easy/"; 
    }
    File dir = new File(folderPath);
    if (!dir.exists()) {
        dir.mkdirs();
    }
    File targetFile = new File(dir, "puzzle.txt");
    
    try (PrintWriter writer = new PrintWriter(new FileWriter(targetFile))) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                writer.print(board[r][c]);
                // Add a space between numbers, but not after the last one
                if (c < 8) writer.print(" ");
            }
 
            writer.println();
        }
        System.out.println("Successfully saved " + diff + " puzzle to " + targetFile.getPath());
    } catch (IOException e) {
        System.err.println("Error saving difficulty file: " + e.getMessage());
    }
}
 private int[][] copyBoard(int[][] original) {
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++) System.arraycopy(original[i], 0, copy[i], 0, 9);
        return copy;
    }
    
    private int[][] loadBoardFromFile(String path) {
        return new int[9][9]; 
    }
}
 

