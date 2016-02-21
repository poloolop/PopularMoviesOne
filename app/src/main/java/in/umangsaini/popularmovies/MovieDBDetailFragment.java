package in.umangsaini.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by 100511 on 20/02/2016.
 */
public class MovieDBDetailFragment extends Fragment {

    public MovieDBDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.moviedetail_fragment, container, false);

        /* Getting intent to pull Movie details */
        Intent intent = getActivity().getIntent();

        if (intent != null) {
            MovieDB movieItem = (MovieDB) intent.getSerializableExtra("movie");
            ((TextView) rootView.findViewById(R.id.movieDBTitle))
                    .setText(movieItem.title);
            ImageView iconView = (ImageView) rootView.findViewById(R.id.movieDBPoster);
            Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w185/"+movieItem.image).into(iconView);
            ((TextView) rootView.findViewById(R.id.movieDBSynopsis))
                    .setText("Movie Overview:  " + movieItem.overview_text);
            ((TextView) rootView.findViewById(R.id.movieDBReleaseDate))
                    .setText("Release Date:  " + movieItem.release_date);
            ((TextView) rootView.findViewById(R.id.movieDBRating))
                    .setText("Average Votes:  " + movieItem.vote_average);
        }

        return rootView;
    }
}
