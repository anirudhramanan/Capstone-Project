package com.mindsparkk.traveladvisor.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.mindsparkk.traveladvisor.NavigationDrawerFragment;
import com.mindsparkk.traveladvisor.R;
import com.mindsparkk.traveladvisor.Utils.PlaceAutocompleteAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        TextView gps;
        AutoCompleteTextView enter_place;
        protected GoogleApiClient mGoogleApiClient;


        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
                new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

        Toolbar toolbar;
        ListView listview;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            toolbar = (Toolbar) rootView.findViewById(R.id.tool_bar);
            ((MainActivity) getActivity()).setSupportActionBar(toolbar);
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("");

            toolbar.setNavigationIcon(R.drawable.ic_drawer);
            listview = (ListView) rootView.findViewById(R.id.places);

            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(Places.GEO_DATA_API)
                    .build();

            enter_place = (AutoCompleteTextView) rootView.findViewById(R.id.enterplace);

            final PlaceAutocompleteAdapter mAdapter = new PlaceAutocompleteAdapter(getActivity(), android.R.layout.simple_list_item_1,
                    mGoogleApiClient, BOUNDS_GREATER_SYDNEY, null);
            enter_place.setAdapter(mAdapter);

            enter_place.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    final PlaceAutocompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(i);
                    final String placeId = String.valueOf(item.placeId);

                    Intent i1 = new Intent(getActivity(), SelectOptionActivity.class);
                    i1.putExtra("placeid", placeId);
                    i1.putExtra("name", item.description);
                    Log.d("id", placeId);
                    startActivity(i1);

                }
            });

            final ArrayList<String> places_name = new ArrayList<>();
            final ArrayList<String> places_name_id = new ArrayList<>();

            places_name.add("New Delhi, India");
            places_name.add("Bangalore, Karnataka, India");
            places_name.add("Gujarat, India");
            places_name.add("Himachal Pradesh, India");
            places_name.add("Chennai, Tamil Nadu, India");
            places_name.add("Kerala, India");
            places_name.add("Mumbai, Maharashtra, India");
            places_name.add("Goa, India");
            places_name.add("Jammu & Kashmir");
            places_name.add("New York, NY, United States");
            places_name.add("Sydney, New South Wales, Australia");

            places_name_id.add("ChIJLbZ-NFv9DDkRzk0gTkm3wlI");
            places_name_id.add("ChIJbU60yXAWrjsR4E9-UejD3_g");
            places_name_id.add("ChIJlfcOXx8FWTkRLlJU7YfYG4Y");
            places_name_id.add("ChIJ9wH5Z8NTBDkRJXdLVsUE_nw");
            places_name_id.add("ChIJYTN9T-plUjoRM9RjaAunYW4");
            places_name_id.add("ChIJW_Wc1P8SCDsRmXw47fuQvWQ");
            places_name_id.add("ChIJwe1EZjDG5zsRaYxkjY_tpF0");
            places_name_id.add("ChIJQbc2YxC6vzsRkkDzYv-H-Oo");
            places_name_id.add("ChIJnaj_mSQJ4TgR8eeXRm16VgY");
            places_name_id.add("ChIJOwg_06VPwokRYv534QaPC8g");
            places_name_id.add("ChIJP3Sa8ziYEmsRUKgyFmh9AQM");

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, places_name);
            listview.setAdapter(arrayAdapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    final String placeId = places_name_id.get(i);
                    Intent i1 = new Intent(getActivity(), SelectOptionActivity.class);
                    i1.putExtra("placeid", placeId);
                    i1.putExtra("name", places_name.get(i));
                    Log.d("placeid", placeId);
                    startActivity(i1);
                    getActivity().overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);

                }
            });

            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
        }

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {

        }

        @Override
        public void onStart() {
            super.onStart();
            if (mGoogleApiClient != null)
                mGoogleApiClient.connect();
        }

        @Override
        public void onStop() {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                mGoogleApiClient.disconnect();
            }
            super.onStop();
        }
    }

}
