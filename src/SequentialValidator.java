

public class SequentialValidator implements SudokuValidator {

    private static SequentialValidator instance;

    private SequentialValidator() {}

    public static SequentialValidator getInstance() {
        if (instance == null)
            instance = new SequentialValidator();
        return instance;
    }

    @Override
    public ValidationResult validate(int[][] board) {

        ValidationResult result = new ValidationResult();

        // Check rows
        for (int r = 0; r < 9; r++) {
            new RowChecker(board, r, result).run();
        }

        // Check columns
        for (int c = 0; c < 9; c++) {
            new ColumnChecker(board, c, result).run();
        }

        // Check boxes
        for (int b = 0; b < 9; b++) {
            new BoxChecker(board, b, result).run();
        }

        // Check for incomplete (zeros)
        if (result.getState() != ValidationResult.State.INVALID) {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (board[i][j] == 0) {
                        result.markIncomplete();
                        return result;
                    }
                }
            }
        }

        return result;
    }
}
