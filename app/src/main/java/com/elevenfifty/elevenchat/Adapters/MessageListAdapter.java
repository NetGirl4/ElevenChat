package com.elevenfifty.elevenchat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.elevenfifty.elevenchat.Models.ChatPicture;
import com.elevenfifty.elevenchat.Models.ChatUser;
import com.elevenfifty.elevenchat.R;

/**
 * Created by kathy on 7/14/2015.
 */
public class MessageListAdapter extends ArrayAdapter<ChatPicture> {
    private ChatPicture[] chatPictures;
    private int resource;
    public MessageListAdapter(Context context, int resource, ChatPicture[] objects) {
        super(context, resource, objects);
        chatPictures = objects;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater)getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(resource, null);
        }

        ChatUser user = chatPictures[position].getFromUser();

        TextView friendName = (TextView)v.findViewById(R.id.twitter_handle);
        friendName.setText(user.getTwitterHandle());

        ImageView checkMark = (ImageView)v.findViewById(R.id.checkmark);
        checkMark.setVisibility(View.INVISIBLE);

        return v;
    }
}
