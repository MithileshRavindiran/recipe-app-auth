package nl.abn.assignment.mapper;

import nl.abn.assignment.domain.IngredientsDto;
import nl.abn.assignment.entities.Ingredients;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IngredientsMapper {

    IngredientsMapper INSTANCE = Mappers.getMapper(IngredientsMapper.class);

    IngredientsDto toDto(Ingredients ingredients);

    Ingredients toEntity(IngredientsDto ingredientsDto);
}
