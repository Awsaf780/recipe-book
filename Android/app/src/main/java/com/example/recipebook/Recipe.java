package com.example.recipebook;

public class Recipe {

    private String mImageUrl;
    private String mName;
    private double mRating;
    private int reviews;
    private String ingredients;
    private String directions;

    public Recipe(String mImageUrl, String mName, double mRating, int reviews, String ingredients, String directions) {
        this.mImageUrl = mImageUrl;
        this.mName = mName;
        this.mRating = mRating;
        this.reviews = reviews;
        this.ingredients = ingredients;
        this.directions = directions;
    }

    public Recipe() {
    }

    public int getReviews() {
        return reviews;
    }

    public void setReviews(int reviews) {
        this.reviews = reviews;
    }

    public String getIngredients() { return ingredients;
    }

    public void setIngredients(String ingredients) {
        String[] arrStr = ingredients.split("^", 10);
        int i;
        String ingredient = "";

        for(i=0; i<arrStr.length; i++){
            ingredient += arrStr[i] + "\n";
        }

        this.ingredients = ingredient;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public double getmRating() {
        return mRating;
    }

    public void setmRating(double mRating) {
        this.mRating = mRating;
    }
}
