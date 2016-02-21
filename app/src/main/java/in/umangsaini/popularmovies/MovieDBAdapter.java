package in.umangsaini.popularmovies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Umang Saini on 20/02/2016.
 * Adapter to hold data MovieDB data
 */
public class MovieDBAdapter extends ArrayAdapter<MovieDB> {
    private static final String LOG_TAG = MovieDBAdapter.class.getSimpleName();

    public MovieDBAdapter(Activity context, List<MovieDB> movies){
        super(context, 0, movies);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        MovieDB movie = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.moviedb_item, parent, false);
        }

        ImageView iconView = (ImageView) convertView.findViewById(R.id.moviedb_image);
        Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w154/"+movie.image).into(iconView);
        return convertView;
    }
}
