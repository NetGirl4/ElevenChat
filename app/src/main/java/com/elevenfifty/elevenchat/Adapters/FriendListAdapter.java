package com.elevenfifty.elevenchat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.elevenfifty.elevenchat.Models.Friendship;
import com.elevenfifty.elevenchat.R;

import java.util.AbstractList;
import java.util.ArrayList;

/**
 * Created by kathy on 7/14/2015.
 */
public class FriendListAdapter extends ArrayAdapter<Friendship> {
    private Friendship[] friends;
    private int resource;

    private boolean checkSelection;
    private AbstractList<Friendship> selectedFriends;

    public FriendListAdapter(Context context, int resource, Friendship[] objects) {
        super(context, resource, objects);
        friends = objects;
        this.resource = resource;
        checkSelection = false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater)getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(resource, null);
        }
        Friendship friendship = friends[position];
        TextView friendName = (TextView)v.findViewById(R.id.twitter_handle);
        friendName.setText(friendship.getTheFriend().getTwitterHandle());

        ImageView checkMark = (ImageView)v.findViewById(R.id.checkmark);

        if (checkSelection) {
            if (selectedFriends.contains(friendship)) {
                checkMark.setVisibility(View.VISIBLE);
            } else {
                checkMark.setVisibility(View.INVISIBLE);
            }
        } else {

            checkMark.setVisibility(View.INVISIBLE);
        }

        return v;
    }
    public void setSelectedFriends(ArrayList<Friendship> friends) {
        selectedFriends = friends;
        checkSelection = true;
        this.notifyDataSetChanged();
    }
}
