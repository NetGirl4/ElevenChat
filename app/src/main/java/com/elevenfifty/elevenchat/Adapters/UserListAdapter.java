package com.elevenfifty.elevenchat.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.elevenfifty.elevenchat.Models.ChatUser;
import com.elevenfifty.elevenchat.R;

/**
 * Created by kathy on 7/14/2015.
 */
public class UserListAdapter extends ArrayAdapter<ChatUser> {
    private ChatUser[] users;
    private int resource;

    public UserListAdapter(Context context, int resource, ChatUser[] objects) {
        super(context, resource, objects);
        users = objects;
        this.resource= resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater)getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(resource, null);
        }

        ChatUser user = users[position];
        Log.d("test", "user: " + user.getTwitterHandle());
        TextView friendName = (TextView)v.findViewById(R.id.twitter_handle);
        friendName.setText(user.getTwitterHandle());

        ImageView checkMark = (ImageView)v.findViewById(R.id.checkmark);
        checkMark.setVisibility(View.INVISIBLE);
        Log.d("test", "users size: " + users.length);

        return v;
    }
}
