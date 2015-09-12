package com.mindsparkk.traveladvisor.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mindsparkk.traveladvisor.R;
import com.mindsparkk.traveladvisor.app.MainApplication;

/**
 * Created by Anirudh on 08/09/15.
 */
public class FlightActivity extends AppCompatActivity {

    RelativeLayout from, to, dateLayout;
    TextView fromname, toname;
    static TextView date;
    Button search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flight_main_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);


        from = (RelativeLayout) findViewById(R.id.from);
        to = (RelativeLayout) findViewById(R.id.to);
        //dateLayout = (RelativeLayout) findViewById(R.id.datelayout);
        fromname = (TextView) findViewById(R.id.fromName);
        toname = (TextView) findViewById(R.id.toName);
        //date = (TextView) findViewById(R.id.date);
        search = (Button) findViewById(R.id.search);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCities(1);
            }
        });

        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCities(2);
            }
        });

       /* dateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = new StartDatePicker();
                dialogFragment.show(getSupportFragmentManager(), "start_date_picker");
            }
        });*/

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FlightActivity.this, FlightDetailActivity.class);
                i.putExtra("from", fromname.getText());
                i.putExtra("to", toname.getText());
                startActivity(i);
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);

            }
        });

    }

    /*static Calendar c = Calendar.getInstance();
    static int startYear = c.get(Calendar.YEAR);
    static int startMonth = c.get(Calendar.MONTH);
    static int startDay = c.get(Calendar.DAY_OF_MONTH);

    public static class StartDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            // Use the current date as the default date in the picker
            DatePickerDialog dialog = new DatePickerDialog(getContext(), this, startYear, startMonth, startDay);
            return dialog;

        }

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            // Do something with the date chosen by the user
            startYear = year;
            startMonth = monthOfYear;
            startDay = dayOfMonth;
            date.setText(startDay + " " + startMonth);
        }
    }*/

    private void showCities(final int option) {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(
                FlightActivity.this);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                FlightActivity.this,
                android.R.layout.select_dialog_singlechoice);

        arrayAdapter.add("Mumbai");
        arrayAdapter.add("Delhi");
        arrayAdapter.add("Bangkok");
        arrayAdapter.add("Bangalore");
        arrayAdapter.add("Pune");
        arrayAdapter.add("Hyderabad");
        arrayAdapter.add("Kolkata");
        arrayAdapter.add("Chennai");
        arrayAdapter.add("Goa");
        arrayAdapter.add("Dubai");
        arrayAdapter.add("Singapore");
        arrayAdapter.add("Kathmandu");

        builderSingle.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(arrayAdapter,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        if (option == 1)
                            fromname.setText(strName);
                        else
                            toname.setText(strName);
                    }
                });
        builderSingle.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Tracker t = ((MainApplication) getApplicationContext()).getTracker(MainApplication.TrackerName.APP_TRACKER);
        t.setScreenName("Flight Screen");
        t.send(new HitBuilders.AppViewBuilder().build());
    }

}

