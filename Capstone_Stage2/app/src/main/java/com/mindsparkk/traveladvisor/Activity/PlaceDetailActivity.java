package com.mindsparkk.traveladvisor.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mindsparkk.traveladvisor.R;
import com.mindsparkk.traveladvisor.Utils.DatabaseSave;
import com.mindsparkk.traveladvisor.Utils.MyTextView;
import com.mindsparkk.traveladvisor.app.MainApplication;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Anirudh on 05/09/15.
 */
public class PlaceDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    String phone_number, place_website;
    private static final String TAG_RESULT = "result";
    private static final String TAG_VICINITY = "vicinity";
    private static final String TAG_PLACE_NAME = "name";
    private static final String TAG_OPENING_HOURS = "opening_hours";
    private static final String TAG_FORMATTED_ADDRESS = "formatted_address";
    private static final String TAG_PHONE_NUMBER = "formatted_phone_number";
    private static final String TAG_TIMETABLE = "weekday_text";
    private static final String TAG_PHOTO = "photos";
    private static final String TAG_PHOTOS_REFERENCE = "photo_reference";
    private static final String TAG_TOTAL_RATING = "user_ratings_total";
    private static final String TAG_WEBSITE = "website";
    private static final String TAG_GEOMETRY = "geometry";
    private static final String TAG_LOCATION = "location";
    private static final String TAG_LAT = "lat";
    private static final String TAG_LNG = "lng";

    DatabaseSave db;
    ImageView image, share;
    TextView place_name, place_vicinity, place_address, rating;
    LinearLayout call_now, website, timetable, saveLayout, reviews, photo_layout;
    SupportMapFragment fm;
    GoogleMap googleMap;
    double lat, lng;
    Toolbar toolbar;
    CardView card2, card3;
    ImageView saveImage;
    TextView saveText;
    int choice;
    ArrayList<String> photos_references = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_detail_layout);

        db = new DatabaseSave(this);

        toolbar = (Toolbar) findViewById(R.id.article_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        image = (ImageView) findViewById(R.id.imagePosterFull);
        saveImage = (ImageView) findViewById(R.id.saveImage);
        share = (ImageView) findViewById(R.id.share);
        place_name = (TextView) findViewById(R.id.place_name);
        place_vicinity = (TextView) findViewById(R.id.place_vicinity);
        place_address = (TextView) findViewById(R.id.address);
        saveText = (TextView) findViewById(R.id.saveText);
        call_now = (LinearLayout) findViewById(R.id.call);
        website = (LinearLayout) findViewById(R.id.website);
        saveLayout = (LinearLayout) findViewById(R.id.save);
        timetable = (LinearLayout) findViewById(R.id.timetable);
        reviews = (LinearLayout) findViewById(R.id.reviews);
        photo_layout = (LinearLayout) findViewById(R.id.photoLayout);
        rating = (TextView) findViewById(R.id.rating);
        card2 = (CardView) findViewById(R.id.card2);
        card3 = (CardView) findViewById(R.id.card3);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        fm.getMapAsync(this);
        googleMap = fm.getMap();

        final String place_id = getIntent().getStringExtra("place_id");
        choice = getIntent().getIntExtra("choice", 0);
        String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + place_id + "&key=API_KEY";

        Log.d("place_id", place_id);
        getPlaceDetail(url);

        call_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phone_number));
                startActivity(callIntent);
            }
        });

        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(website + ""));
                startActivity(intent);
            }
        });

        reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PlaceDetailActivity.this, ReviewActivity.class);
                i.putExtra("place_id", place_id);
                startActivity(i);
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);

            }
        });

        saveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (choice) {
                    case 1:

                        if (!db.getPlaces(place_id)) {
                            saveImage.setImageResource(R.drawable.favourite_icon_red);
                            saveText.setText("SAVED");
                            saveText.setTextColor(Color.RED);
                            db.addPlaces(place_id);
                        } else {
                            Snackbar.make(view, "Already Added", Snackbar.LENGTH_SHORT).show();
                        }
                        break;

                    case 2:

                        if (!db.gethotel(place_id)) {
                            saveImage.setImageResource(R.drawable.favourite_icon_red);
                            saveText.setText("SAVED");
                            saveText.setTextColor(Color.RED);
                            db.addHotels(place_id);
                        } else {
                            Snackbar.make(view, "Already Added", Snackbar.LENGTH_SHORT).show();
                        }
                        break;

                    case 3:

                        if (!db.getRes(place_id)) {
                            saveImage.setImageResource(R.drawable.favourite_icon_red);
                            saveText.setText("SAVED");
                            saveText.setTextColor(Color.RED);
                            db.addRestaurants(place_id);
                        } else {
                            Snackbar.make(view, "Already Added", Snackbar.LENGTH_SHORT).show();
                        }
                        break;

                    case 4:
                        if (!db.getPlaces(place_id)) {
                            saveImage.setImageResource(R.drawable.favourite_icon_red);
                            saveText.setText("SAVED");
                            saveText.setTextColor(Color.RED);
                            db.addPlaces(place_id);
                        } else {
                            Snackbar.make(view, "Already Added", Snackbar.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                        "Download traveladvisor to help plan your trips perfectly.");
                startActivity(Intent.createChooser(sharingIntent,
                        "Share using"));
            }
        });
    }

    public void getPlaceDetail(String url) {
        final ArrayList<String> photos_reference = new ArrayList<>();
        JsonObjectRequest movieReq = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {

                    JSONObject list = jsonObject.getJSONObject(TAG_RESULT);

                    if (list.has(TAG_PHOTO)) {
                        JSONArray photos = list.getJSONArray(TAG_PHOTO);

                        for (int j = 0; j < photos.length(); j++) {
                            JSONObject photo = photos.getJSONObject(j);

                            photos_reference.add(photo.getString(TAG_PHOTOS_REFERENCE));
                            photos_references.add(photo.getString(TAG_PHOTOS_REFERENCE));

                        }

                        Picasso.with(PlaceDetailActivity.this).load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=500&photoreference=" + photos_reference.get(0) + "&key=API_KEY").fit().into(image);
                        image.setAlpha(0.6f);

                    } else {
                        card2.setVisibility(View.GONE);
                    }

                    String placename = list.getString(TAG_PLACE_NAME);
                    place_name.setText(placename);

                    String vicinity = list.getString(TAG_VICINITY);
                    place_vicinity.setText(vicinity);

                    String address = list.getString(TAG_FORMATTED_ADDRESS);
                    place_address.setText(address);

                    JSONObject geometry = list.getJSONObject(TAG_GEOMETRY);
                    JSONObject location = geometry.getJSONObject(TAG_LOCATION);

                    lat = Double.parseDouble(location.getString(TAG_LAT));
                    lng = Double.parseDouble(location.getString(TAG_LNG));

                    double rate = list.getDouble(TAG_TOTAL_RATING);
                    rating.setText(rate + " user ratings");

                    if (list.has(TAG_OPENING_HOURS)) {
                        JSONObject opening_hours = list.getJSONObject(TAG_OPENING_HOURS);

                        JSONArray times = opening_hours.getJSONArray(TAG_TIMETABLE);

                        MyTextView textView;

                        for (int j = 0; j < times.length(); j++) {
                            String time1 = (String) times.get(j);
                            textView = new MyTextView(PlaceDetailActivity.this);
                            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                            textView.setLayoutParams(params);
                            textView.setPadding(3, 8, 3, 8);
                            textView.setText(time1);
                            textView.setTextSize(17);
                            textView.setTextColor(Color.parseColor("#2f2f2f"));

                            timetable.addView(textView);
                        }
                    } else {
                        card3.setVisibility(View.GONE);
                    }

                    place_website = list.getString(TAG_WEBSITE);
                    phone_number = list.getString(TAG_PHONE_NUMBER);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setMap(lat, lng);
                setPhotos();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });

        MainApplication.getInstance().addToRequestQueue(movieReq);
    }

    public void setMap(double lat, double lng) {
        LatLng sydney = new LatLng(lat, lng);
        googleMap.addMarker(new MarkerOptions().position(sydney).title("Here"));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 12.0f));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
    }

    public void setPhotos() {

        ImageView img;

        for (int i = 0; i < photos_references.size(); i++) {
            img = new ImageView(PlaceDetailActivity.this);

            img.setPadding(5, 0, 5, 0);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            String photo = photos_references.get(i);
            img.setLayoutParams(params);
            Log.d("reference", photos_references.get(i));

            Picasso.with(PlaceDetailActivity.this).load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=500&photoreference=" + photo + "&key=API_KEY").resize(300, 300).into(img);
            photo_layout.addView(img);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Tracker t = ((MainApplication) getApplicationContext()).getTracker(MainApplication.TrackerName.APP_TRACKER);
        t.setScreenName("Place Detail Screen");
        t.send(new HitBuilders.AppViewBuilder().build());
    }
}
