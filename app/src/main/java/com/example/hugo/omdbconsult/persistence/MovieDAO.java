package com.example.hugo.omdbconsult.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.hugo.omdbconsult.model.Movie;

import java.util.ArrayList;

/**
 * Created by Hugo on 24/10/2015.
 */
public class MovieDAO {
    private CustomSQLiteOpenHelper sqliteOpenHelper;
    private SQLiteDatabase database;

    public MovieDAO(Context context) {
        sqliteOpenHelper = new CustomSQLiteOpenHelper(context);
    }

    public void open() throws SQLException {
        database = sqliteOpenHelper.getWritableDatabase();
    }

    public void close() {
        sqliteOpenHelper.close();
    }

    public Movie save(Movie movie) {
        if (findById(movie.getImdbID()) == null) {
            return create(movie);
        } else {
            return update(movie);
        }
    }

    public Movie create(Movie movie) {
        ContentValues movieValues = new ContentValues();

        movieValues.put("imdb_id", movie.getImdbID());
        movieValues.put("title", movie.getTitle());
        movieValues.put("year", movie.getYear());
        movieValues.put("rated", movie.getRated());
        movieValues.put("released", movie.getReleased());
        movieValues.put("runtime", movie.getRuntime());
        movieValues.put("genre", movie.getGenre());
        movieValues.put("director", movie.getDirector());
        movieValues.put("writer", movie.getWriter());
        movieValues.put("actors", movie.getActors());
        movieValues.put("plot", movie.getPlot());
        movieValues.put("language", movie.getLanguage());
        movieValues.put("country", movie.getCountry());
        movieValues.put("awards", movie.getAwards());
        movieValues.put("poster", movie.getPoster());
        movieValues.put("metascore", movie.getMetascore());
        movieValues.put("imdb_rating", movie.getImdbRating());
        movieValues.put("imdb_votes", movie.getImdbVotes());
        open();
        database.insert(CustomSQLiteOpenHelper.MOVIE_TABLE,
                null, movieValues);

        close();
        return movie;
    }

    public Movie update(Movie movie) {
        ContentValues movieValues = new ContentValues();
        movieValues.put("title", movie.getTitle());
        movieValues.put("year", movie.getYear());
        movieValues.put("rated", movie.getRated());
        movieValues.put("released", movie.getReleased());
        movieValues.put("runtime", movie.getRuntime());
        movieValues.put("genre", movie.getGenre());
        movieValues.put("director", movie.getDirector());
        movieValues.put("writer", movie.getWriter());
        movieValues.put("actors", movie.getActors());
        movieValues.put("plot", movie.getPlot());
        movieValues.put("language", movie.getLanguage());
        movieValues.put("country", movie.getCountry());
        movieValues.put("awards", movie.getAwards());
        movieValues.put("poster", movie.getPoster());
        movieValues.put("metascore", movie.getMetascore());
        movieValues.put("imdb_rating", movie.getImdbRating());
        movieValues.put("imdb_votes", movie.getImdbVotes());
        open();
        database.update(CustomSQLiteOpenHelper.MOVIE_TABLE, movieValues,
                CustomSQLiteOpenHelper.COLUMN_ID + "='" + movie.getImdbID() + "'", null);

        close();
        return movie;
    }

    public Movie findById(String id) {
        Movie movie = null;
        open();
        Cursor cursor = database.query(CustomSQLiteOpenHelper.MOVIE_TABLE,
                null, CustomSQLiteOpenHelper.COLUMN_ID + " = '" + id + "'", null, null, null, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            movie = new Movie();
            movie.setImdbID(cursor.getString(0));
            movie.setTitle(cursor.getString(1));
            movie.setYear(cursor.getString(2));
            movie.setRated(cursor.getString(3));
            movie.setReleased(cursor.getString(4));
            movie.setRuntime(cursor.getString(5));
            movie.setGenre(cursor.getString(6));
            movie.setDirector(cursor.getString(7));
            movie.setWriter(cursor.getString(8));
            movie.setActors(cursor.getString(9));
            movie.setPlot(cursor.getString(10));
            movie.setLanguage(cursor.getString(11));
            movie.setCountry(cursor.getString(12));
            movie.setAwards(cursor.getString(13));
            movie.setPoster(cursor.getString(14));
            movie.setMetascore(cursor.getString(15));
            movie.setImdbRating(cursor.getString(16));
            movie.setImdbVotes(cursor.getString(17));
            cursor.moveToNext();
        }
        cursor.close();
        close();
        return movie;
    }

    public ArrayList<Movie> findAll() {
        ArrayList<Movie> movieList = new ArrayList<>();
        open();
        Cursor cursor = database.query(CustomSQLiteOpenHelper.MOVIE_TABLE,
                null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Movie movie = new Movie();
            movie.setImdbID(cursor.getString(0));
            movie.setTitle(cursor.getString(1));
            movie.setYear(cursor.getString(2));
            movie.setRated(cursor.getString(3));
            movie.setReleased(cursor.getString(4));
            movie.setRuntime(cursor.getString(5));
            movie.setGenre(cursor.getString(6));
            movie.setDirector(cursor.getString(7));
            movie.setWriter(cursor.getString(8));
            movie.setActors(cursor.getString(9));
            movie.setPlot(cursor.getString(10));
            movie.setLanguage(cursor.getString(11));
            movie.setCountry(cursor.getString(12));
            movie.setAwards(cursor.getString(13));
            movie.setPoster(cursor.getString(14));
            movie.setMetascore(cursor.getString(15));
            movie.setImdbRating(cursor.getString(16));
            movie.setImdbVotes(cursor.getString(17));
            movieList.add(movie);
            cursor.moveToNext();
        }
        cursor.close();
        close();
        return movieList;
    }

    public int remove(Movie movie) {
        open();
        int retValue = database.delete(CustomSQLiteOpenHelper.MOVIE_TABLE, CustomSQLiteOpenHelper.COLUMN_ID + "= " + movie.getImdbID(), null);
        close();
        return retValue;
    }
}
