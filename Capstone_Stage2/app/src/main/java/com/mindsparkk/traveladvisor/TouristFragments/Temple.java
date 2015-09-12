package com.mindsparkk.traveladvisor.TouristFragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
 * Created by Anirudh on 06/09/15.
 */
public class Temple extends Fragment {

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_restaurant, container, false);

        progressWheel = (ProgressWheel) v.findViewById(R.id.progress_wheel);
        progressWheel.spin();

        latitude = getActivity().getIntent().getDoubleExtra("lat", 0.0);
        longitude = getActivity().getIntent().getDoubleExtra("lng", 0.0);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        int columnCount = 1;
        StaggeredGridLayoutManager sglm =
                new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(sglm);

        placeListAdapter = new PlaceListAdapter(getActivity(), placeListDetailList, 1);
        recyclerView.setAdapter(placeListAdapter);

        StringBuilder sb = new StringBuilder(
                "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        sb.append("location=" + latitude + "," + longitude);
        sb.append("&types=hindu_temple|mosque");
        sb.append("&radius=30000");
        sb.append("&rankby=prominence");
        sb.append("&key=API_KEY");

        getPlaceList(sb.toString());

        return v;
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
}
