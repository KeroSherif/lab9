package core;

import java.util.*;
import java.util.stream.Collectors;

public class ValidationResult {

    private boolean valid;
    private final List<String> errors;

    // ============================
    //  MAIN CONSTRUCTOR
    // ============================
    public ValidationResult(boolean valid, List<String> errors) {
        this.valid = valid;
        this.errors = Collections.synchronizedList(errors);
    }

    // ============================
    //  SHORTCUT CONSTRUCTOR
    //  (Allows: new ValidationResult(true))
    // ============================
    public ValidationResult(boolean valid) {
        this(valid, new ArrayList<>());
    }

    // ============================
    //  ADD ERROR
    // ============================
    public synchronized void addError(String err) {
        this.errors.add(err);
        this.valid = false;
    }

    public boolean isValid() {
        return errors.isEmpty();
    }

    public List<String> getErrors() {
        return errors;
    }

    // ============================
    //   GROUPED FORMATTED OUTPUT
    // ============================
    public String formatGrouped() {

        if (errors.isEmpty())
            return "VALID";

        // Group errors by type (ROW / COL / BOX)
        Map<String, List<String>> groups = errors.stream()
                .collect(Collectors.groupingBy(err -> err.split(" ")[0]));

        StringBuilder sb = new StringBuilder();
        sb.append("INVALID\n\n");

        // ROWS
        if (groups.containsKey("ROW")) {
            sb.append("ROWS:\n");
            for (String e : groups.get("ROW"))
                sb.append(e).append("\n");
            sb.append("\n");
        }

        // COLUMNS
        if (groups.containsKey("COL")) {
            sb.append("COLUMNS:\n");
            for (String e : groups.get("COL"))
                sb.append(e).append("\n");
            sb.append("\n");
        }

        // BOXES
        if (groups.containsKey("BOX")) {
            sb.append("BOXES:\n");
            for (String e : groups.get("BOX"))
                sb.append(e).append("\n");
        }

        return sb.toString();
    }
}
