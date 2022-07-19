package nl.abn.assignment.domain;

import lombok.Data;
import nl.abn.assignment.entities.Recipe;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

@Data
public class RecipeAggregationResult {

    private int count;
    private List<Recipe> recipes;

    public static class RecipeAggregationExecutor {

        private Aggregation aggregation;

        public RecipeAggregationExecutor withAggregations(final AggregationOperation... operations) {
            this.aggregation = newAggregation(operations);
            return this;
        }

        @SuppressWarnings("unchecked")
        public RecipeAggregationResult executeAndGetResult(final MongoOperations operations) {
            return operations.aggregate(aggregation, Recipe.class, RecipeAggregationResult.class)
                    .getUniqueMappedResult();
        }

    }
}
