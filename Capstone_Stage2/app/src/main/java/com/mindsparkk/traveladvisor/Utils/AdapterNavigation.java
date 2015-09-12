package com.mindsparkk.traveladvisor.Utils;

/**
 * Created by Anirudh on 22/06/15.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindsparkk.traveladvisor.R;
import com.parse.ParseUser;

public class AdapterNavigation extends ArrayAdapter<String> {

    Context ctx;
    private String[] names;
    private TypedArray icons;
    LayoutInflater inflater;
    int resourceId;
    ParseUser user;

    public AdapterNavigation(Context context, int resource, String[] name, TypedArray icons) {
        super(context, resource);

        this.ctx = context;
        this.resourceId = resource;
        this.names = name;
        this.icons = icons;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        user = ParseUser.getCurrentUser();
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public String getItem(int position) {
        return names[position];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        holder = new ViewHolder();
        if (position == 0) {
            convertView = inflater.inflate(R.layout.profile_navigation, parent, false);

            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.email = (TextView) convertView.findViewById(R.id.email);
            if (user != null) {
                holder.name.setText(user.getString("name"));
                holder.email.setText(user.getEmail());
            } else {
                holder.name.setText("Guest User");
            }

        } else {
            convertView = inflater.inflate(resourceId, parent, false);

            holder.nameTxt = (TextView) convertView.findViewById(R.id.text_navigationlist);
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon_navigationlist);

            holder.nameTxt.setText(names[position]);
            holder.imageView.setImageDrawable(icons.getDrawable(position));
        }

        return convertView;
    }

    class ViewHolder {
        TextView nameTxt;
        ImageView imageView;
        TextView name, email;

    }
}

