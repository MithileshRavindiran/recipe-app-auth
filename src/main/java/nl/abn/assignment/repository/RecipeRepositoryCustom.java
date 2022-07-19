package nl.abn.assignment.repository;

import nl.abn.assignment.domain.QueryRequest;
import nl.abn.assignment.entities.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface RecipeRepositoryCustom {

    Page<Recipe> filterRecipe(Pageable pageable, QueryRequest queryRequest) ;
}
