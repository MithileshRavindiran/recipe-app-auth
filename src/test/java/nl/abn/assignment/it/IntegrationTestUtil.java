package nl.abn.assignment.it;

import nl.abn.assignment.entities.Ingredients;
import nl.abn.assignment.entities.Instruction;
import nl.abn.assignment.entities.Recipe;
import nl.abn.assignment.entities.RecipeType;
import nl.abn.assignment.repository.RecipeRepository;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;

public final class IntegrationTestUtil {
    private IntegrationTestUtil() {
        // intentionally empty
    }

    /**
     * Fill table with dummy data to query
     * 
     * @param recipeRepository repository where data needs to be stored
     */
    public static void initTable(RecipeRepository recipeRepository) {
        Recipe entity1 = Recipe.builder().id("62d2198c85ca871f9fc45c35")
                .name("My Recipe 1")
                .recipeType(RecipeType.VEGAN)
                .servings(4)
                .instructions(Set.of(Instruction.builder()
                        .instruction("Instruction 1")
                        .instructionNo("Step No1").build(),
                        Instruction.builder()
                                .instruction("Instruction 2")
                                .instructionNo("Step No2")
                                .build()
                        ))
                .ingredients(Set.of(Ingredients.builder()
                                        .ingredientName("Salt")
                                        .quantity("100 gm")
                                        .build(),
                                Ingredients.builder()
                                        .ingredientName("Sugar")
                                        .quantity("300 gm")
                                        .build()))
              .build();
        recipeRepository.save(entity1);

        Recipe entity2 = Recipe.builder().id("62d2198c85ca871f9fc45c36")
                .name("My Recipe 2")
                .recipeType(RecipeType.VEGETARIAN)
                .servings(3)
                .instructions(Set.of(Instruction.builder()
                                .instruction("Instruction 1")
                                .instructionNo("Step No1").build(),
                        Instruction.builder()
                                .instruction("Instruction 2")
                                .instructionNo("Step No2")
                                .build()
                ))
                .ingredients(Set.of(Ingredients.builder()
                                .ingredientName("Salt")
                                .quantity("100 gm")
                                .build(),
                        Ingredients.builder()
                                .ingredientName("Sugar")
                                .quantity("300 gm")
                                .build()))
                .build();
        recipeRepository.save(entity2);

        Recipe entity3 = Recipe.builder().id("62d2198c85ca871f9fc45c37")
                .name("My Recipe 3")
                .recipeType(RecipeType.NON_VEGETARIAN)
                .servings(1)
                .instructions(Set.of(Instruction.builder()
                                .instruction("Instruction 1")
                                .instructionNo("Step No1").build(),
                        Instruction.builder()
                                .instruction("Instruction 2")
                                .instructionNo("Step No2")
                                .build()
                ))
                .ingredients(Set.of(Ingredients.builder()
                                .ingredientName("Salt")
                                .quantity("100 gm")
                                .build(),
                        Ingredients.builder()
                                .ingredientName("Sugar")
                                .quantity("300 gm")
                                .build()))
                .build();
        recipeRepository.save(entity3);

        Recipe entity4 = Recipe.builder().id("62d2198c85ca871f9fc45c38")
                .name("My Recipe 4")
                .recipeType(RecipeType.RELIGIOUS)
                .servings(2)
                .instructions(Set.of(Instruction.builder()
                                .instruction("Instruction 1")
                                .instructionNo("Step No1").build(),
                        Instruction.builder()
                                .instruction("Instruction 2")
                                .instructionNo("Step No2")
                                .build()
                ))
                .ingredients(Set.of(Ingredients.builder()
                                .ingredientName("Salt")
                                .quantity("100 gm")
                                .build(),
                        Ingredients.builder()
                                .ingredientName("Sugar")
                                .quantity("300 gm")
                                .build()))
                .build();
        recipeRepository.save(entity3);
    }


}
