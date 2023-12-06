package nl.abn.assignment.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.validation.FieldError;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.ConstraintViolation;
import lombok.Builder;
import lombok.Data;

/**
 * DTO containing error information.
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private final String code;
    private final String message;
    private  final List<FieldError> fields = new ArrayList<>();
    private  final List<ErrorResponse> subErrors = new ArrayList<>();

    public void addValidationErrors(Set<ConstraintViolation<?>> constraintViolations) {
        constraintViolations.forEach(this::addValidationError);
    }

    /**
     * Utility method for adding error of ConstraintViolation. Usually when
     * a @Validated validation fails.
     *
     * @param cv the ConstraintViolation
     */
    private void addValidationError(ConstraintViolation<?> cv) {
        this.addValidationError(
                cv.getRootBeanClass().getSimpleName(),
                ((PathImpl) cv.getPropertyPath()).getLeafNode().asString(),
                cv.getMessage());
    }

    private void addValidationError(String object, String field, String message) {
        fields.add(new FieldError(object, field, message));
    }

    private void addValidationError(FieldError fieldError) {
        fields.add(fieldError);
    }

    public void addValidationErrors(List<FieldError> fieldErrors) {
        fieldErrors.forEach(this::addValidationError);
    }

}
