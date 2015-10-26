package com.example.hugo.omdbconsult.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Hugo on 24/10/2015.
 */
public class CustomSQLiteOpenHelper extends SQLiteOpenHelper {
    public static final String MOVIE_TABLE = "movie";
    public static final String COLUMN_ID = "imdb_id";
    public static final String COLUMNS = "title TEXT,\n" +
            "year TEXT,\n" +
            "rated TEXT,\n" +
            "released TEXT,\n" +
            "runtime TEXT,\n" +
            "genre TEXT,\n" +
            "director TEXT,\n" +
            "writer TEXT,\n" +
            "actors TEXT,\n" +
            "plot TEXT,\n" +
            "language TEXT,\n" +
            "country TEXT,\n" +
            "awards TEXT,\n" +
            "poster TEXT,\n" +
            "metascore TEXT,\n" +
            "imdb_rating TEXT,\n" +
            "imdb_votes TEXT";
    private static final String DATABASE_NAME = " movie.db";
    private static final int DATABASE_VERSION = 1;
    // Database creation sql statement
    private static final String CREATE_MOVIE = " create table " + MOVIE_TABLE
            + "(" + COLUMN_ID + " TEXT , "
            + COLUMNS + ");";

    public CustomSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + MOVIE_TABLE);
        onCreate(db);
    }

}