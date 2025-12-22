/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solver;

import model.*;
import exceptions.*;

/**
 *
 * @author kiro sherif
 */
import java.util.ArrayList;
import java.util.List;
import validation.FlyweightVerifier;

public class MultiThreadedSudokuSolver {

    private static final int WORKER_COUNT = 4;

    public static int[] solve(int[][] board) {

        int[][] empty = new int[5][2];
        int idx = 0;

        for (int i = 0; i < 9 && idx < 5; i++) {
            for (int j = 0; j < 9 && idx < 5; j++) {
                if (board[i][j] == 0) {
                    empty[idx][0] = i;
                    empty[idx][1] = j;
                    idx++;
                }
            }
        }

        if (idx != 5)
            throw new IllegalStateException(
                    "Threaded solver works only with exactly 5 empty cells");

        PermutationIterator iterator = new PermutationIterator(5);
        FlyweightVerifier verifier = new FlyweightVerifier(board, empty);
        SolutionBox solutionBox = new SolutionBox();

        List<Thread> workers = new ArrayList<>();

        while (iterator.hasNext() && !solutionBox.isSolved()) {

            workers.clear();

            for (int i = 0; i < WORKER_COUNT && iterator.hasNext(); i++) {
                int[] candidate = iterator.next();
                WorkerThread worker =
                        new WorkerThread(verifier, candidate, solutionBox);
                workers.add(worker);
                worker.start();
            }

            for (Thread t : workers) {
                try {
                    t.join();
                } catch (InterruptedException ignored) {}
            }
        }

        return solutionBox.getSolution();
    }

    // Fills the provided board in-place with the found solution values for the 5 empty cells.
    // Returns the mutated board if a solution is found; otherwise returns null.
    public static int[][] solveBoard(int[][] board) {
        int[][] empty = new int[5][2];
        int idx = 0;

        for (int i = 0; i < 9 && idx < 5; i++) {
            for (int j = 0; j < 9 && idx < 5; j++) {
                if (board[i][j] == 0) {
                    empty[idx][0] = i;
                    empty[idx][1] = j;
                    idx++;
                }
            }
        }

        if (idx != 5)
            throw new IllegalStateException(
                    "Threaded solver works only with exactly 5 empty cells");

        PermutationIterator iterator = new PermutationIterator(5);
        FlyweightVerifier verifier = new FlyweightVerifier(board, empty);
        SolutionBox solutionBox = new SolutionBox();

        List<Thread> workers = new ArrayList<>();

        while (iterator.hasNext() && !solutionBox.isSolved()) {

            workers.clear();

            for (int i = 0; i < WORKER_COUNT && iterator.hasNext(); i++) {
                int[] candidate = iterator.next();
                WorkerThread worker =
                        new WorkerThread(verifier, candidate, solutionBox);
                workers.add(worker);
                worker.start();
            }

            for (Thread t : workers) {
                try {
                    t.join();
                } catch (InterruptedException ignored) {}
            }
        }

        int[] sol = solutionBox.getSolution();
        if (sol == null) return null;

        // Apply solution into the board at the recorded empty positions
        for (int k = 0; k < 5; k++) {
            int r = empty[k][0];
            int c = empty[k][1];
            board[r][c] = sol[k];
        }

        return board;
    }
}

