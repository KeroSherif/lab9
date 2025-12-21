/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author kiro shrerif
 */
public class TestSolverMain {

    public static void main(String[] args) {

        int[][] board = {
            {5, 3, 0, 0, 7, 8, 9, 1, 2},
            {6, 7, 2, 1, 9, 5, 3, 4, 8},
            {1, 9, 8, 3, 4, 2, 5, 6, 7},
            {8, 5, 9, 7, 6, 1, 4, 2, 3},
            {4, 2, 6, 8, 0, 3, 7, 9, 1},
            {7, 1, 3, 9, 2, 4, 8, 5, 6},
            {9, 6, 1, 5, 3, 7, 2, 8, 4},
            {2, 8, 7, 4, 1, 9, 6, 3, 5},
            {3, 4, 5, 2, 0, 6, 1, 7, 0}
        };

        // must be 5 empty cells
        System.out.println("Testing Sequential Solver...");
        int[] sol1 = SudokuSolver.solve(board);

        if (sol1 != null) {
            printSolution(sol1);
        } else {
            System.out.println("No solution found (Sequential)");
        }

        System.out.println("\nTesting Multi-Threaded Solver...");
        int[] sol2 = MultiThreadedSudokuSolver.solve(board);

        if (sol2 != null) {
            printSolution(sol2);
        } else {
            System.out.println("No solution found (Threaded)");
        }
    }

    private static void printSolution(int[] sol) {
        System.out.print("Solution values: ");
        for (int v : sol) {
            System.out.print(v + " ");
        }
        System.out.println();
    }
}
