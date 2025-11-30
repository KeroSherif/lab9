import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class Mode3Validator implements SudokuValidator {
    @Override
    public ValidationResult validate(int[][] board) {
        List<String> initialErrors = new ArrayList<>();
        ValidationResult initialResult = new ValidationResult(true, initialErrors);
        final AtomicReference<ValidationResult> resultRef = new AtomicReference<>(initialResult);

        CountDownLatch latch = new CountDownLatch(3);

        Thread rowsThread = new Thread(() -> {
            for (int i = 0; i < 9; i++) {
                new RowChecker(board, i, resultRef.get()).run();
            }
            latch.countDown();
        });

        Thread colsThread = new Thread(() -> {
            for (int i = 0; i < 9; i++) {
                new ColumnChecker(board, i, resultRef.get()).run();
            }
            latch.countDown();
        });

        Thread boxesThread = new Thread(() -> {
            for (int i = 0; i < 9; i++) {
                new BoxChecker(board, i, resultRef.get()).run();
            }
            latch.countDown();
        });

        rowsThread.start();
        colsThread.start();
        boxesThread.start();

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (!resultRef.get().getErrors().isEmpty()) {
            resultRef.set(new ValidationResult(false, resultRef.get().getErrors()));
        }
        return resultRef.get();
    }
}             

























