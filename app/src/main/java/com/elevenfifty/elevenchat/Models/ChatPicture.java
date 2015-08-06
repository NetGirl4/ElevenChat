package com.elevenfifty.elevenchat.Models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

/**
 * Created by kathy on 7/14/2015.
 */
@ParseClassName("ChatPicture")
public class ChatPicture extends ParseObject {
    private ChatUser fromUser;
    private ChatUser toUser;
    private ParseFile image;

    public ChatUser getFromUser() {
        return (ChatUser)get("fromUser");
    }

    public void setFromUser(ChatUser fromUser) {
        put("fromUser", fromUser);
    }

    public ChatUser getToUser() {
        return (ChatUser)get("toUser");
    }

    public void setToUser(ChatUser toUser) {
        put ("toUser", toUser);
    }

    public ParseFile getImage() {
        return getParseFile("image");
    }

    public void setImage(ParseFile image) {
        put("image", image);
    }
}
