package utils;

import core.SudokuValidator;
import modes.SequentialValidator;
import modes.Mode3Validator;
import modes.Mode27Validator;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author User
 */
public class ValidatorFactory {
    public static SudokuValidator get(int mode) {
        return switch (mode) {
            case 0 -> new SequentialValidator();
            case 3 -> new Mode3Validator();
            case 27 -> new Mode27Validator();
            default -> throw new IllegalArgumentException("Invalid mode");
        };
    }
}
