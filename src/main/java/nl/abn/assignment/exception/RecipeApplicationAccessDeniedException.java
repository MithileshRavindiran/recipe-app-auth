package nl.abn.assignment.exception;

public class RecipeApplicationAccessDeniedException extends RuntimeException{

    public RecipeApplicationAccessDeniedException(String message) {
        super(message);
    }

    public RecipeApplicationAccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}
