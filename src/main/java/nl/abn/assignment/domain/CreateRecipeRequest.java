package nl.abn.assignment.domain;

import java.util.Set;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.abn.assignment.validator.RecipeTypePattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRecipeRequest {
    @NotEmpty(message = "{recipe.title.empty.validation.message}")
    @Size(min = 5, max = 40, message = "{recipe.title.size.validation.message}")
    @Schema(description = "Title of the recipe", example = "my title", required = true)
    private String name;

    @RecipeTypePattern(anyOf = {RecipeType.RELIGIOUS, RecipeType.VEGAN, RecipeType.GLUTEN_FREE, RecipeType.VEGETARIAN, RecipeType.NON_VEGETARIAN},
    message = "{recipe.type.validation.message}")
    @Schema(description = "Type of the Recipe", example = "VEGETARIAN", required = true)
    private RecipeType recipeType;

    @Min(value = 1, message = "{recipe.servings.empty.validation.message}")
    @Schema(description = "No of Servings", example = "3", required = true)
    private Integer servings;

    private Set<IngredientsDto> ingredients;

    private Set<InstructionDto> instructions;
}
