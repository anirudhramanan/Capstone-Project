package com.mindsparkk.traveladvisor.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mindsparkk.traveladvisor.R;
import com.mindsparkk.traveladvisor.Utils.PlaceListAdapter;
import com.mindsparkk.traveladvisor.Utils.PlaceListDetail;
import com.mindsparkk.traveladvisor.Utils.ProgressWheel;
import com.mindsparkk.traveladvisor.app.MainApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anirudh on 07/09/15.
 */
public class TouristMainActivity extends AppCompatActivity {

    private PlaceListAdapter placeListAdapter;
    private List<PlaceListDetail> placeListDetailList = new ArrayList<>();
    private static final String TAG_RESULT = "results";
    private static final String TAG_ICON = "icon";
    private static final String TAG_NAME = "name";
    private static final String TAG_PLACE_ID = "place_id";
    private static final String TAG_RATING = "rating";
    private static final String TAG_ADDRESS = "vicinity";
    private static final String TAG_PHOTOS = "photos";
    private static final String TAG_PHOTOS_REFERENCE = "photo_reference";
    ProgressWheel progressWheel;
    private RecyclerView recyclerView;
    private Double latitude, longitude;
    FloatingActionButton category;
    AdView mAdView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tourist_main_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        mAdView1 = (AdView) findViewById(R.id.adView1);

        AdRequest adRequest = new AdRequest.Builder().addTestDevice("297E3429DC050C49908F854D208438C7")
                .build();
        mAdView1.loadAd(adRequest);

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
        category = (FloatingActionButton) findViewById(R.id.category);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        progressWheel.spin();

        if (getIntent().getExtras() != null) {
            latitude = getIntent().getDoubleExtra("lat", 0.0);
            longitude = getIntent().getDoubleExtra("lng", 0.0);
        }

        int columnCount = 1;
        StaggeredGridLayoutManager sglm =
                new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(sglm);

        placeListAdapter = new PlaceListAdapter(this, placeListDetailList, 1);
        recyclerView.setAdapter(placeListAdapter);

        StringBuilder sb = new StringBuilder(
                "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        sb.append("location=" + latitude + "," + longitude);
        sb.append("&types=places_of_interest|establishment");
        sb.append("&radius=30000");
        sb.append("&rankby=prominence");
        sb.append("&key=API_KEY");

        getPlaceList(sb.toString());

        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TouristMainActivity.this, TouristPlaceListActivity.class);
                i.putExtra("lat", latitude);
                i.putExtra("lng", longitude);
                startActivity(i);
                finish();
            }
        });
    }

    public void getPlaceList(String url) {
        JsonObjectRequest placeReq = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                progressWheel.stopSpinning();

                try {
                    JSONArray list = jsonObject.getJSONArray(TAG_RESULT);
                    for (int i = 0; i < list.length(); i++) {

                        JSONObject m = list.getJSONObject(i);
                        PlaceListDetail placeListDetail = new PlaceListDetail();

                        placeListDetail.setPlace_id(m.getString(TAG_PLACE_ID));
                        placeListDetail.setIcon_url(m.getString(TAG_ICON));
                        placeListDetail.setPlace_address(m.getString(TAG_ADDRESS));
                        placeListDetail.setPlace_name(m.getString(TAG_NAME));

                        if (m.has(TAG_RATING)) {
                            placeListDetail.setPlace_rating(m.getDouble(TAG_RATING));
                        }

                        if (m.has(TAG_PHOTOS)) {
                            JSONArray photos = m.getJSONArray(TAG_PHOTOS);

                            Log.d("photos", photos.toString());

                            for (int j = 0; j < photos.length(); j++) {
                                JSONObject photo = photos.getJSONObject(j);

                                ArrayList<String> photos_reference = new ArrayList<>();
                                photos_reference.add(photo.getString(TAG_PHOTOS_REFERENCE));

                                placeListDetail.setPhoto_reference(photos_reference);
                            }
                        }

                        placeListDetailList.add(placeListDetail);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                placeListAdapter.notifyDataSetChanged();
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
        t.setScreenName("Tourist Places Screen");
        t.send(new HitBuilders.AppViewBuilder().build());
    }
}
