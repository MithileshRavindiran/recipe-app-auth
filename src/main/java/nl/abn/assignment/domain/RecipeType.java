package nl.abn.assignment.domain;



public enum RecipeType {
    NON_VEGETARIAN,
    VEGETARIAN,
    VEGAN,
    GLUTEN_FREE,
    RELIGIOUS;

    public String getRecipeType() {
        return this.name();
    }
}
