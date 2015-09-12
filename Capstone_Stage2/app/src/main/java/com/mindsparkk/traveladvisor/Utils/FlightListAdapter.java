package com.mindsparkk.traveladvisor.Utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindsparkk.traveladvisor.R;

import java.util.ArrayList;

/**
 * Created by Anirudh on 09/09/15.
 */
public class FlightListAdapter extends RecyclerView.Adapter<FlightListAdapter.ViewHolder> {

    Context context;
    private LayoutInflater mInflater;
    ArrayList<String> stime, time, flight, airline, termnal;
    ArrayList<Double> duration, price;

    public FlightListAdapter(Context c, ArrayList<Double> price, ArrayList<String> stime, ArrayList<String> ttime, ArrayList<String> flight, ArrayList<String> airline, ArrayList<Double> duration, ArrayList<String> terminal) {
        this.context = c;
        mInflater = (LayoutInflater) c
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.price = price;
        this.stime = stime;
        this.time = ttime;
        this.flight = flight;
        this.airline = airline;
        this.termnal = terminal;
        this.duration = duration;
    }

    @Override
    public FlightListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.flight_list_item, viewGroup, false);
        final ViewHolder vh = new ViewHolder(view);

        return vh;
    }

    public Drawable getIcon(int i) {
        switch (airline.get(i)) {
            case "6E":
                return context.getResources().getDrawable(R.drawable.indigo_logo);
            case "AI":
                return context.getResources().getDrawable(R.drawable.airindia_logo);
            case "AC":
                return context.getResources().getDrawable(R.drawable.aircanada_logo);
            case "TK":
                return context.getResources().getDrawable(R.drawable.turkish_logo);
            case "ET":
                return context.getResources().getDrawable(R.drawable.ethiopian_logo);
            case "G8":
                return context.getResources().getDrawable(R.drawable.goair_logo);
            case "9W":
                return context.getResources().getDrawable(R.drawable.jetairways_logo);
            case "S2":
                return context.getResources().getDrawable(R.drawable.jetlite_logo);
            case "UK":
                return context.getResources().getDrawable(R.drawable.vistara_logo);
            case "SG":
                return context.getResources().getDrawable(R.drawable.spicejet_logo);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(FlightListAdapter.ViewHolder viewHolder, int i) {

        viewHolder.airline.setText(airline.get(i));
        viewHolder.price.setText("Rs. " + price.get(i) * 60);
        viewHolder.stime.setText(stime.get(i));
        viewHolder.ttime.setText(time.get(i));
        viewHolder.flight.setText(flight.get(i));
        viewHolder.terminal.setText("Departure Terminal : " + termnal.get(i));

        viewHolder.flight_icon.setImageDrawable(getIcon(i));

    }

    @Override
    public int getItemCount() {
        return flight.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView airline, terminal, stime, ttime, price, flight;
        ImageView flight_icon;

        public ViewHolder(View itemView) {
            super(itemView);

            airline = (TextView) itemView.findViewById(R.id.airline);
            terminal = (TextView) itemView.findViewById(R.id.terminal);
            stime = (TextView) itemView.findViewById(R.id.sTime);
            flight = (TextView) itemView.findViewById(R.id.flight);
            ttime = (TextView) itemView.findViewById(R.id.tTime);
            price = (TextView) itemView.findViewById(R.id.price);
            flight_icon = (ImageView) itemView.findViewById(R.id.flight_icon);

        }
    }
}
