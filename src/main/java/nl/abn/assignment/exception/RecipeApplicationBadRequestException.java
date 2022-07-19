package nl.abn.assignment.exception;

public class RecipeApplicationBadRequestException extends RuntimeException{

    public RecipeApplicationBadRequestException(String message) {
        super(message);
    }

    public RecipeApplicationBadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
