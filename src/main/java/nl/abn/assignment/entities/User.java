package nl.abn.assignment.entities;

import java.util.Set;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Document(collection = "users")
@Accessors(chain = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @MongoId(FieldType.OBJECT_ID)
    private String id;

    @Indexed(unique = true)
    @Field
    private String username;

    private String password;

    @Field(targetType = FieldType.ARRAY)
    private Set<String> userRoles;
}
