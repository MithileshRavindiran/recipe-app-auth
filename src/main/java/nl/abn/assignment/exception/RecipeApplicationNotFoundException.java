package nl.abn.assignment.exception;

public class RecipeApplicationNotFoundException extends RuntimeException{

    public RecipeApplicationNotFoundException(String message) {
        super(message);
    }

    public RecipeApplicationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
