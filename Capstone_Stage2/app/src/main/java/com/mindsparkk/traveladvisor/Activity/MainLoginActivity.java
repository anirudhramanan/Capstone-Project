package com.mindsparkk.traveladvisor.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.mindsparkk.traveladvisor.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anirudh on 05/09/15.
 */
public class MainLoginActivity extends AppCompatActivity {

    LinearLayout l;
    Button login, signup, fb_login_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_activity);

        l = (LinearLayout) findViewById(R.id.l);
        login = (Button) findViewById(R.id.login);
        signup = (Button) findViewById(R.id.signup);
        fb_login_button = (Button) findViewById(R.id.fb_login_button);

        TextView skip = (TextView) findViewById(R.id.skip);

        Animation bottom_slide = AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom);
        l.startAnimation(bottom_slide);

        ParseUser currentUser = ParseUser.getCurrentUser();

        if ((currentUser != null)) {
            startIntent();
        }

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startIntent();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainLoginActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainLoginActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });

        fb_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoginClick(view);
            }
        });
    }

    public void startIntent() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public void onLoginClick(View v) {
        final ProgressDialog d = new ProgressDialog(this);
        d.setMessage("Verifying Credentials...");
        d.setIndeterminate(true);
        d.setCanceledOnTouchOutside(false);
        d.show();

        final List<String> permissions = new ArrayList<String>();
        permissions.add("public_profile");
        permissions.add("email");

        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, new LogInCallback() {
            @Override
            public void done(final ParseUser user, ParseException e) {
                d.dismiss();
                if (e == null) {
                    if (user == null) {
                        ParseUser.logOut();
                    } else if (user.isNew()) {
                        //Log.d("id", "User signed up for 1st tym using facebook");
                        //saving it in a shared preference as he logged in from fb......................
                        getFbData();
                        //startIntent();
                    } else {
                        getFbData();
                    }
                } else {
                    Log.d("error1", e.getMessage());

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    //getting facebook data.......................................................................
    public void getFbData() {

        final ProgressDialog d = new ProgressDialog(this);
        d.setIndeterminate(true);
        d.setMessage("Verifying Credentials....");
        d.setCanceledOnTouchOutside(false);
        d.show();

        //<------------------------------------------getting gender , name , email ---------------------------------->
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {

                        Log.d("responseFacebook", graphResponse.toString());
                        try {
                            final String id, email, name;

                            email = jsonObject.getString("email");
                            name = jsonObject.getString("name");

                            ParseUser user = ParseUser.getCurrentUser();
                            user.put("name", name);
                            user.put("email", email);

                            user.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        d.dismiss();
                                        startIntent();
                                    } else {
                                        Log.d("error", e.getMessage());
                                        d.dismiss();
                                    }
                                }
                            });

                        } catch (JSONException e) {
                            d.dismiss();
                        }
                    }
                });

        Bundle params = new Bundle();
        params.putString("fields", "id,name,email");
        request.setParameters(params);
        request.executeAsync();
    }
}
