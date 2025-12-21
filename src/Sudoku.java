

public class Sudoku {

    public static void main(String[] args) {

        if (args == null || args.length == 0) {
            System.out.println("No arguments provided!");
            System.out.println("Usage: java Sudoku <board.csv>");
            return;
        }

        String filePath = args[0];
        int[][] board;

        try {
            board = SudokuBoardManager.loadBoard(filePath);
        } catch (Exception e) {
            System.out.println("Failed to load board: " + e.getMessage());
            return;
        }

        // ======================================================
        //  PRINT THE BOARD
        // ======================================================
        BoardPrinter.print(board);

        // ======================================================
        //  RUN SEQUENTIAL VALIDATION (ONLY MODE IN LAB 10)
        // ======================================================
        System.out.println(" Running Sudoku Validator (Sequential Mode)...");

        SudokuValidator validator = SequentialValidator.getInstance();
        ValidationResult result = validator.validate(board);

        // ======================================================
        //  PRINT RESULT
        // ======================================================
        System.out.println(result.formatResult());
    }
}
