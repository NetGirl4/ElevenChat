package com.elevenfifty.elevenchat.Models;

import com.parse.ParseClassName;
import com.parse.ParseUser;

/**
 * Created by kathy on 7/13/2015.
 */
@ParseClassName("_User")
public class ChatUser extends ParseUser {
    private String twitterHandle;

    public String getTwitterHandle() {
        return getString("twitterHandle");
    }

    public void setTwitterHandle(String twitterHandle) {
        put("twitterHandle", twitterHandle);
    }
}
