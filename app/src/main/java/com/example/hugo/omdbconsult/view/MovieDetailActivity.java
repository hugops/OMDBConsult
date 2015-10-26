package com.example.hugo.omdbconsult.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hugo.omdbconsult.R;
import com.example.hugo.omdbconsult.model.Movie;

public class MovieDetailActivity extends AppCompatActivity {

    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent data = this.getIntent();
        movie = (Movie) data.getExtras().get("movie");

        if (movie.getPosterBitmap() != null) {
            ((ImageView) findViewById(R.id.poster_image_detail)).setImageBitmap(movie.getPosterBitmap().getBitmap());
        }
        ((TextView) findViewById(R.id.title_text_detail)).setText(movie.getTitle());
        ((TextView) findViewById(R.id.year_text_detail)).setText(movie.getYear());
        ((TextView) findViewById(R.id.rated_text_detail)).setText(movie.getRated());
        ((TextView) findViewById(R.id.released_text_detail)).setText(movie.getReleased());
        ((TextView) findViewById(R.id.runtime_text_detail)).setText(movie.getRuntime());
        ((TextView) findViewById(R.id.genre_text_detail)).setText(movie.getGenre());
        ((TextView) findViewById(R.id.director_text_detail)).setText(movie.getDirector());
        ((TextView) findViewById(R.id.writer_text_detail)).setText(movie.getWriter());
        ((TextView) findViewById(R.id.actors_text_detail)).setText(movie.getActors());
        ((TextView) findViewById(R.id.plot_text_detail)).setText(movie.getPlot());
        ((TextView) findViewById(R.id.language_text_detail)).setText(movie.getLanguage());
        ((TextView) findViewById(R.id.contry_text_detail)).setText(movie.getCountry());
        ((TextView) findViewById(R.id.awards_text_detail)).setText(movie.getAwards());
        ((TextView) findViewById(R.id.metascore_text_detail)).setText(movie.getMetascore());
        ((TextView) findViewById(R.id.rating_text_detail)).setText(movie.getImdbRating());
        ((TextView) findViewById(R.id.votes_text_detail)).setText(movie.getImdbVotes());

    }

}
