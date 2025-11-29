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
    
    @Override
    public void validate() {
        checkRows();
        checkColumns();
        checkBoxes();
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public void printReport() {
        if (valid) {
            System.out.println("VALID");
        } else {
            System.out.println("INVALID");
            System.out.println("Details:");
            errors.forEach(System.out::println);
        }
    }

   