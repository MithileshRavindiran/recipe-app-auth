package nl.abn.assignment.validator;


import nl.abn.assignment.domain.RecipeType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class RecipeTypeValidator implements ConstraintValidator<RecipeTypePattern, RecipeType> {

    private nl.abn.assignment.domain.RecipeType[] subset;

    @Override
    public void initialize(RecipeTypePattern constraint) {
        this.subset = constraint.anyOf();
    }

    @Override
    public boolean isValid(nl.abn.assignment.domain.RecipeType value, ConstraintValidatorContext context) {
        return value != null || Arrays.asList(subset).contains(value);
    }

}
