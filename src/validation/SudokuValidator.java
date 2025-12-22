/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package validation;

import model.*;
/**
 *
 *  @author kiro sherif
 */
public interface SudokuValidator {
    ValidationResult validate(int[][] board);
}