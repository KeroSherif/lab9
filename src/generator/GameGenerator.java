/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package generator;

import model.*;

import java.util.*;

import java.io.File;
import java.util.Random;
import storage.StorageManager;
import storage.SudokuBoardManager;
import validation.SudokuVerifier;

/**
 *
 * @author DANAH
 */
public class GameGenerator {

    private SudokuVerifier verifier = new SudokuVerifier();
    private StorageManager storage = new StorageManager();

    public class SolutionInvalidException extends Exception {

        public SolutionInvalidException(String msg) {
            super(msg);
        }
    }

    public void generateLevels(String sourcePath) throws SolutionInvalidException {

        int[][] solvedBoard = loadBoardFromFile(sourcePath);

        String state = verifier.verify(solvedBoard);

        
        if (!state.equals("VALID")) {
            throw new SolutionInvalidException(
                    "Source Sudoku must be COMPLETE and VALID. Found: " + state
            );
        }

        
        // Use RandomPairs class as required by Lab 10 specifications
        RandomPairs randomPairs = new RandomPairs();

        saveLevel(solvedBoard, 10, DifficultyEnum.EASY, randomPairs);
        saveLevel(solvedBoard, 20, DifficultyEnum.MEDIUM, randomPairs);
        saveLevel(solvedBoard, 25, DifficultyEnum.HARD, randomPairs);
    }

    private void saveLevel(int[][] solved, int cellsToRemove,
            DifficultyEnum diff, RandomPairs randomPairs) {

        int[][] puzzle = copyBoard(solved);
        
        // Generate distinct random pairs using RandomPairs class
        var pairs = randomPairs.generateDistinctPairs(cellsToRemove);
        
        for (int[] pair : pairs) {
            int r = pair[0];
            int c = pair[1];
            puzzle[r][c] = 0;
        }

        storage.saveDifficultyFile(puzzle, diff);
    }

    private int[][] copyBoard(int[][] original) {
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, 9);
        }
        return copy;
    }

    private int[][] loadBoardFromFile(String path) {
        try {
            return SudokuBoardManager.loadBoard(path);
        } catch (Exception e) {
            System.err.println("Error loading board: " + e.getMessage());
            return new int[9][9];
        }
    }
}
