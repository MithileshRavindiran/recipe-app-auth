package nl.abn.assignment.web;

import nl.abn.assignment.domain.CreateRecipeRequest;
import nl.abn.assignment.domain.QueryRequest;
import nl.abn.assignment.exception.RecipeApplicationNotFoundException;
import nl.abn.assignment.service.RecipeService;
import nl.abn.assignment.util.TestHelperUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(RecipeController.class)
public class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    RecipeService recipeService;


    @Test
    public void whenRequestingGetRecipeDetails_thenServiceProcessTheRequestSuccessfully_expectRecipeWithRequestedIdReturnedSuccessfully() throws Exception {
        when(recipeService.getRecipeDetails(any(String.class))).thenReturn(TestHelperUtil.getRecipeDetails().get(0));
        mockMvc.perform(MockMvcRequestBuilders.get("/recipes/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name", is("my recipe 1")))
               .andExpect(jsonPath("$.recipeType", is("VEGETARIAN")))
               .andExpect(jsonPath("$.id", is("1")));
       verify(recipeService, times(1)).getRecipeDetails(any(String.class));
    }

    @Test
    public void whenRequestingGetRecipeDetails_thenServiceProcessAndThrowsNotFoundException_expect404NotFoundException() throws Exception {
        doThrow(new RecipeApplicationNotFoundException("Requested Record Not Found")).when(recipeService).getRecipeDetails(any(String.class));
        mockMvc.perform(MockMvcRequestBuilders.get("/recipes/1"))
                .andExpect(status().isNotFound());
        verify(recipeService, times(1)).getRecipeDetails(any(String.class));
    }

    @Test
    public void whenRequestingDeletingRecipes_thenServiceProcessTheDeleteRequestSuccessfully() throws Exception {
        doNothing().when(recipeService).deleteRecipe(any(String.class));
        mockMvc.perform(MockMvcRequestBuilders.delete("/recipes/1"))
                .andExpect(status().isOk());
        verify(recipeService, times(1)).deleteRecipe(any(String.class));
    }

    @Test
    public void whenRequestingDeletingRecipe_thenServiceProcessTheDeletionOfAnNonExistentId_expectRecipeNotFoundException() throws Exception {
        doThrow(new RecipeApplicationNotFoundException("Resource Requested For Deletion doesn't exists")).when(recipeService).deleteRecipe(any(String.class));
        mockMvc.perform(MockMvcRequestBuilders.delete("/recipes/1"))
                .andExpect(status().isNotFound());
        verify(recipeService, times(1)).deleteRecipe(any(String.class));
    }

    @Test
    public void whenRequestingCreateRecipe_thenServiceProcessTheValidatedPostObject_expectRecipeCreatedSuccessfully() throws Exception {
      when(recipeService.createRecipe(any(CreateRecipeRequest.class))).thenReturn(TestHelperUtil.getRecipeDetails().get(0));
      mockMvc.perform(MockMvcRequestBuilders.post("/recipes")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content("{\"id\":\"62d2198c85ca871f9fc45c35\",\"name\":\"myrecipe1\",\"recipeType\":\"VEGETARIAN\",\"ingredients\":[{\"ingredientName\":\"Salt\",\"quantity\":\"100gm\"},{\"ingredientName\":\"Sugar\",\"quantity\":\"300gm\"}],\"instructions\":[{\"instruction\":\"Instruction1\",\"instructionNo\":\"Step1\"},{\"instruction\":\"Instruction2\",\"instructionNo\":\"Step2\"}]}"))
                .andExpect(status().isCreated());
        verify(recipeService, times(1)).createRecipe(any(CreateRecipeRequest.class));
    }

    @Test
    public void whenRequestingCreateRecipeWithTitleLessThanMinimum_thenServiceShouldnotBeTriggered_expectPostsThrowBadRequestException() throws Exception {
        when(recipeService.createRecipe(any(CreateRecipeRequest.class))).thenReturn(TestHelperUtil.getRecipeDetails().get(0));
        mockMvc.perform(MockMvcRequestBuilders.post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"my\",\"recipeType\":\"VEGETARIAN\"}"))
                .andExpect(status().isBadRequest()).andReturn();
        verify(recipeService, times(0)).createRecipe(any(CreateRecipeRequest.class));
    }

    @Test
    public void whenRequestingCreatRecipeWithNameMoreThanMaximum_thenServiceShouldnotBeTriggered_expectRecipeThrowBadRequestException() throws Exception {
        when(recipeService.createRecipe(any(CreateRecipeRequest.class))).thenReturn(TestHelperUtil.getRecipeDetails().get(0));
        mockMvc.perform(MockMvcRequestBuilders.post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"myjdfaafjjfajfajajfajfajdfdfdfadfadfafdafadfdafa\",\"recipeType\":\"VEGETARIAN\"}"))
                .andExpect(status().isBadRequest()).andReturn();
        verify(recipeService, times(0)).createRecipe(any(CreateRecipeRequest.class));
    }

    @Test
    public void whenRequestingCreateRecipeWithNoName_thenServiceShouldnotBeTriggered_expectRecipesThrowBadRequestException() throws Exception {
        when(recipeService.createRecipe(any(CreateRecipeRequest.class))).thenReturn(TestHelperUtil.getRecipeDetails().get(0));
        mockMvc.perform(MockMvcRequestBuilders.post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"recipeType\":\"VEGETARIAN\"}"))
                .andExpect(status().isBadRequest()).andReturn();
        verify(recipeService, times(0)).createRecipe(any(CreateRecipeRequest.class));
    }

    @Test
    public void whenRequestingCreateRecipeWithNoRecipeType_thenServiceShouldnotBeTriggered_expectRecipesThrowBadRequestException() throws Exception {
        when(recipeService.createRecipe(any(CreateRecipeRequest.class))).thenReturn(TestHelperUtil.getRecipeDetails().get(0));
        mockMvc.perform(MockMvcRequestBuilders.post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"my title\"}"))
                .andExpect(status().isBadRequest()).andReturn();
        verify(recipeService, times(0)).createRecipe(any(CreateRecipeRequest.class));
    }

    @Test
    public void whenRequestingCreateRecipeWithNoRecipeTypeAndNameFieldLessThanAccepted_thenServiceShouldnotBeTriggered_expectRecipeThrowBadRequestException() throws Exception {
        when(recipeService.createRecipe(any(CreateRecipeRequest.class))).thenReturn(TestHelperUtil.getRecipeDetails().get(0));
        mockMvc.perform(MockMvcRequestBuilders.post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"my\"}"))
                .andExpect(status().isBadRequest()).andReturn();
        verify(recipeService, times(0)).createRecipe(any(CreateRecipeRequest.class));
    }

    @Test
    public void whenRequestingGetAllRecipesWithFilter_thenServiceProcessTheRequestSuccessfully_expectAllRecipesReturnedSuccessfully() throws Exception {
        when(recipeService.filterRecipe(any(Pageable.class), any(QueryRequest.class))).thenReturn(new PageImpl<>(TestHelperUtil.getRecipeDetails()));
        mockMvc.perform(MockMvcRequestBuilders.post("/recipes/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"recipeType\":\"VEGAN\",\"servings\":1,\"ingredients\":[\"sugar\"],\"instructions\":[\"ontheoven\"]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.totalElements", is(2)))
                .andExpect(jsonPath("$.content[0].name", is("my recipe 1")))
                .andExpect(jsonPath("$.content[1].recipeType", is("GLUTEN_FREE")));
        verify(recipeService, times(1)).filterRecipe(any(Pageable.class), any(QueryRequest.class));
    }

    @Test
    public void whenRequestingGetAllRecipesWithNoFilter_thenServiceProcessTheRequestSuccessfully_expectAllRecipesReturnedSuccessfully() throws Exception {
        when(recipeService.filterRecipe(any(Pageable.class), any(QueryRequest.class))).thenReturn(new PageImpl<>(TestHelperUtil.getRecipeDetails()));
        mockMvc.perform(MockMvcRequestBuilders.post("/recipes/filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.totalElements", is(2)))
                .andExpect(jsonPath("$.content[0].name", is("my recipe 1")))
                .andExpect(jsonPath("$.content[1].recipeType", is("GLUTEN_FREE")));
        verify(recipeService, times(1)).filterRecipe(any(Pageable.class), any(QueryRequest.class));
    }

}
