package nl.abn.assignment;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import nl.abn.assignment.entities.User;
import nl.abn.assignment.entities.UserRole;
import nl.abn.assignment.service.UserService;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Recipe Api", version = "1.0", description = "Recipe Management Api's"))
public class RecipeAppAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecipeAppAuthApplication.class, args);
    }

}
