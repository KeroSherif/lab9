/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package core;
/**
 *
 * @author kiro sherif
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ValidationResult {

    private boolean valid;
    private final List<String> errors;

    public ValidationResult(boolean valid, List<String> errors) {
        this.valid = valid;
        // MAKE THREAD SAFE
        this.errors = Collections.synchronizedList(errors);
    }

    public synchronized void addError(String error) {
        this.errors.add(error);
        this.valid = false;
    }

    public boolean isValid() {
        return this.errors.isEmpty();
    }

    public List<String> getErrors() {
        return this.errors;
    }
}
