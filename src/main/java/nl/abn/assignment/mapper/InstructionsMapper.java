package nl.abn.assignment.mapper;

import nl.abn.assignment.domain.IngredientsDto;
import nl.abn.assignment.domain.InstructionDto;
import nl.abn.assignment.entities.Ingredients;
import nl.abn.assignment.entities.Instruction;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InstructionsMapper {

    InstructionsMapper INSTANCE = Mappers.getMapper(InstructionsMapper.class);

    InstructionDto toDto(Instruction instruction);

    Instruction toEntity(InstructionDto instructionDto);
}
