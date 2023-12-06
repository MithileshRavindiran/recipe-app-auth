package nl.abn.assignment.validator;


import java.util.Arrays;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import nl.abn.assignment.domain.RecipeType;

public class RecipeTypeValidator implements ConstraintValidator<RecipeTypePattern, RecipeType> {

    private RecipeType[] subset;

    @Override
    public void initialize(RecipeTypePattern constraint) {
        this.subset = constraint.anyOf();
    }

    @Override
    public boolean isValid(RecipeType value, ConstraintValidatorContext context) {
        return value != null || Arrays.asList(subset).contains(value);
    }

}
