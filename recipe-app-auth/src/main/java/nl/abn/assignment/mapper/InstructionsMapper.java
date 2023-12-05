package nl.abn.assignment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import nl.abn.assignment.domain.InstructionDto;
import nl.abn.assignment.entities.Instruction;

@Mapper
public interface InstructionsMapper {

    InstructionsMapper INSTANCE = Mappers.getMapper(InstructionsMapper.class);

    InstructionDto toDto(Instruction instruction);

    Instruction toEntity(InstructionDto instructionDto);
}
