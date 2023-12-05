package nl.abn.assignment.service;

import static nl.abn.assignment.util.TestHelperUtil.getListOfRecipeEntity;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.auth0.jwt.interfaces.DecodedJWT;

import nl.abn.assignment.domain.CreateRecipeRequest;
import nl.abn.assignment.domain.QueryRequest;
import nl.abn.assignment.domain.RecipeDetails;
import nl.abn.assignment.domain.RecipeType;
import nl.abn.assignment.entities.Recipe;
import nl.abn.assignment.exception.RecipeApplicationNotFoundException;
import nl.abn.assignment.repository.RecipeRepository;
import nl.abn.assignment.repository.UserRepository;

public class RecipeServiceImplTest {

    @InjectMocks
    RecipeServiceImpl recipeService;

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    TokenParserService tokenParserService;

    @Mock
    DecodedJWT decodedJWT;

    List<Recipe> recipeList;

    @BeforeEach
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);
        recipeList = getListOfRecipeEntity();
    }


    @Test
    public void whenRequestingRecipesWithFilter_thenFromTheDatabaseEmptyListRecipesFetched_expectTheRecipesSentInPageable() {
        Page<Recipe> recipePage = new PageImpl<>(getListOfRecipeEntity());
        when(recipeRepository.filterRecipe(PageRequest.of(1,10, Sort.by("id")), QueryRequest.builder()
                .excludeIngredients(false)
                .ingredients(List.of("salt","sugar"))
                .instructions(List.of("Chicken", "Place on the oven"))
                .noOfServings(1)
                .build())).thenReturn(recipePage);
        Page<RecipeDetails> recipeInfoPages = recipeService.filterRecipe(
                PageRequest.of(1,10, Sort.by("id")), QueryRequest.builder()
                        .excludeIngredients(false)
                        .ingredients(List.of("salt","sugar"))
                        .instructions(List.of("Chicken", "Place on the oven"))
                        .noOfServings(1)
                .build());
        assertAll("Validating the Received recipe",
                () -> assertNotNull(recipeInfoPages),
                () -> assertEquals(2, recipeInfoPages.getTotalElements()),
                () -> assertEquals(2, recipeInfoPages.getContent().size()),
                () -> verify(recipeRepository, times(1))
                        .filterRecipe(PageRequest.of(1,10, Sort.by("id")),QueryRequest.builder()
                        .excludeIngredients(false)
                        .ingredients(List.of("salt","sugar"))
                        .instructions(List.of("Chicken", "Place on the oven"))
                        .noOfServings(1)
                        .build() ));
    }

    @Test
    public void whenRequestingRecipesByPostId_thenFromTheDatabaseRecipesByIdFetchedSuccessfully_expectTheValidRecipes() {
        when(recipeRepository.findById("1")).thenReturn(Optional.ofNullable(recipeList.get(0)));
        RecipeDetails details = recipeService.getRecipeDetails("1");
        assertAll("Validating the Received post messages",
                () -> assertNotNull(details),
                () -> assertEquals("White Sauce Pasta", details.getName()),
                () -> assertEquals("VEGAN", details.getRecipeType().name()),
                () -> verify(recipeRepository, times(1)).findById("1"));
    }

    @Test
    public void whenRequestingRecipesByRecipeId_thenFromTheDatabaseRecipesByIdFetchesEmptySuccessfully_expectRecipesApplicationNotFoundException() {
        when(recipeRepository.findById("10")).thenReturn(Optional.ofNullable(null));
        RecipeApplicationNotFoundException thrown = assertThrows(RecipeApplicationNotFoundException.class, () -> {
            recipeService.getRecipeDetails("10");
        }, "RecipeApplicationNotFoundException was expected");
        assertNotNull(thrown.getMessage());
        verify(recipeRepository, times(1)).findById("10");
    }

    @Test
    public void whenRequestingDeletingARecipeById_thenFromTheDatabasePostsByIdIsDeleted_expectTheRecipeDeleted() {
        when(tokenParserService.parseTokenFromAuthenticatedUser()).thenReturn(decodedJWT);
        when(decodedJWT.getSubject()).thenReturn("userName");
        when(recipeRepository.findById("1")).thenReturn(Optional.ofNullable(recipeList.get(0)));
        doNothing().when(recipeRepository).deleteById("1");
       recipeService.deleteRecipe("1");
        assertAll("Validating the delete recipe",
                () -> verify(recipeRepository, times(1)).findById("1"),
                () -> verify(recipeRepository, times(1)).deleteById("1"));
    }

    @Test
    public void whenRequestingDeletingARecipeById_thenFromTheDatabaseRecipeIsNotFound_expectRecipeApplicationNotFoundException() {
        when(tokenParserService.parseTokenFromAuthenticatedUser()).thenReturn(decodedJWT);
        when(decodedJWT.getSubject()).thenReturn("userName");
        when(recipeRepository.findById("1")).thenReturn(Optional.ofNullable(null));

        RecipeApplicationNotFoundException thrown = assertThrows(RecipeApplicationNotFoundException.class, () -> {
            recipeService.deleteRecipe("1");
        }, "RecipeApplicationNotFoundException was expected");
        assertNotNull(thrown.getMessage());
        verify(recipeRepository, times(1)).findById("1");
        verify(recipeRepository, times(0)).deleteById("1");
    }

    @Test
    public void whenRequestingDeletingARecipeById_thenFromTheDatabaseRecipeIsNotFound_expectRecipeAccessDeniedException() {
        when(tokenParserService.parseTokenFromAuthenticatedUser()).thenReturn(decodedJWT);
        when(decodedJWT.getSubject()).thenReturn("userName");
        when(recipeRepository.findById("1")).thenReturn(Optional.ofNullable(null));

        RecipeApplicationNotFoundException thrown = assertThrows(RecipeApplicationNotFoundException.class, () -> {
            recipeService.deleteRecipe("1");
        }, "RecipeApplicationNotFoundException was expected");
        assertNotNull(thrown.getMessage());
        verify(recipeRepository, times(1)).findById("1");
        verify(recipeRepository, times(0)).deleteById("1");
    }

    @Test
    public void whenRequestingCreatingANewRecipe_thenFromTheDatabaseNewRecipeIsCreate_expectTheRecipeDetailsoReturnedSuccessfully() {
        CreateRecipeRequest createRecipeRequest = CreateRecipeRequest.builder()
                .name("my title")
                .recipeType(RecipeType.VEGAN)
                .build();
        when(tokenParserService.parseTokenFromAuthenticatedUser()).thenReturn(decodedJWT);
        when(decodedJWT.getSubject()).thenReturn("userName");
        when(recipeRepository.save(any())).thenReturn(recipeList.get(0));
        RecipeDetails recipeDetails = recipeService.createRecipe(createRecipeRequest);
        assertAll("Validating the Received post messages",
                () -> assertNotNull(recipeDetails),
                () -> assertEquals("White Sauce Pasta", recipeDetails.getName()),
                () -> assertEquals("VEGAN", recipeDetails.getRecipeType().name()),
                ()  -> assertEquals(2, recipeDetails.getIngredients().size()),
                ()  -> assertEquals(2, recipeDetails.getInstructions().size()),
                () -> verify(recipeRepository, times(1)).save(any()));
    }


}
