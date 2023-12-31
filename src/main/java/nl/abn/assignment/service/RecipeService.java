package nl.abn.assignment.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import nl.abn.assignment.domain.CreateRecipeRequest;
import nl.abn.assignment.domain.QueryRequest;
import nl.abn.assignment.domain.RecipeDetails;


public interface RecipeService {
    RecipeDetails createRecipe(CreateRecipeRequest createrecipeRequest);

    Page<RecipeDetails> filterRecipe(final Pageable pageable, QueryRequest queryRequest);

    RecipeDetails getRecipeDetails(String id);

    void deleteRecipe(String id);
}
