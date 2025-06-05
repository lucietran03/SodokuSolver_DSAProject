package sudoku.common;

public class InputValidator {

    /**
     * Check Sudoku input and report detailed errors if invalid.
     *
     * @param input String to check
     * @throws IllegalArgumentException if input is invalid
     */
    public static void validateInput(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Input is null.");
        }

        if (input.isEmpty()) {
            throw new IllegalArgumentException("Input is empty. Expected 81 characters.");
        }

        if (input.length() != 81) {
            throw new IllegalArgumentException(
                    "Invalid input length: got " + input.length() + ", expected 81.");
        }

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c < '0' || c > '9') {
                throw new IllegalArgumentException(
                        "Invalid character at position " + i + ": '" + c + "'. Only digits 0-9 are allowed.");
            }
        }
    }
}
