package com.elevenfifty.elevenchat.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by kathy on 7/14/2015.
 */

@ParseClassName("Friendship")
public class Friendship extends ParseObject {
    private ChatUser currentUser;
    private ChatUser theFriend;

    public ChatUser getCurrentUser() {
        return (ChatUser)get("currentUser");
    }

    public void setCurrentUser(ChatUser currentUser) {
        put("currentUser", currentUser);
    }

    public ChatUser getTheFriend() {
        return (ChatUser)get("theFriend");
    }

    public void setTheFriend(ChatUser theFriend) {
        put("theFriend", theFriend);
    }
}
