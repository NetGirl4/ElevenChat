package com.elevenfifty.elevenchat;

import android.app.Application;
import android.util.Log;

import com.elevenfifty.elevenchat.Models.ChatPicture;
import com.elevenfifty.elevenchat.Models.ChatUser;
import com.elevenfifty.elevenchat.Models.Friendship;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;

/**
 * Created by kathy on 7/13/2015.
 */
public class ElevenChatApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);

        ParseUser.registerSubclass(ChatUser.class);
        ParseObject.registerSubclass(Friendship.class);
        ParseObject.registerSubclass(ChatPicture.class);

        Parse.initialize(this, "0SyF4vtF9BVpPkam8XaovAvFI70NFO9Z8SSpGmVz", "VOHtotWgOTXsTMwTPHiLC6Yc5yw9FymPdHF7mDZz");
        ParseTwitterUtils.initialize("cQc2gkE0j7YP5fh2YwrNaswK1", "JpDbaaZ3THV2AiMEKhc4wy5WAJG4RJaKPlYUzZxlnzDsI82Svz");

        ParseInstallation parseInstallation = ParseInstallation.getCurrentInstallation();
        ArrayList<String> channels = new ArrayList<>();
        channels.add("globe");
        parseInstallation.put("channels", channels);
        parseInstallation.saveInBackground();

        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });

    }

}
