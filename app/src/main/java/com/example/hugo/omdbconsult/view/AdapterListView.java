package com.example.hugo.omdbconsult.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.hugo.omdbconsult.R;
import com.example.hugo.omdbconsult.model.Movie;
import com.example.hugo.omdbconsult.utils.CachedBitmap;
import com.example.hugo.omdbconsult.utils.VolleySingleton;

import java.util.List;

/**
 * Created by Hugo on 24/10/2015.
 */
public class AdapterListView extends BaseAdapter {

    private Context context;

    private List<Movie> movieList;

    public AdapterListView(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public Movie getItem(int position) {
        return movieList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.movie_item, parent, false);
        }
        TextView title = ViewHolder.get(convertView, R.id.title_text);
        final ImageView poster = ViewHolder.get(convertView, R.id.poster_image);
        final ProgressBar spinner = ViewHolder.get(convertView, R.id.progressBar1);

        final Movie movie = movieList.get(position);

        title.setText(movie.getTitle());

        if (movie.getPosterBitmap() == null && !movie.getPoster().equals("N/A")) {
            ImageRequest request = new ImageRequest(movie.getPoster(),
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            poster.setImageBitmap(bitmap);
                            movie.setPosterBitmap(new CachedBitmap(bitmap, movie.getPoster()));
                            spinner.setVisibility(View.GONE);
                        }
                    }, 0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.ARGB_8888,
                    new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Main", "Error when downloading Image");
                            spinner.setVisibility(View.GONE);
                        }
                    });
            VolleySingleton.getInstance(context).addToRequestQueue(request);
        }else{
            if(movie.getPoster().equals("N/A")) {
                poster.setImageResource(R.drawable.no_poster_available);
            }else{
                poster.setImageBitmap(movie.getPosterBitmap().getBitmap());
            }
            spinner.setVisibility(View.GONE);
        }
        return convertView;
    }
}
