package com.example.recipebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static com.example.recipebook.MainActivity.EXTRA_DIRECTIONS;
import static com.example.recipebook.MainActivity.EXTRA_INGREDIENTS;
import static com.example.recipebook.MainActivity.EXTRA_NAME;
import static com.example.recipebook.MainActivity.EXTRA_RATING;
import static com.example.recipebook.MainActivity.EXTRA_REVIEWS;
import static com.example.recipebook.MainActivity.EXTRA_URL;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra(EXTRA_URL);
        String name = intent.getStringExtra(EXTRA_NAME);
        double rating = intent.getDoubleExtra(EXTRA_RATING, 0.0);
        int reviews = intent.getIntExtra(EXTRA_REVIEWS, 0);
        String ingredients = intent.getStringExtra(EXTRA_INGREDIENTS);
        String directions = intent.getStringExtra(EXTRA_DIRECTIONS);


        ImageView imageView = findViewById(R.id.image_view_detail);
        TextView textViewName = findViewById(R.id.text_view_name_detail);
        TextView textViewRating = findViewById(R.id.text_view_rating_detail);
        TextView textViewReviews = findViewById(R.id.text_view_review_detail);
        TextView textViewIngredients = findViewById(R.id.text_view_ingredients_detail);
        TextView textViewDirections = findViewById(R.id.text_view_directions_detail);


        Picasso.with(this).load(imageUrl).fit().centerCrop().into(imageView);
        textViewName.setText(name);
        textViewRating.setText("Rating: " + rating);
        textViewReviews.setText("Reviews: "+reviews);
        textViewIngredients.setText(ingredients);
        textViewDirections.setText(directions);


    }
}