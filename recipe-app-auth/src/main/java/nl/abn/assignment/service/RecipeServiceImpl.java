package nl.abn.assignment.service;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.abn.assignment.domain.CreateRecipeRequest;
import nl.abn.assignment.domain.QueryRequest;
import nl.abn.assignment.domain.RecipeDetails;
import nl.abn.assignment.entities.Recipe;
import nl.abn.assignment.entities.User;
import nl.abn.assignment.exception.RecipeApplicationAccessDeniedException;
import nl.abn.assignment.exception.RecipeApplicationNotFoundException;
import nl.abn.assignment.mapper.RecipeDetailsMapper;
import nl.abn.assignment.repository.RecipeRepository;
import nl.abn.assignment.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    private final UserRepository userRepository;

    private final TokenParserService tokenParserService;

    @Override
    public RecipeDetails createRecipe(CreateRecipeRequest createrecipeRequest) {
        var decodedJwt = tokenParserService.parseTokenFromAuthenticatedUser();
        // Access claims
        String userName = decodedJwt.getSubject(); // Subject claim
//        String issuer = decodedJwt.getIssuer(); // Issuer claim
//
//        // Example: extracting a custom claim named 'roles'
//        List<String> roles = decodedJwt.getClaim("roles").asList(String.class);
        User user = userRepository.findByUsername(userName);
        var recipeEntity = RecipeDetailsMapper.INSTANCE.toEntity(createrecipeRequest);
        recipeEntity.setCreatedBy(user);
        Recipe recipe = recipeRepository.save(recipeEntity);
        return RecipeDetailsMapper.INSTANCE.toDto(recipe);
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
        var decodedJwt = tokenParserService.parseTokenFromAuthenticatedUser();
        // Access claims
        String userName = decodedJwt.getSubject(); // Subject claim

        var matchedRecipeEntity = recipeRepository.findById(id);
        if (matchedRecipeEntity.isPresent()) {
            var recipeEntity = matchedRecipeEntity.get();
            var userEntity = recipeEntity.getCreatedBy();
            if (!userName.equals(userEntity.getUsername())) {
                throw new RecipeApplicationAccessDeniedException("Requested resource operation can't be completed by your permission ");
            }
            recipeRepository.deleteById(id);
        }else {
            throw new RecipeApplicationNotFoundException("recipe with the id doesn't exists");
        }
    }
}
