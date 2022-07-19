package nl.abn.assignment.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@AllArgsConstructor
@Data
@Builder
public class Ingredients {

    private String ingredientName;

    private String quantity;
}
