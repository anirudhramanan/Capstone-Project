package com.mindsparkk.traveladvisor.Activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mindsparkk.traveladvisor.R;
import com.mindsparkk.traveladvisor.Utils.SlidingTabLayout;
import com.mindsparkk.traveladvisor.Utils.ViewPagerAdapterTourist;
import com.mindsparkk.traveladvisor.app.MainApplication;

/**
 * Created by Anirudh on 06/09/15.
 */
public class TouristPlaceListActivity extends AppCompatActivity {

    ViewPager pager;
    ViewPagerAdapterTourist adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[] = {"TEMPLE", "MUSEUMS", "NIGHT CLUB"};
    int Numboftabs = 3;
    TextView nametitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycle_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        nametitle = (TextView) findViewById(R.id.name);
        nametitle.setText("Tourist Spots");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //<-------------------------------Setting adapter for view pager---------------------------->
        adapter = new ViewPagerAdapterTourist(getFragmentManager(), Titles, Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);

        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Tracker t = ((MainApplication) getApplicationContext()).getTracker(MainApplication.TrackerName.APP_TRACKER);
        t.setScreenName("Toursit Places Category-wise");
        t.send(new HitBuilders.AppViewBuilder().build());
    }
}
