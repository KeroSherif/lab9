/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author kiro sherif
 */
public class SudokuSolver {

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
            throw new IllegalStateException("Solver requires exactly 5 empty cells");

        PermutationIterator it = new PermutationIterator(5);
        FlyweightVerifier verifier = new FlyweightVerifier(board, empty);

        while (it.hasNext()) {
            int[] candidate = it.next();
            if (verifier.isValid(candidate))
                return candidate;
        }

        return null;
    }
}
