package nl.abn.assignment.service;


import nl.abn.assignment.domain.CreateRecipeRequest;
import nl.abn.assignment.domain.QueryRequest;
import nl.abn.assignment.domain.RecipeDetails;
import nl.abn.assignment.entities.Recipe;
import nl.abn.assignment.exception.RecipeApplicationNotFoundException;
import nl.abn.assignment.mapper.RecipeDetailsMapper;
import nl.abn.assignment.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    @Override
    public RecipeDetails createRecipe(CreateRecipeRequest createrecipeRequest) {
        Recipe recipe = recipeRepository.save(RecipeDetailsMapper.INSTANCE.toEntity(createrecipeRequest));
        return RecipeDetailsMapper.INSTANCE.toDto(recipe);
    }

    @Override
    public Page<RecipeDetails> getRecipe(Pageable pageable) {
       Page<Recipe> recipesPages =  recipeRepository.findAll(pageable);
       return recipesPages.map(RecipeDetailsMapper.INSTANCE::toDto);
    }

    @Override
    public Page<RecipeDetails> filterRecipe(Pageable pageable, QueryRequest queryRequest) {
        Page<Recipe> recipesPages =  recipeRepository.filterRecipe(pageable, queryRequest);
        return recipesPages.map(RecipeDetailsMapper.INSTANCE::toDto);
    }

    @Override
    public RecipeDetails getRecipeDetails(String id) {
      Optional<RecipeDetails> recipeDetails = recipeRepository.findById(id)
              .map(RecipeDetailsMapper.INSTANCE::toDto);

      if(recipeDetails.isPresent()) {
          return recipeDetails.get();
      }else {
          throw new RecipeApplicationNotFoundException("Requested recipe record is not found");
      }
    }

    @Override
    public void deleteRecipe(String id) {
        if (recipeRepository.findById(id).isPresent()) {
            recipeRepository.deleteById(id);
        }else {
            throw new RecipeApplicationNotFoundException("recipe with the id doesn't exists");
        }
    }
}
