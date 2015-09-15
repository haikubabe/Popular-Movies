package com.example.android.movie;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieFragment extends Fragment {

    public static final String LOG_TAG = MovieFragment.class.getSimpleName();

    public static final int MOVIE_LOADER = 0;
    private ImageAdapter imageAdapter;
    public ArrayList<String > rev_arr = new ArrayList<String>();


    public MovieFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_movie_list, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateMovie();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        return rootView;
    }

    private void updateMovie() {
        FetchMoviesTask moviesTask = new FetchMoviesTask();
        moviesTask.execute();
    }

    public void onStart() {
        super.onStart();
        updateMovie();
    }

    public class FetchMoviesTask extends AsyncTask<Void, Void, JSONObject> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
        private final String MESSAGE = "MovieDetails";
        private final String REVIEW_MESSAGE = "ReviewDetails";


        private boolean DEBUG = true;
        private ProgressDialog progress;

        protected void onPreExecute() {
            progress=new ProgressDialog(getActivity());
            progress.show();
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            // URL for calling the API is needed
            final String API_KEY = getString(R.string.API_KEY);

            final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";

            final String OWM_APIKEY = "api_key";
            final String OWM_SORT_BY = "sort_by";

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sort_by = prefs.getString(getString(R.string.pref_movie_key), getString(R.string.pref_sort_default_value));

            //Built the uri
            Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendQueryParameter(OWM_APIKEY, API_KEY)
                    .appendQueryParameter(OWM_SORT_BY, sort_by)
                    .build();


            String url = builtUri.toString();

            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.getJSONFromUrl(url);

            return json; //the return value will be used by onPostExecute to update UI
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(final JSONObject json) {

            progress.dismiss();

            //JSON objects that need to be extracted
            final String OWM_RESULT = "results";
            final String OWM_ID = "id";
            final String OWM_TITLE = "original_title";
            final String OWM_SYNOPSIS = "overview";
            final String OWM_POSTER_PATH = "poster_path";
            final String OWM_RATING = "vote_average";
            final String OWM_RELEASE_DATE = "release_date";
            final String OWM_POPULARITY = "popularity";
            final String OWM_GENRE = "genre_ids";
            final String OWM_GENRES = "genres";
            String[] content;
            //final String BASE_URL = "http://api.themoviedb.org/3/movie/";

            List<String> poster_paths = new ArrayList<String>();
            List<String> titles = new ArrayList<String>();

            JSONArray movies_list_array;
            JSONArray reviews_list_array;

            if(json != null) {
                try {

                    movies_list_array = json.getJSONArray(OWM_RESULT);

                    for (int i = 0; i < movies_list_array.length(); i++) {

                        JSONObject movie = movies_list_array.getJSONObject(i);
                        poster_paths.add(movie.getString(OWM_POSTER_PATH));
                        titles.add(movie.getString(OWM_TITLE));
                    }

                } catch (JSONException e) {
                    Log.e(LOG_TAG, "Error parsing JSON:", e);
                }


                GridView gridview = (GridView) getActivity().findViewById(R.id.movies_list_grid);
                gridview.setAdapter(new ImageAdapter(getActivity(), poster_paths));

                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                        try {
                            JSONObject movieDetails = json.getJSONArray(OWM_RESULT).getJSONObject(position);
                            //JSONObject reviewDetails = json[1].getJSONArray(OWM_RESULT).getJSONObject(position);
                            Intent intent = new Intent(getActivity(), DetailActivity.class)
                                    .putExtra(MESSAGE, movieDetails.toString());
                                    //.putExtra(REVIEW_MESSAGE, reviewDetails.toString());
                            startActivity(intent);

                        } catch (JSONException e) {
                            Log.e(LOG_TAG, "Error parsing json", e);
                        }

                    }
                });

            } else {

                try {
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

                    alertDialog.setTitle("Info");
                    alertDialog.setMessage("Internet not available, Cross check your internet connectivity and try again");
                    alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    });

                    alertDialog.show();
                }
                catch(Exception e)
                {
                    Log.d(LOG_TAG, "Show Dialog: "+e.getMessage());
                }
            }
        }
    }

}

