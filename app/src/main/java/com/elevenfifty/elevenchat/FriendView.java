package com.elevenfifty.elevenchat;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import com.elevenfifty.elevenchat.Adapters.FriendListAdapter;
import com.elevenfifty.elevenchat.Adapters.UserListAdapter;
import com.elevenfifty.elevenchat.Models.ChatPicture;
import com.elevenfifty.elevenchat.Models.ChatUser;
import com.elevenfifty.elevenchat.Models.Friendship;
import com.elevenfifty.elevenchat.Models.ImageEvent;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by kathy on 7/14/2015.
 */
public class FriendView extends RelativeLayout
{

    private SearchView searchBar;
    private ListView friendList;

    private ParseQuery<Friendship> friendQuery;
    private FriendListAdapter friendListAdapter;

    private UserListAdapter userListAdapter;
    private boolean searching;
    private boolean addingFriends = false;
    private boolean sendMode = false;
    private ArrayList<Friendship> selectedFriends;

    public FriendView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        searchBar = (SearchView) findViewById(R.id.search_view);
        friendList = (ListView) findViewById(R.id.friend_list);

        friendQuery = ParseQuery.getQuery(Friendship.class);
        friendQuery.whereEqualTo("currentUser", ChatUser.getCurrentUser());
        friendQuery.include("theFriend");
        runFriendQuery();


        searching = false;

        Button sendButton = (Button)findViewById(R.id.send_button);
        if (getContext().getClass().equals(ChatPagerActivity.class)) {
            sendButton.setVisibility(GONE);
            setupSearchBar();
        } else {
            sendMode = true;
            searchBar.setVisibility(GONE);
            selectedFriends = new ArrayList<>();

            EventBus.getDefault().register(this);
            sendButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new ImageEvent());
                }
            });


        }

        friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (sendMode) {
                    Friendship friendship = friendListAdapter.getItem(position);

                    if (selectedFriends.contains(friendship)) {
                        selectedFriends.remove(friendship);


                    } else {
                        selectedFriends.add(friendship);
                    }
                    friendListAdapter.setSelectedFriends(selectedFriends);
                } else {
                    if (searching) {
                        ChatUser theFriend = userListAdapter.getItem(position);
                        addFriend(theFriend);
                    }
                }
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        if (sendMode) {
            EventBus.getDefault().unregister(this);
        }
        super.onDetachedFromWindow();
    }

    private void addFriend(final ChatUser friend)
    {
        if (addingFriends)
        {
            return;
        }
        addingFriends = true;
        ParseQuery<Friendship> friendCheck = ParseQuery.getQuery(Friendship.class);
        friendCheck.whereEqualTo("currentUser", ChatUser.getCurrentUser());
        friendCheck.whereEqualTo("theFriend", friend);
        friendCheck.countInBackground(new CountCallback() {
            @Override
            public void done(int i, ParseException e) {
                if (i > 0) {
                    addingFriends = false;

                } else {
                    Friendship friendship = new Friendship();
                    friendship.setCurrentUser((ChatUser) ChatUser.getCurrentUser());
                    friendship.setTheFriend(friend);
                    friendship.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            addingFriends = false;
                        }
                    });
                }
            }
        });
    }

    private void runFriendQuery() {
        friendQuery.findInBackground(new FindCallback<Friendship>() {
            @Override
            public void done(List<Friendship> list, ParseException e) {
                if (e == null) {
                    Friendship[] friendship = new Friendship[list.size()];
                    for (int i = 0; i < list.size(); i++) {
                        friendship[i] = list.get(i);
                    }
                    friendListAdapter = new FriendListAdapter(getContext(), R.layout.chatuser_list_item, friendship);
                    friendList.setAdapter(friendListAdapter);
                }
            }
        });

    }

    private void runUserQuery(String text) {
        if (text.equals("")) {
            runFriendQuery();
        } else {
            ParseQuery<ChatUser> userQuery = ParseQuery.getQuery(ChatUser.class);
            userQuery.whereContains("twitterHandle", text.toLowerCase());
            userQuery.findInBackground(new FindCallback<ChatUser>() {
                @Override
                public void done(List<ChatUser> list, ParseException e) {
                    if (e == null) {
                        Log.d("test","list size: " + list.size());
                        ChatUser[] chatUsers = new ChatUser[list.size()];
                        for (int i = 0; i < list.size(); i++) {
                            Log.d("test","list item: " + list.get(i).getTwitterHandle());
                            chatUsers[i] = list.get(i);
                        }
                        userListAdapter = new UserListAdapter(getContext(),
                                R.layout.chatuser_list_item, chatUsers);

                        friendList.setAdapter(userListAdapter);
                    }

                }
            });
        }
    }

    private void setupSearchBar() {
        searchBar.setOnSearchClickListener(new OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   searching = true;
                                                   runUserQuery("");

                                               }
                                           }
        );

        searchBar.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searching = false;
                runFriendQuery();
                return false;
            }


        });

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searching = true;
                runUserQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searching = true;
                runUserQuery(newText);
                return false;
            }
        });
    }
    public void onEventAsync(ImageEvent event) {
        try {
            File imageFile = new File(getContext().getCacheDir() +
                File.separator + CameraView.IMAGE_NAME);
            InputStream inputStream = new FileInputStream(imageFile);
            byte[] bytes;
            byte[] buffer = new byte[8192];
            int bytesRead;

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            try {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            bytes = output.toByteArray();
                    inputStream.close();

            final ParseFile uploadFile = new ParseFile(CameraView.IMAGE_NAME, bytes);
            uploadFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        for (Friendship friendship : selectedFriends) {
                            ChatPicture chatPicture = new ChatPicture();
                            chatPicture.setFromUser((ChatUser) ChatUser.getCurrentUser());
                            chatPicture.setToUser(friendship.getTheFriend());
                            chatPicture.setImage(uploadFile);
                            chatPicture.saveInBackground();

                            ParsePush matchbox20 = new ParsePush();

                            ParseQuery<ChatUser> userQuery = ParseQuery.getQuery(ChatUser.class);
                            userQuery.whereEqualTo("objectId", chatPicture.getToUser().getObjectId());
                            ParseQuery<ParseInstallation> query =
                                    ParseQuery.getQuery(ParseInstallation.class);
                            query.whereMatchesQuery("user", userQuery);

                            matchbox20.setQuery(query);
                            matchbox20.setMessage("You have a new message");
                            matchbox20.sendInBackground();
                        }
                    }
                    ((Activity) getContext()).finish();

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
