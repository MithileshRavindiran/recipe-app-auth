package nl.abn.assignment.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import nl.abn.assignment.domain.CreateRecipeRequest;
import nl.abn.assignment.domain.IngredientsDto;
import nl.abn.assignment.domain.InstructionDto;
import nl.abn.assignment.domain.RecipeDetails;
import nl.abn.assignment.entities.Ingredients;
import nl.abn.assignment.entities.Instruction;
import nl.abn.assignment.entities.Recipe;
import nl.abn.assignment.entities.RecipeType;
import nl.abn.assignment.entities.User;

public class TestHelperUtil {

    public static List<Recipe> getListOfRecipeEntity() {
        List<Recipe> recipes = new ArrayList<>();
        Recipe recipe = Recipe.builder()
                .name("White Sauce Pasta")
                .recipeType(RecipeType.VEGAN)
                .servings(2)
                .createdBy(User.builder()
                  .username("userName")
                  .build())
                .instructions(Set.of(
                        Instruction.builder()
                                .instruction("Boil Water")
                                .instructionNo("Step 1")
                        .build(),
                        Instruction.builder()
                                .instruction("Add 500 gm Pasta")
                                .instructionNo("Step 2")
                                .build()))
                .ingredients(Set.of(
                        Ingredients.builder()
                                .ingredientName("Pasta")
                                .quantity("500gm")
                                .build(),
                        Ingredients.builder()
                                .ingredientName("Tomato")
                                .quantity("150 gm")
                                .build()))
                        .build();

        recipes.add(recipe);
        recipes.add(Recipe.builder()
                .name("Chicken Pepperoni Pizza")
                .recipeType(RecipeType.NON_VEGETARIAN)
                        .servings(2)
                .createdBy(User.builder()
                    .username("userName")
                    .build())
                .instructions(Set.of(
                        Instruction.builder()
                                .instruction("Add  and Mend Dough for 250 gm")
                                .instructionNo("Step 1")
                                .build(),
                        Instruction.builder()
                                .instruction("Keep it aside for 6 hrs")
                                .instructionNo("Step 2")
                                .build()))
                .ingredients(Set.of(
                        Ingredients.builder()
                                .ingredientName("Chicken Shredded")
                                .quantity("100gm")
                                .build(),
                        Ingredients.builder()
                                .ingredientName("Volkern Mais Meel")
                                .quantity("150 gm")
                                .build()))
                .build());
        return recipes;
    }

    public static List<RecipeDetails> getRecipeDetails() {
        return List.of(createRecipeDetails("1", "my recipe 1", nl.abn.assignment.domain.RecipeType.VEGETARIAN),
                createRecipeDetails("2", "my recipe 1", nl.abn.assignment.domain.RecipeType.GLUTEN_FREE));
    }



    private static RecipeDetails createRecipeDetails(String id, String name, nl.abn.assignment.domain.RecipeType recipeType) {
        return RecipeDetails.builder()
                .id(id)
                .name(name)
                .recipeType(recipeType)
                .ingredients(Set.of(IngredientsDto.builder()
                                .ingredientName("Salt")
                                .quantity("100 gm")
                        .build(), IngredientsDto.builder()
                                .ingredientName("Sugar")
                                .quantity("300 gm")
                        .build()))
                .instructions(Set.of(InstructionDto
                                .builder()
                                .instruction("Instruction 1")
                                .instructionNo("Step 1")
                        .build(),
                        InstructionDto.builder()
                                .instruction("Instruction 2")
                                .instructionNo("Step 2")
                                .build()))
                .build();
    }

    public static CreateRecipeRequest createRecipeRequest(String name, nl.abn.assignment.domain.RecipeType recipeType) {
        return CreateRecipeRequest.builder()
                .name(name)
                .recipeType(recipeType)
                .servings(2)
                .ingredients(Set.of(IngredientsDto.builder()
                        .ingredientName("Salt")
                        .quantity("100 gm")
                        .build(), IngredientsDto.builder()
                        .ingredientName("Sugar")
                        .quantity("300 gm")
                        .build()))
                .instructions(Set.of(InstructionDto
                                .builder()
                                .instruction("Instruction 1")
                                .instructionNo("Step 1")
                                .build(),
                        InstructionDto.builder()
                                .instruction("Instruction 2")
                                .instructionNo("Step 2")
                                .build()))
                .build();
    }

}
