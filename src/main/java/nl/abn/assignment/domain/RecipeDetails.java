package nl.abn.assignment.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDetails {

    @Schema(description = "Identifier of the recipe", example = "1", required = true)
    private String id;

    @Schema(description = "Name of the recipe", example = "White Sauce Pasta", required = true)
    private String name;

    @Schema(description = "No of Servings", example = "3", required = true)
    private Integer servings;

    @Schema(description = "Type of the recipe", example = "Vegan", required = true)
    private RecipeType recipeType;

    private Set<InstructionDto> instructions;

    private Set<IngredientsDto> ingredients;
}
