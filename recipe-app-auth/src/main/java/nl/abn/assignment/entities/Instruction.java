package nl.abn.assignment.entities;

import org.springframework.data.mongodb.core.index.TextIndexed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@AllArgsConstructor
@Data
@Builder
public class Instruction {

    private String instructionNo;

    @TextIndexed
    private String instruction;
}
