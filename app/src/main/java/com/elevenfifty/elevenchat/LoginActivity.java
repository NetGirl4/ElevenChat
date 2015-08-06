package com.elevenfifty.elevenchat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.elevenfifty.elevenchat.Models.ChatUser;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;


public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (ChatUser.getCurrentUser() != null) {
            startApp();
        }

        final Button twitterLogin = (Button)findViewById(R.id.twitter_button);
        twitterLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseTwitterUtils.logIn(LoginActivity.this, new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if (parseUser == null) {
                            // 2du: show message to user
                            Log.d("LoginActivity", "null user on login");

                        } else if (parseUser.isNew()) {
                            login(parseUser);

                        } else {
                            login(parseUser);
                        }
                    }
                });
            }
        });

    }

    private void login(ParseUser parseUser) {
        ChatUser chatUser = (ChatUser)parseUser;
        chatUser.setTwitterHandle(ParseTwitterUtils.getTwitter().getScreenName().toLowerCase());
        chatUser.saveInBackground();

        startApp();


    }

    private void startApp() {
        Intent intent = new Intent(this, ChatPagerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
