package nl.abn.assignment.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngredientsDto {

    @Schema(description = "Ingredient Name", example = "Salt")
    private String ingredientName;

    @Schema(description = "Quantity of the Recipe", example = "1 kg")
    private String quantity;
}
