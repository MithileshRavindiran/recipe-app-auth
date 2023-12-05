package nl.abn.assignment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import nl.abn.assignment.domain.CreateRecipeRequest;
import nl.abn.assignment.domain.RecipeDetails;
import nl.abn.assignment.entities.Recipe;

@Mapper(uses = {IngredientsMapper.class, InstructionsMapper.class})
public interface RecipeDetailsMapper {

    RecipeDetailsMapper INSTANCE = Mappers.getMapper(RecipeDetailsMapper.class);

//    @Mapping(source = "recipe.instructions", target = "ingredientsDto")
//    @Mapping(source = "recipe.ingredients", target = "instructionDto")
    RecipeDetails toDto(Recipe recipe);

//    @Mapping(source = "request.instructionDto", target = "instructions")
//    @Mapping(source = "request.ingredientsDto", target = "ingredients")
    Recipe toEntity(CreateRecipeRequest request);
}
