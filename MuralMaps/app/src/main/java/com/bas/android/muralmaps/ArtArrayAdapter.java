package com.bas.android.muralmaps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mitchell on 10/22/2017.
 */

public class ArtArrayAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Art> mDataSource;

    public ArtArrayAdapter(Context context, ArrayList<Art> items){
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return mDataSource.size();
    }
    @Override
    public Art getItem(int position) {
        return mDataSource.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = mInflater.inflate(R.layout.art_cell, parent, false);

        TextView name = (TextView) rowView.findViewById(R.id.art_label);
        name.setText(getItem(position).getName());

        ImageView artImage = (ImageView) rowView.findViewById(R.id.art_image);
        Bitmap bmp = BitmapFactory.decodeByteArray(getItem(position).getImage(), 0, getItem(position).getImage().length);
        artImage.setImageBitmap(bmp);

        return rowView;
    }



}

