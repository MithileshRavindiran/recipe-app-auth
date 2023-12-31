package nl.abn.assignment.web;


import java.util.List;

import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.abn.assignment.domain.CreateRecipeRequest;
import nl.abn.assignment.domain.QueryRequest;
import nl.abn.assignment.domain.RecipeDetails;
import nl.abn.assignment.exception.ErrorResponse;
import nl.abn.assignment.service.RecipeService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/recipes", produces = MediaType.APPLICATION_JSON_VALUE)
public class RecipeController {

    private final RecipeService recipeService;


    @Operation(summary = "Get Recipe with Optional Filters")
    @PageableAsQueryParam
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",description =" Filters and Returns the matched recipe",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecipeDetails.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Exception",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(path = "/filter",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<RecipeDetails>> createRecipe(@PageableDefault(sort = {"id"}) Pageable pageable, @Valid @RequestBody(required = false) QueryRequest queryRequest) {
        ResponseEntity<Page<RecipeDetails>> responseEntity =  new ResponseEntity<>(recipeService.filterRecipe(pageable, queryRequest), HttpStatus.OK);
        return responseEntity;
    }

    @Operation(summary = "Create a new recipe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",description =" Create  and  returns a new recipe",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecipeDetails.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request mandatory  fields missing",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Request Resource is not Found",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Exception",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RecipeDetails> createRecipe(@Valid @RequestBody CreateRecipeRequest createrecipeRequest) {
        return new ResponseEntity<>(recipeService.createRecipe(createrecipeRequest), HttpStatus.CREATED);
    }

    @Operation(summary = "Retrieves a recipe by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description =" Retrieved recipe matched by the id",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecipeDetails.class))),
            @ApiResponse(responseCode = "404", description = "Request Resource is not Found",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Exception",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("{id}")
    public ResponseEntity<RecipeDetails> getRecipeDetails(@PathVariable("id") final String id) {
        return new ResponseEntity<>(recipeService.getRecipeDetails(id), HttpStatus.OK);
    }

    @Operation(summary = "Delete a recipe by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",description =" Deletes the Requested recipe",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Request Resource is not Found For Deletion",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Exception",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable("id") final String id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }
}
