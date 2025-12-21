/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */



/**
 *
 * @author kirol sherif
 */


public class BoardPrinter {

    // Enable color or disable
    private static final boolean COLORS = true;

    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String WHITE = "\u001B[37m";

    public static void print(int[][] board) {

        System.out.println();
        System.out.println("======= SUDOKU BOARD =======");

        for (int row = 0; row < 9; row++) {

            if (row % 3 == 0)
                System.out.println("+-------+-------+-------+");

            for (int col = 0; col < 9; col++) {

                if (col % 3 == 0)
                    System.out.print("| ");

                int val = board[row][col];

                // Optional styling
                if (COLORS)
                    System.out.print(CYAN + val + RESET + " ");
                else
                    System.out.print(val + " ");
            }

            System.out.println("|");
        }

        System.out.println("+-------+-------+-------+");
        System.out.println();
    }
}
