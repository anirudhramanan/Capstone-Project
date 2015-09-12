package com.mindsparkk.traveladvisor.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mindsparkk.traveladvisor.R;
import com.mindsparkk.traveladvisor.Utils.DatabaseSave;
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
 * Created by Anirudh on 11/09/15.
 */
public class SavedListActivity extends AppCompatActivity {

    TextView title, nodata;
    DatabaseSave db;

    private PlaceListAdapter placeListAdapter;
    private List<PlaceListDetail> placeListDetailList = new ArrayList<>();
    private static final String TAG_RESULT = "result";
    private static final String TAG_PHOTOS_REFERENCE = "photo_reference";
    ProgressWheel progressWheel;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_list_layout);

        db = new DatabaseSave(this);

        progressWheel = (ProgressWheel) findViewById(R.id.progress_wheel);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        progressWheel.spin();

        int columnCount = 1;
        StaggeredGridLayoutManager sglm =
                new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(sglm);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        title = (TextView) findViewById(R.id.name);
        nodata = (TextView) findViewById(R.id.nodata);
        nodata.setVisibility(View.GONE);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        String mode = getIntent().getStringExtra("mode");
        switch (mode) {
            case "place":
                title.setText("Saved Places");

                placeListAdapter = new PlaceListAdapter(this, placeListDetailList, 1);
                recyclerView.setAdapter(placeListAdapter);

                if (db.getAllPlaces() != null) {
                    for (String id : db.getAllPlaces()) {
                        String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + id + "&key=API_KEY";
                        getPlaceDetail(url);
                    }
                } else {
                    progressWheel.stopSpinning();
                    nodata.setVisibility(View.VISIBLE);
                }

                break;

            case "restaurant":
                title.setText("Saved Restaurant");

                placeListAdapter = new PlaceListAdapter(this, placeListDetailList, 1);
                recyclerView.setAdapter(placeListAdapter);

                if (db.getAllRes().size() > 0) {
                for (String id : db.getAllRes()) {
                    String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + id + "&key=API_KEY";
                    getPlaceDetail(url);
                }
            }else{
                progressWheel.stopSpinning();
                nodata.setVisibility(View.VISIBLE);
            }

            break;

            case "hotel":
                title.setText("Saved Hotels");

                placeListAdapter = new PlaceListAdapter(this, placeListDetailList, 1);
                recyclerView.setAdapter(placeListAdapter);

                if (db.getAllHotels().size() > 0) {
                    for (String id : db.getAllHotels()) {
                        String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + id + "&key=API_KEY";
                        getPlaceDetail(url);
                    }
                } else {
                    progressWheel.stopSpinning();
                    nodata.setVisibility(View.VISIBLE);
                }

        }

    }

    public void getPlaceDetail(String url) {
        final ArrayList<String> photos_reference = new ArrayList<>();
        JsonObjectRequest movieReq = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    progressWheel.stopSpinning();
                    PlaceListDetail detail = new PlaceListDetail();

                    JSONObject list = jsonObject.getJSONObject(TAG_RESULT);

                    JSONArray photos = list.getJSONArray("photos");

                    for (int j = 0; j < photos.length(); j++) {
                        JSONObject photo = photos.getJSONObject(j);

                        photos_reference.add(photo.getString(TAG_PHOTOS_REFERENCE));
                        detail.setPhoto_reference(photos_reference);

                    }

                    String placename = list.getString("name");
                    detail.setPlace_name(placename);

                    String vicinity = list.getString("vicinity");
                    detail.setPlace_address(vicinity);
                    detail.setPlace_id(list.getString("place_id"));
                    detail.setPlace_rating(list.getDouble("rating"));
                    placeListDetailList.add(detail);

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

        MainApplication.getInstance().addToRequestQueue(movieReq);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Tracker t = ((MainApplication) getApplicationContext()).getTracker(MainApplication.TrackerName.APP_TRACKER);
        t.setScreenName("Saved Places Screen");
        t.send(new HitBuilders.AppViewBuilder().build());
    }
}
