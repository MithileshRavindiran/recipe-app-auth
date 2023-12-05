package nl.abn.assignment.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstructionDto {

    @Schema(description = "Instruction Sequence Number", example = "Step No 1")
    private String instructionNo;

    @Schema(description = "Instruction to do the Recipe", example = "Add Salt")
    private String instruction;
}
