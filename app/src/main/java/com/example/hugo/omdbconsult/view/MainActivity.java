package com.example.hugo.omdbconsult.view;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.hugo.omdbconsult.R;
import com.example.hugo.omdbconsult.model.Movie;
import com.example.hugo.omdbconsult.persistence.MovieDAO;
import com.example.hugo.omdbconsult.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private MovieDAO movieDAO;

    private ListView movieListView;

    private AdapterListView adapterListView;

    private List<Movie> movieList;

    private AutoCompleteTextView titleAutoCompleteTextView;

    private ArrayAdapter<String> autoCompleteAdapter;

    private List<String> movieSuggestionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieSuggestionList = new ArrayList<>();

        movieDAO = new MovieDAO(this);

        movieList = movieDAO.findAll();

        titleAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.title_autocomplete);
        autoCompleteAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        titleAutoCompleteTextView.setAdapter(autoCompleteAdapter);
        autoCompleteAdapter.setNotifyOnChange(false);
        titleAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() > 1) {
                    searchSuggestions(s.toString());
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        movieListView = (ListView) this.findViewById(R.id.movie_list);
        adapterListView = new AdapterListView(this, movieList);
        movieListView.setAdapter(adapterListView);
        movieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                Movie movie = adapterListView.getItem(arg2);
                try {
                    Intent i = new Intent(MainActivity.this,
                            MovieDetailActivity.class);
                    i.putExtra("movie", movie);
                    startActivity(i);

                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });




    }

    public void saveMovie(View view) {
        if (isInternetAvailable(this)) {
            findOnIMDB(titleAutoCompleteTextView.getText().toString());
        }else{
            Toast.makeText(MainActivity.this, R.string.connection_not_found, Toast.LENGTH_LONG).show();
        }
    }

    private void update() {
        runOnUiThread(new Runnable() {
            public void run() {
                Log.d("Main", "update movieList");
                movieListView.invalidateViews();
                adapterListView.notifyDataSetChanged();
            }
        });
    }

    private void findOnIMDB(String title) {
        title = title.replace(" ", "+");
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "http://www.omdbapi.com/?t=" + title + "&plot=short&r=json",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("Response").equals("True")) {
                                final Movie movie = new Movie();
                                movie.setTitle(jsonObject.getString("Title"));
                                movie.setYear(jsonObject.getString("Year"));
                                movie.setRated(jsonObject.getString("Rated"));
                                movie.setReleased(jsonObject.getString("Released"));
                                movie.setRuntime(jsonObject.getString("Runtime"));
                                movie.setGenre(jsonObject.getString("Genre"));
                                movie.setDirector(jsonObject.getString("Director"));
                                movie.setWriter(jsonObject.getString("Writer"));
                                movie.setActors(jsonObject.getString("Actors"));
                                movie.setPlot(jsonObject.getString("Plot"));
                                movie.setLanguage(jsonObject.getString("Language"));
                                movie.setCountry(jsonObject.getString("Country"));
                                movie.setAwards(jsonObject.getString("Awards"));
                                movie.setPoster(jsonObject.getString("Poster"));
                                movie.setMetascore(jsonObject.getString("Metascore"));
                                movie.setImdbRating(jsonObject.getString("imdbRating"));
                                movie.setImdbVotes(jsonObject.getString("imdbVotes"));
                                movie.setImdbID(jsonObject.getString("imdbID"));

                                movieDAO.save(movie);
                                if (!movieList.contains(movie)) {
                                    movieList.add(movie);
                                }
                                update();
                            } else {
                                Toast.makeText(MainActivity.this, R.string.movie_not_found, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("Main", "Error on parse movie data", e);
                        }

                        Log.i("Main", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Main", "Error on find Movie", error.fillInStackTrace());
                        if (error.networkResponse != null)
                            Log.e("Main", new String(error.networkResponse.data));
                    }
                });
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void searchSuggestions(String title) {
        title = title.replace(" ", "+");
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "http://www.omdbapi.com/?s=" + title + "&plot=short&r=json",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has("Search")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("Search");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    String title = jsonArray.getJSONObject(i).getString("Title");
                                    if (!movieSuggestionList.contains(title)) {
                                        movieSuggestionList.add(title);
                                    }
                                }
                                updateAutoComplete();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("Main", "Error on parse movie data", e);
                        }

                        Log.i("Main", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Main", "Error on find Movie", error.fillInStackTrace());
                        if (error.networkResponse != null)
                            Log.e("Main", new String(error.networkResponse.data));
                    }
                });
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void updateAutoComplete() {
        autoCompleteAdapter.clear();
        autoCompleteAdapter.addAll(movieSuggestionList);
        autoCompleteAdapter.getFilter().filter(titleAutoCompleteTextView.getText(), null);
    }


    public boolean isInternetAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
