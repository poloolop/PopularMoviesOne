package in.umangsaini.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Umang Saini on 20/02/2016.
 */
public class MovieDBListFragment extends Fragment {

    private MovieDBAdapter movieAdapter;
    private static String sortBy = "popular";
    private final String TAG = MovieDBListFragment.class.getSimpleName();

    public MovieDBListFragment() {
    }

    public static MovieDBListFragment getInstance(String title) {
        MovieDBListFragment fragment = new MovieDBListFragment();
        /* Sort By method title is set here*/
        sortBy = title;
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movielist_fragment, container, false);

        movieAdapter = new MovieDBAdapter(getActivity(), new ArrayList<MovieDB>());

        /* Attach Adapter to the GridView */
        GridView gridView = (GridView) rootView.findViewById(R.id.moviedb_grid);
        gridView.setAdapter(movieAdapter);
        /* Launch Intent to Detail Activity */
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieDB movieItem = movieAdapter.getItem(position);
                Intent detailActivityIntent = new Intent(getActivity(),
                                    MovieDBDetailActivity.class).putExtra("movie", movieItem);

                startActivity(detailActivityIntent);
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        /* Fetching Movies using Async Background Task, to not block the UI */
        FetchMovieDBBackgroundTask movieTask = new FetchMovieDBBackgroundTask();
        /* Executing the task with sortBy String. Two sorting methods exposed */
        movieTask.execute(sortBy);
    }

    public class FetchMovieDBBackgroundTask extends AsyncTask<String, Void, MovieDB[]> {

        private final String TAG = FetchMovieDBBackgroundTask.class.getSimpleName();

        @Override
        protected MovieDB[] doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            /* String to store JSON String */
            String movieDBJSONString = null;

            /* Replace the key in strings.xml */
            String api_key = getResources().getString(R.string.api_key);

            final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
            final String SORT_BY_PARAM = "sort_by";
            final String API_KEY_PARAM = "api_key";


            try {
                /* Building the query URL */
                /* params[0] is the Sort By string */
                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_BY_PARAM, params[0])
                        .appendQueryParameter(API_KEY_PARAM, api_key)
                        .build();

                URL url = new URL(builtUri.toString());

                /* Create MovieDB request and Connection */
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                /* Read the JSON String */
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                if (buffer.length() == 0) {
                    return null;
                }

                /* Convert received buffer to String */
                movieDBJSONString = buffer.toString();

            } catch (IOException e) {
                Log.e(TAG, "Error ", e);
                /* Connection Exception */
                return null;
            } finally {
                /* Close the connection */
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                /* Parse JSON to string */
                return getMovieDBDataFromJson(movieDBJSONString);
            } catch (JSONException e) {
                /* JSON parsing Exception */
                Log.e(TAG, "getMovieDBDataFromJson exception");
                e.printStackTrace();
            }
            return null;
        }

        private MovieDB[] getMovieDBDataFromJson(String forecastJsonStr)
                throws JSONException {
            final String RESULTS = "results";
            JSONObject moviesJson = new JSONObject(forecastJsonStr);
            JSONArray moviesArray = moviesJson.getJSONArray(RESULTS);

            MovieDB[] movieItems = new MovieDB[20];

            for(int i = 0; i < moviesArray.length(); i++) {
                JSONObject movieJson = moviesArray.getJSONObject(i);
                String poster_path = movieJson.getString("poster_path");
                String original_title = movieJson.getString("original_title");
                String release_date = movieJson.getString("release_date");
                String vote_average = movieJson.getString("vote_average");
                String overview = movieJson.getString("overview");

                MovieDB movieItem = new MovieDB(poster_path, original_title,
                                                release_date, vote_average, overview);

                movieItems[i] = movieItem;
            }
            return movieItems;
        }

        @Override
        protected void onPostExecute(MovieDB[] movieItems) {
            if (movieItems != null) {
                movieAdapter.clear();
                /* Add the Movies to Adapter */
                for (MovieDB movieItem : movieItems) {
                    movieAdapter.add(movieItem);
                }
            }
        }
    }
}
