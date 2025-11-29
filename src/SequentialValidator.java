/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author DANAH
 */
interface Validator {
    void validate();
    boolean isValid();
    void printReport();
}

public class SequentialValidator implements Validator {

    private final int[][] board;       
    private final List<String> errors; 
    private boolean valid = true;     

    public SequentialValidator(int[][] board) {
        this.board = board;
        this.errors = new ArrayList<>();
    }

   