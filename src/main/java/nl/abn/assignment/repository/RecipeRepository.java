package nl.abn.assignment.repository;

import nl.abn.assignment.entities.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends MongoRepository<Recipe, String>,RecipeRepositoryCustom {
}
