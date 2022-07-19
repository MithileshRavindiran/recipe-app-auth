package nl.abn.assignment.service;

import nl.abn.assignment.domain.CreateRecipeRequest;
import nl.abn.assignment.domain.QueryRequest;
import nl.abn.assignment.domain.RecipeDetails;
import nl.abn.assignment.domain.RecipeType;
import nl.abn.assignment.entities.Recipe;
import nl.abn.assignment.exception.RecipeApplicationNotFoundException;
import nl.abn.assignment.repository.RecipeRepository;
import nl.abn.assignment.util.TestHelperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static nl.abn.assignment.util.TestHelperUtil.getListOfRecipeEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RecipeServiceImplTest {

    @InjectMocks
    RecipeServiceImpl recipeService;

    @Mock
    RecipeRepository recipeRepository;

    List<Recipe> recipeList;

    @BeforeEach
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);
        recipeList = getListOfRecipeEntity();
    }

    @Test
    public void whenRequestingRecipes_thenFromTheDatabaseRecipesFetched_expectTheValidPostsSentInPageable() {
        Page<Recipe> recipePage = new PageImpl<>(recipeList);
        when(recipeRepository.findAll(PageRequest.of(1,10, Sort.by("id")))).thenReturn(recipePage);
        Page<RecipeDetails> postInfoPages = recipeService.getRecipe(PageRequest.of(1,10, Sort.by("id")));
        assertAll("Validating the Received recipe",
                () -> assertNotNull(postInfoPages),
                () -> assertEquals(2, postInfoPages.getTotalElements()),
                () -> assertEquals(2, postInfoPages.getContent().size()),
                () -> verify(recipeRepository, times(1)).findAll(PageRequest.of(1,10, Sort.by("id"))));
    }

    @Test
    public void whenRequestingRecipes_thenFromTheDatabaseEmptyListRecipesFetched_expectTheRecipesSentInPageable() {
        Page<Recipe> recipePage = new PageImpl<>(new ArrayList<>());
        when(recipeRepository.findAll(PageRequest.of(1,10, Sort.by("id")))).thenReturn(recipePage);
        Page<RecipeDetails> recipeInfoPages = recipeService.getRecipe(PageRequest.of(1,10, Sort.by("id")));
        assertAll("Validating the Received recip",
                () -> assertNotNull(recipeInfoPages),
                () -> assertEquals(0, recipeInfoPages.getTotalElements()),
                () -> assertEquals(0, recipeInfoPages.getContent().size()),
                () -> verify(recipeRepository, times(1)).findAll(PageRequest.of(1,10, Sort.by("id"))));
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
        when(recipeRepository.findById("1")).thenReturn(Optional.ofNullable(recipeList.get(0)));
        doNothing().when(recipeRepository).deleteById("1");
       recipeService.deleteRecipe("1");
        assertAll("Validating the delete recipe",
                () -> verify(recipeRepository, times(1)).findById("1"),
                () -> verify(recipeRepository, times(1)).deleteById("1"));
    }

    @Test
    public void whenRequestingDeletingARecipeById_thenFromTheDatabaseRecipeIsNotFound_expectRecipeApplicationNotFoundException() {
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
