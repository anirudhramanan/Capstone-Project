package com.mindsparkk.traveladvisor.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mindsparkk.traveladvisor.R;
import com.mindsparkk.traveladvisor.Utils.FlightListAdapter;
import com.mindsparkk.traveladvisor.Utils.ProgressWheel;
import com.mindsparkk.traveladvisor.app.MainApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Anirudh on 09/09/15.
 */
public class FlightDetailActivity extends AppCompatActivity {

    ProgressWheel progressWheel;
    RecyclerView recyclerView;
    String from, to;
    private static final String TAG_ROUTES = "routes";
    private static final String TAG_SEGMENTS = "segments";
    private static final String TAG_ITENARIES = "itineraries";
    ArrayList<Double> price = new ArrayList<>();
    ArrayList<String> sTime = new ArrayList<>();
    ArrayList<String> tTime = new ArrayList<>();
    ArrayList<String> flight = new ArrayList<>();
    ArrayList<String> airline = new ArrayList<>();
    ArrayList<Double> duration = new ArrayList<>();
    ArrayList<String> terminal = new ArrayList<>();
    FlightListAdapter flightListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flight_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        progressWheel = (ProgressWheel) findViewById(R.id.progress_wheel);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        if (getIntent().getExtras() != null) {
            from = getIntent().getStringExtra("from");
            to = getIntent().getStringExtra("to");
        }

        int columnCount = 1;
        StaggeredGridLayoutManager sglm =
                new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(sglm);

        flightListAdapter = new FlightListAdapter(this, price, sTime, tTime, flight, airline, duration, terminal);
        recyclerView.setAdapter(flightListAdapter);

        StringBuilder sb = new StringBuilder("http://free.rome2rio.com/api/1.2/json/Search?key=API_KEY");
        sb.append("&oName=" + from);
        sb.append("&dName=" + to);
        sb.append("&flags=0x000FFFF0");

        getFlightDetails(sb.toString());

    }

    public void getFlightDetails(String url) {
        JsonObjectRequest placeReq = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                progressWheel.stopSpinning();

                try {
                    JSONArray routes = jsonObject.getJSONArray(TAG_ROUTES);
                    for (int j = 0; j < routes.length(); j++) {
                        JSONObject object = routes.getJSONObject(j);
                        JSONArray segments = object.getJSONArray(TAG_SEGMENTS);
                        for (int k = 0; k < segments.length(); k++) {
                            JSONObject object1 = segments.getJSONObject(k);
                            JSONArray itenaries = object1.getJSONArray(TAG_ITENARIES);

                            for (int b = 0; b < itenaries.length(); b++) {
                                JSONObject object2 = itenaries.getJSONObject(b);

                                JSONArray legs = object2.getJSONArray("legs");

                                for (int i = 0; i < legs.length(); i++) {
                                    JSONObject object3 = legs.getJSONObject(i);
                                    JSONObject indictiveprices = object3.getJSONObject("indicativePrice");
                                    price.add(indictiveprices.getDouble("price"));
                                    Log.d("price", indictiveprices.getDouble("price") + "");

                                    JSONArray hops = object3.getJSONArray("hops");
                                    JSONObject object4 = hops.getJSONObject(0);
                                    sTime.add(object4.getString("sTime"));
                                    tTime.add(object4.getString("tTime"));
                                    flight.add(object4.getString("flight"));
                                    airline.add(object4.getString("airline"));
                                    airline.add(object4.getString("airline"));
                                    terminal.add(object4.getString("sTerminal"));
                                    duration.add(object4.getDouble("duration"));
                                }
                            }
                        }
                    }

                    flightListAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });

        MainApplication.getInstance().addToRequestQueue(placeReq);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Tracker t = ((MainApplication) getApplicationContext()).getTracker(MainApplication.TrackerName.APP_TRACKER);
        t.setScreenName("Flight Detail Screen");
        t.send(new HitBuilders.AppViewBuilder().build());
    }
}
