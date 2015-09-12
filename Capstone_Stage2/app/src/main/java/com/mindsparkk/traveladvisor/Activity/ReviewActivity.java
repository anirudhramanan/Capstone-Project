package com.mindsparkk.traveladvisor.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mindsparkk.traveladvisor.R;
import com.mindsparkk.traveladvisor.Utils.ProgressWheel;
import com.mindsparkk.traveladvisor.Utils.ReviewAdapter;
import com.mindsparkk.traveladvisor.app.MainApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Anirudh on 05/09/15.
 */
public class ReviewActivity extends AppCompatActivity {

    String place_id;

    ArrayList<String> author_name = new ArrayList<>();
    ArrayList<String> author_review = new ArrayList<>();
    ArrayList<String> author_rating = new ArrayList<>();
    ReviewAdapter adapter;
    ListView listView;
    ProgressWheel progressWheel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reviews_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        progressWheel = (ProgressWheel) findViewById(R.id.progress_wheel);
        progressWheel.spin();
        listView = (ListView) findViewById(R.id.list);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (getIntent().getExtras() != null) {
            place_id = getIntent().getStringExtra("place_id");
        }

        String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + place_id + "&key=API_KEY";

        adapter = new ReviewAdapter(this, author_name, author_review, author_rating);
        listView.setAdapter(adapter);

        getReviews(url);
    }

    public void getReviews(String url) {
        JsonObjectRequest movieReq = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                progressWheel.stopSpinning();
                try {

                    JSONObject list = jsonObject.getJSONObject("result");
                    JSONArray reviews = list.getJSONArray("reviews");

                    for (int k = 0; k < reviews.length(); k++) {
                        author_name.add(reviews.getJSONObject(k).getString("author_name"));
                        author_review.add(reviews.getJSONObject(k).getString("text"));
                        author_rating.add(String.valueOf(reviews.getJSONObject(k).getDouble("rating")));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });

        MainApplication.getInstance().addToRequestQueue(movieReq);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Tracker t = ((MainApplication) getApplicationContext()).getTracker(MainApplication.TrackerName.APP_TRACKER);
        t.setScreenName("Review Screen");
        t.send(new HitBuilders.AppViewBuilder().build());
    }
}
