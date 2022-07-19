package nl.abn.assignment.repository.impl;

import nl.abn.assignment.domain.QueryRequest;
import nl.abn.assignment.entities.Recipe;
import nl.abn.assignment.repository.RecipeRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class RecipeRepositoryImpl implements RecipeRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public RecipeRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<Recipe> filterRecipe(Pageable pageable, QueryRequest queryRequest) {
        Query query = getQuery(pageable, queryRequest);
        List<Recipe> queryResults = mongoTemplate.find(query, Recipe.class);
        return PageableExecutionUtils.getPage(
                queryResults,
                pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Recipe.class));
    }

    private Query getQuery(Pageable pageable, QueryRequest queryRequest) {
        Query query = new Query();
        if (queryRequest != null) {
            if (queryRequest.getRecipeType() != null) {
                query.addCriteria(Criteria.where("recipeType").is(queryRequest.getRecipeType()));
            }
            if (queryRequest.getNoOfServings() != null && queryRequest.getNoOfServings() > 0) {
                query.addCriteria(Criteria.where("servings").is(queryRequest.getNoOfServings()));
            }

            if (queryRequest.getIngredients() != null && !queryRequest.getIngredients().isEmpty()) {
                if (!queryRequest.isExcludeIngredients()) {
                    query.addCriteria(Criteria.where("ingredients.ingredientName")
                           .in(queryRequest.getIngredients()));
                }
                if (queryRequest.isExcludeIngredients()) {
                    query.addCriteria(Criteria.where("ingredients.ingredientName")
                           .nin(queryRequest.getIngredients()));
                }
            }

            if (queryRequest.getInstructions() != null && !queryRequest.getInstructions().isEmpty()) {
                query.addCriteria(TextCriteria.forDefaultLanguage().matchingAny(queryRequest.getInstructions().toArray(new String[0])));
            }
        }
        query.with(pageable);
        return query;
    }
}
