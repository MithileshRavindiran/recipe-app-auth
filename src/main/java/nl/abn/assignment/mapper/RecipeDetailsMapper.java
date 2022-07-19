package nl.abn.assignment.mapper;

import nl.abn.assignment.domain.CreateRecipeRequest;
import nl.abn.assignment.entities.Recipe;
import nl.abn.assignment.domain.RecipeDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

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
