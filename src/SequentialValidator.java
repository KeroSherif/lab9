import java.util.*;

public class SequentialValidator implements SudokuValidator {
    @Override
    public ValidationResult validate(int[][] board) {
        List<String> errors = new ArrayList<>();

        // Rows
        for (int i = 0; i < 9; i++) {
            int[] row = board[i];
            List<Integer> duplicates = findDuplicates(row);
            for (int digit : duplicates) {
                List<Integer> positions = new ArrayList<>();
                for (int col = 0; col < 9; col++) {
                    if (row[col] == digit) {
                        positions.add(col + 1); // 1-based
                    }
                }
                errors.add(String.format("ROW %d,#%d,[%s]", i + 1, digit, listToString(positions)));
            }
        }

        // Columns
        for (int j = 0; j < 9; j++) {
            int[] col = new int[9];
            for (int i = 0; i < 9; i++) col[i] = board[i][j];
            List<Integer> duplicates = findDuplicates(col);
            for (int digit : duplicates) {
                List<Integer> positions = new ArrayList<>();
                for (int i = 0; i < 9; i++) {
                    if (board[i][j] == digit) {
                        positions.add(i + 1);
                    }
                }
                errors.add(String.format("COL %d,#%d,[%s]", j + 1, digit, listToString(positions)));
            }
        }

        // Boxes
        for (int boxIndex = 0; boxIndex < 9; boxIndex++) {
            int startRow = (boxIndex / 3) * 3;
            int startCol = (boxIndex % 3) * 3;
            int[] box = new int[9];
            int idx = 0;
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    box[idx++] = board[startRow + r][startCol + c];
                }
            }
            List<Integer> duplicates = findDuplicates(box);
            for (int digit : duplicates) {
                List<Integer> positions = new ArrayList<>();
                idx = 0;
                for (int r = 0; r < 3; r++) {
                    for (int c = 0; c < 3; c++) {
                        if (board[startRow + r][startCol + c] == digit) {
                            positions.add(idx + 1); // 1–9 in box (row-major)
                        }
                        idx++;
                    }
                }
                errors.add(String.format("BOX %d,#%d,[%s]", boxIndex + 1, digit, listToString(positions)));
            }
        }

        boolean valid = errors.isEmpty();
        return new ValidationResult(valid, errors);
    }

    private List<Integer> findDuplicates(int[] arr) {
        int[] count = new int[10];
        for (int val : arr) {
            if (val >= 1 && val <= 9) count[val]++;
        }
        List<Integer> dup = new ArrayList<>();
        for (int d = 1; d <= 9; d++) {
            if (count[d] > 1) dup.add(d);
        }
        return dup;
    }

    private String listToString(List<Integer> list) {
        return String.join(",", list.stream().map(String::valueOf).toArray(String[]::new));
    }
}