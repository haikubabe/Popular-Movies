package com.example.android.movie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.HttpCookie;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by sreemoyee on 27/8/15.
 */
public class DetailFragment extends Fragment {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    private static final int DETAIL_LOADER = 0;
    private static final String MESSAGE = "MovieDetails";
    private static final String REVIEW_MESSAGE = "ReviewDetails";
    static final String DETAIL_URI = "URI";
    public String REVIEW_URL, VIDEO_URL, URL;
    public String id, review, trailer, runtime, genre, key;
    public String[] trailerLinks;

    List<String> ids;

    final String OWM_RESULT = "results";
    final String OWM_TITLE = "original_title";
    final String OWM_POSTER = "poster_path";
    final String OWM_RELEASE_DATE = "release_date";
    final String OWM_SYNOPSIS = "overview";
    final String OWM_RATING = "vote_average";
    final String OWM_GENRE_ID = "genre_ids";
    final String OWM_ID = "id";
    final String OWM_AUTHOR = "author";
    final String OWM_CONTENT = "content";
    final String OWM_RUNTIME = "runtime";
    final String OWM_NAME = "name";
    final String OWM_GENRE = "genres";

    LinearLayout videosContainer;

    TextView movieTitle;
    TextView movieRating;
    TextView movieSynopsis;
    TextView movieReleaseDate;
    TextView movieGenres;
    TextView movieReview;
    ImageView moviePoster;
    TextView movieTrailer;
    TextView movieTrailerLinks;
    TextView movieRunTime;
    TextView trailerNotAvailable;
    Button openTrailerButton;
    ImageView button;
    ImageView favoriteImg;

    public DetailFragment() {

    }

    private void updateBackground() {
        BackgroundTask backgroundTask = new BackgroundTask();
        backgroundTask.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Intent intent = getActivity().getIntent();
        String jsonString = intent.getStringExtra(MESSAGE);
        //String reviewString = intent.getStringExtra(REVIEW_MESSAGE);

        try {

            final JSONObject jObj = new JSONObject(jsonString);
            //final JSONObject reviewObj = new JSONObject(reviewString);

            movieTitle = (TextView) rootView.findViewById(R.id.movie_title);
            movieTitle.setText(jObj.getString(OWM_TITLE));
            //getString(R.string.title_activity_detail, jObj.getString(OWM_TITLE));

            moviePoster = (ImageView) rootView.findViewById(R.id.movie_poster);
            String basepath = "http://image.tmdb.org/t/p/w185/";
            String relativePath = jObj.getString(OWM_POSTER);

            Glide.with(getActivity()).load(basepath + relativePath).into(moviePoster);

            movieRating = (TextView) rootView.findViewById(R.id.movie_rating);
            Float rating = Float.valueOf(jObj.getString(OWM_RATING));
            movieRating.setText("Rating: " + rating + "/10");

            RatingBar ratingBar = (RatingBar) rootView.findViewById(R.id.rating_bar);
            ratingBar.setRating(rating);

            movieSynopsis = (TextView) rootView.findViewById(R.id.movie_synopsis);
            movieSynopsis.setText(jObj.getString(OWM_SYNOPSIS));
            if(jObj.getString(OWM_SYNOPSIS) == "null") {
                movieSynopsis.setText("Synopsis is not available for this movie");
            }

            movieReleaseDate = (TextView) rootView.findViewById(R.id.movie_release_date);
            movieReleaseDate.setText(jObj.getString(OWM_RELEASE_DATE));

            movieGenres = (TextView) rootView.findViewById(R.id.genres);

            /*JSONArray jArray = jObj.getJSONArray(OWM_GENRE_ID);
            StringBuilder builder = new StringBuilder();

            Map<Integer, String> genreMap = new HashMap<Integer, String>() {
                {
                    put(28, "Action");
                    put(12, "Adventure");
                    put(16, "Animation");
                    put(35, "Comedy");
                    put(80, "Crime");
                    put(99, "Documentary");
                    put(18, "Drama");
                    put(10751, "Family");
                    put(14, "Fantasy");
                    put(10769, "Foreign");
                    put(36, "History");
                    put(27, "Horror");
                    put(10402, "Music");
                    put(9648, "Mystery");
                    put(10749, "Romance");
                    put(878, "Science Fiction");
                    put(10770, "TV Movie");
                    put(53, "Thriller");
                    put(10752, "War");
                    put(37, "Western");
                }
            };

            //ArrayList<Integer> genreList = new ArrayList<Integer>();

            for (int i = 0; i < jArray.length(); i++) {
                int val = jArray.getInt(i);
                for (Map.Entry<Integer, String> entry : genreMap.entrySet()) {
                    int key = entry.getKey();
                    String value = entry.getValue();
                    if (val == key) {
                        builder.append(value + " ");
                        movieGenres.setText(builder);
                    }
                }
            }*/

            String date = (String) movieReleaseDate.getText().toString();
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date convertedDate = formatter.parse(date);
                SimpleDateFormat postFormatter = new SimpleDateFormat("yyyy MMM dd");
                String finalDate = postFormatter.format(convertedDate);
                movieReleaseDate.setText(finalDate);
                //String finalDate = formatter("YYYY MMM DD").format(convertedDate);
            } catch (ParseException e) {
                Log.e(LOG_TAG, "Error Parsing Date: ", e);
            }


            movieReview = (TextView) rootView.findViewById(R.id.review);
            //movieTrailer = (TextView) rootView.findViewById(R.id.trailer);
            //movieTrailerLinks = (TextView) rootView.findViewById(R.id.trailer_links);
            movieRunTime = (TextView) rootView.findViewById(R.id.runtime);

            videosContainer = (LinearLayout) rootView.findViewById(R.id.movie_detail_videos_container);

            id = jObj.getString(OWM_ID);
            REVIEW_URL = "http://api.themoviedb.org/3/movie/" + id + "/reviews?api_key=" + getString(R.string.API_KEY);
            VIDEO_URL = "http://api.themoviedb.org/3/movie/" + id + "/videos?api_key=" + getString(R.string.API_KEY);
            URL = "http://api.themoviedb.org/3/movie/" + id + "?api_key=" + getString(R.string.API_KEY) + "&append_to_response=reviews,trailers";

            updateBackground();

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error Parsing JSON: ", e);
        }

        return rootView;
    }


    public class BackgroundTask extends AsyncTask<Void, Void, Void> {

        StringBuilder str1 = new StringBuilder();
        List<String> str3 = new ArrayList<String>();
        List<String> str2 = new ArrayList<String>();
        StringBuilder str4 = new StringBuilder();
        //String[] str2;

        private final String LOG_TAG = BackgroundTask.class.getSimpleName();

        @Override
        protected Void doInBackground(Void... params) {

            JSONParser jParser = new JSONParser();
            JSONObject json1 = jParser.getJSONFromUrl(REVIEW_URL);
            JSONObject json2 = jParser.getJSONFromUrl(VIDEO_URL);
            JSONObject json3 = jParser.getJSONFromUrl(URL);
            JSONObject json4 = jParser.getJSONFromUrl(URL);

            JSONArray reviewArray;
            JSONArray videoArray;
            JSONArray genreArray;

            try {

                reviewArray = json1.getJSONArray(OWM_RESULT);
                videoArray = json2.getJSONArray(OWM_RESULT);
                genreArray = json4.getJSONArray(OWM_GENRE);

                for (int i = 0; i < reviewArray.length(); i++) {

                    JSONObject review = reviewArray.getJSONObject(i);
                    String author = "<b>" + review.getString(OWM_AUTHOR) + "</b>";
                    str1.append(author + ": " + review.getString(OWM_CONTENT) + "<br><br>");
                }

                trailerLinks = new String[videoArray.length()];

                for (int j = 0; j < videoArray.length(); j++) {
                    JSONObject video = videoArray.getJSONObject(j);
                    key = video.getString("key");
                    str2.add(video.getString(OWM_NAME) + "<br><br>");
                    //trailer = "http://www.youtube.com/watch?v=" + key;
                    str3.add(key);
                    //trailerLinks[j] = "http://www.youtube.com/watch?v=" + key;
                }

                for (int k = 0; k < genreArray.length(); k++) {
                    JSONObject genre = genreArray.getJSONObject(k);
                    String name = genre.getString(OWM_NAME);
                    str4.append(name + " ");
                }

                runtime = json3.getString(OWM_RUNTIME);

            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error parsing JSON:", e);
            }

            review = str1.toString();
            genre = str4.toString();

            return null;
        }

        @Override
        protected void onPostExecute(Void avoid) {
            super.onPostExecute(avoid);
            movieReview.setText(Html.fromHtml(review));
            if (review.isEmpty()) {
                //reviewsHeader.setVisibility(View.INVISIBLE);
                movieReview.setText("No reviews are there for this movie");
            }
            movieRunTime.setText(runtime + " min");
            if(runtime == "null" || runtime == "0") {
                movieRunTime.setText("Runtime is not available");
            }
            movieGenres.setText(genre);
            if(genre.isEmpty()) {
                movieGenres.setText("Genre is not present");
            }
            /*for (String s : str2) {
                movieTrailer.append(Html.fromHtml(s));

                movieTrailer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (String t : str3) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + t)));
                        }
                    }
                });
            }*/


            LayoutInflater inflater = LayoutInflater.from(getActivity());
            for (int i = 0; i < str2.size(); i++) {
                String name = str2.get(i);

                View videoView = inflater.inflate(R.layout.movie_video_button, videosContainer, false);
                openTrailerButton = (Button) videoView.findViewById(R.id.video_start_button);
                openTrailerButton.setText(getString(R.string.movie_detail_open_trailer_button_text, i + 1));
                openTrailerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (String t : str3) {
                            Uri youtubeUri = Uri.parse("http://www.youtube.com/watch?v=" + t);
                            Intent intent = new Intent(Intent.ACTION_VIEW, youtubeUri);
                            startActivity(intent);
                        }

                    }
                });

                videosContainer.addView(videoView);

            }
            if (str2.isEmpty()) {
                View notAvailableVideo = inflater.inflate(R.layout.movie_video_text, videosContainer, false);
                trailerNotAvailable = (TextView) notAvailableVideo.findViewById(R.id.video_text);
                trailerNotAvailable.setText("No trailers are available for this movie");
                videosContainer.addView(notAvailableVideo);
            }

        }

    }

}


