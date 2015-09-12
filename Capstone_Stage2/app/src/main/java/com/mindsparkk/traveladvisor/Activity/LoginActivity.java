package com.mindsparkk.traveladvisor.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mindsparkk.traveladvisor.R;
import com.mindsparkk.traveladvisor.app.MainApplication;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    EditText usernameTxt, passwordTxt;
    String username, password;
    ImageView cross;
    Button login;
    TextView newuser;
    ProgressDialog progressDialog;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;
    TextView changePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        passwordTxt = (EditText) findViewById(R.id.passwordTxt);
        usernameTxt = (EditText) findViewById(R.id.usernameTxt);
        login = (Button) findViewById(R.id.login);
        newuser = (TextView) findViewById(R.id.newuser);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToParse();
            }
        });

        newuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
                finish();*/
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Verifying Credentials...");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    public void showForgetDialog() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Sending...");
        progressDialog.setCanceledOnTouchOutside(false);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Forgot Password");
        alert.setMessage("Please entered the registered email address to receive the password reset link.");

        final EditText input = new EditText(LoginActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(15, 10, 15, 10);
        input.setLayoutParams(lp);
        alert.setView(input);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                progressDialog.show();
                if (input.getText().length() != 0) {

                    ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
                    parseQuery.whereEqualTo("email", input.getText().toString());
                    parseQuery.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> list, ParseException e) {
                            if (e == null) {
                                if (list.size() != 0) {
                                    for (int i = 0; i < list.size(); i++) {
                                        ParseUser user = list.get(i);

                                        if (!ParseFacebookUtils.isLinked(user)) {
                                            ParseUser.requestPasswordResetInBackground(user.getEmail(), new RequestPasswordResetCallback() {
                                                public void done(ParseException e) {
                                                    if (e == null) {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(LoginActivity.this, "Link sent successfully", Toast.LENGTH_SHORT).show();
                                                        // An email was successfully sent with reset instructions.
                                                    } else {
                                                        // Something went wrong. Look at the ParseException to see what's up.
                                                    }
                                                }
                                            });
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(LoginActivity.this, "User registered with facebook!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(LoginActivity.this, "Email id not registered!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

                } else {
                    Toast.makeText(LoginActivity.this, "Please enter the email address.", Toast.LENGTH_SHORT).show();
                }


                dialog.cancel();


            }
        });

        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alert.create();
        alertDialog.show();
        TextView msgTxt = (TextView) alertDialog.findViewById(android.R.id.message);
        Button pos = (Button) alertDialog.findViewById(android.R.id.button1);
        Button pos1 = (Button) alertDialog.findViewById(android.R.id.button2);

        pos1.setTextColor(Color.DKGRAY);
        pos.setTextColor(this.getResources().getColor(R.color.colorPrimary));
        pos.setTypeface(Typeface.DEFAULT_BOLD);
        pos1.setTypeface(Typeface.DEFAULT_BOLD);

        msgTxt.setTextSize(14);
        pos.setTextSize(13);
        pos1.setTextSize(13);
    }

    private void sendDataToParse() {
        if (passwordTxt.getText().length() != 0 && usernameTxt.getText().length() != 0) {

            progressDialog.show();

            username = usernameTxt.getText().toString();
            password = passwordTxt.getText().toString();

            ParseUser.logInInBackground(username, password,
                    new LogInCallback() {
                        public void done(ParseUser user, ParseException e) {
                            if (user != null) {
                                progressDialog.dismiss();
                                // If user exist and authenticated, send user to Welcome.class
                                Intent intent = new Intent(
                                        LoginActivity.this,
                                        MainActivity.class);
                                intent.putExtra("login_from", "custom");
                                Loginfrom("custom");
                                startActivity(intent);
                                finish();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Wrong username or password",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } else {
            if (usernameTxt.getText().length() == 0) {
                usernameTxt.setError("Cannot be left blank");
            }
            if (passwordTxt.getText().length() == 0) {
                passwordTxt.setError("Cannot be left blank");
            }
        }
    }

    //method to store from where the user logged in..............
    public void Loginfrom(String mode) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("login_from", mode);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Tracker t = ((MainApplication) getApplicationContext()).getTracker(MainApplication.TrackerName.APP_TRACKER);
        t.setScreenName("Login Screen");
        t.send(new HitBuilders.AppViewBuilder().build());
    }
}
