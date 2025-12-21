

import java.util.*;

public class ValidationResult {

    public enum State {
        VALID,
        INVALID,
        INCOMPLETE
    }

    private State state;
    private final List<String> errors;

    public ValidationResult() {
        this.state = State.VALID;
        this.errors = new ArrayList<>();
    }

    public void addError(String err) {
        errors.add(err);
        state = State.INVALID;
    }

    public void markIncomplete() {
        if (state == State.VALID) {
            state = State.INCOMPLETE;
        }
    }

    public State getState() {
        return state;
    }

    public List<String> getErrors() {
        return errors;
    }

    public boolean isValid() {
        return state == State.VALID;
    }

    public String formatResult() {
        if (state == State.VALID)
            return "VALID";

        if (state == State.INCOMPLETE)
            return "INCOMPLETE";

        StringBuilder sb = new StringBuilder();
        sb.append("INVALID\n");
        for (String e : errors) {
            sb.append(e).append("\n");
        }
        return sb.toString();
    }
}
