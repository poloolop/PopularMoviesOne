package in.umangsaini.popularmovies;

import java.io.Serializable;

/**
 * Created by Umang Saini on 20/02/2016.
 */
public class MovieDB implements Serializable {
    String image;
    String title;
    String release_date;
    String vote_average;
    String overview_text;

    public MovieDB(String image, String title, String release_date,
                   String vote_average, String overview_text) {
        this.image = image;
        this.title = title;
        this.release_date = release_date;
        this.vote_average = vote_average;
        this.overview_text = overview_text;
    }
}
