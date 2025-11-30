package utils;

import core.SudokuValidator;
import modes.SequentialValidator;
import modes.Mode3Validator;
import modes.Mode27Validator;

public class ValidatorFactory {

    public static SudokuValidator get(int mode) {
        return switch (mode) {
            case 0 -> SequentialValidator.getInstance();
            case 3 -> Mode3Validator.getInstance();
            case 27 -> Mode27Validator.getInstance();
            default -> throw new IllegalArgumentException("Invalid Mode");
        };
    }
}
