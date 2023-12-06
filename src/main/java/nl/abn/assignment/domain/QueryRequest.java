package nl.abn.assignment.domain;

import java.util.List;

import org.springframework.lang.Nullable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * Query parameters
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryRequest {

    @Nullable
    @Schema(description = "Recipe Type to filter", example = "VEGETARIAN")
    private RecipeType recipeType;

    @Nullable
    @Schema(description = "No of servings", example = "2")
    private Integer noOfServings;

    @Schema(description = "Identifier to check if the check on ingredients with excluded on the Recipe", example = "true", defaultValue = "false")
    private boolean excludeIngredients;

    @Nullable
    @Schema(description = "List of Ingredients to be either present or not on the Recipe", example = "Salt, Chicken")
    private List<String> ingredients;

    @Nullable
    @Schema(description = "List of Instructions to be searched on the  recipe.", example = "Add Salt")
    private List<String> instructions;
}
