package com.mindsparkk.traveladvisor.Utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mindsparkk.traveladvisor.R;

import java.util.ArrayList;

/**
 * Created by Anirudh on 05/09/15.
 */
public class ReviewAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater mInflater;
    ArrayList<String> authorname;
    ArrayList<String> authorreview;
    ArrayList<String> authorrating;

    public ReviewAdapter(Context c, ArrayList<String> authorname, ArrayList<String> authorreview, ArrayList<String> authorrating) {
        this.ctx = c;
        mInflater = (LayoutInflater) c
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.authorname = authorname;
        this.authorreview = authorreview;
        this.authorrating = authorrating;
    }

    @Override
    public int getCount() {
        return authorname.size();
    }

    @Override
    public Object getItem(int i) {
        return authorname.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.reviews_list_item, viewGroup, false);

            holder.authorname = (TextView) convertView.findViewById(R.id.authorname);
            holder.authorreview = (TextView) convertView.findViewById(R.id.authorreview);
            holder.rating = (RatingBar) convertView.findViewById(R.id.rating);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Log.d("name", authorname.get(i));
        holder.authorname.setText(authorname.get(i));
        holder.authorreview.setText(authorreview.get(i));
        holder.rating.setRating(Float.parseFloat(authorrating.get(i)));

        return convertView;
    }

    static class ViewHolder {
        TextView authorname, authorreview;
        RatingBar rating;
    }
}
