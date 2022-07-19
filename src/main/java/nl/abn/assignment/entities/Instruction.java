package nl.abn.assignment.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.index.TextIndexed;

@Accessors(chain = true)
@AllArgsConstructor
@Data
@Builder
public class Instruction {

    private String instructionNo;

    @TextIndexed
    private String instruction;
}
