package com.example.android.movie;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.movie.data.MovieContract;

import java.util.List;

/**
 * Created by sreemoyee on 18/8/15.
 */
public class ImageAdapter extends BaseAdapter {

    Context context;
    List<String> poster_path;

    public ImageAdapter(Context context, List<String> poster_path) {
        this.context = context;
        this.poster_path = poster_path;
    }

    @Override
    public int getCount() {
        return poster_path.toArray().length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;

        if (convertView == null) {
            // if it's not recycled, initialize with new ImageView
            imageView = new ImageView(context);
        } else {
            imageView = (ImageView) convertView; //recylcing the same view
        }


        //setup the poster path URL
        String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185/";
        String relativePath=poster_path.get(position);

        Glide.with(context).load(IMAGE_BASE_URL + relativePath).into(imageView);
        return imageView;

    }

}