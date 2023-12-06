package nl.abn.assignment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import nl.abn.assignment.domain.IngredientsDto;
import nl.abn.assignment.entities.Ingredients;

@Mapper(componentModel = "spring")
public interface IngredientsMapper {

    IngredientsMapper INSTANCE = Mappers.getMapper(IngredientsMapper.class);

    IngredientsDto toDto(Ingredients ingredients);

    Ingredients toEntity(IngredientsDto ingredientsDto);
}
