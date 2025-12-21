/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.io.File;
import java.util.Random;

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

        
        Random random = new Random(System.currentTimeMillis());

        saveLevel(solvedBoard, 10, DifficultyEnum.EASY, random);
        saveLevel(solvedBoard, 20, DifficultyEnum.MEDIUM, random);
        saveLevel(solvedBoard, 25, DifficultyEnum.HARD, random);
    }

    private void saveLevel(int[][] solved, int cellsToRemove,
            DifficultyEnum diff, Random rand) {

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
