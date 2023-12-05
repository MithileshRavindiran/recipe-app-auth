package nl.abn.assignment.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import nl.abn.assignment.entities.Recipe;

@Repository
public interface RecipeRepository extends MongoRepository<Recipe, String>,RecipeRepositoryCustom {
}
