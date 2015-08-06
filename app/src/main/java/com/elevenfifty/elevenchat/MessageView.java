package com.elevenfifty.elevenchat;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.elevenfifty.elevenchat.Adapters.MessageListAdapter;
import com.elevenfifty.elevenchat.Models.ChatPicture;
import com.elevenfifty.elevenchat.Models.ChatUser;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by kathy on 7/14/2015.
 */
public class MessageView extends RelativeLayout {
    private ListView messageList;
    private MessageListAdapter messageListAdapter;

    private boolean imageUp;
    private ParseImageView fullImage;

    private ParseQuery<ChatPicture> picQuery;

    public MessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        imageUp = false;
        messageList = (ListView)findViewById(R.id.message_list);
        fullImage = (ParseImageView)findViewById(R.id.full_image);

        picQuery = ParseQuery.getQuery(ChatPicture.class);
        picQuery.whereEqualTo("toUser", ChatUser.getCurrentUser());
        picQuery.include("fromUser");
        runPicQuery();

        messageList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                imageUp = true;
                fullImage.setImageDrawable(null);
                fullImage.setVisibility(VISIBLE);

                final ChatPicture chatPicture = messageListAdapter.getItem(position);
                fullImage.setParseFile(chatPicture.getImage());
                fullImage.loadInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] bytes, ParseException e) {
                        if (e == null) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    selfDistruct(chatPicture);

                                }
                            }, 10000);
                        }

                    }
                });

                return false;
            }
        });

        messageList.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        if (imageUp) {
                            imageUp = false;
                            fullImage.setVisibility(GONE);
                        }
                        break;
                    default:
                        break;

                }
                return false;
            }
        });
    }
    private void runPicQuery() {
        picQuery.findInBackground(new FindCallback<ChatPicture>() {
            @Override
            public void done(List<ChatPicture> list, ParseException e) {
                if (e == null) {
                    ChatPicture[] pictures = new ChatPicture[list.size()];
                    for (int i = 0; i < list.size(); i++) {
                        pictures[i] = list.get(i);
                    }
                    messageListAdapter = new MessageListAdapter(getContext(),
                            R.layout.chatuser_list_item, pictures);
                    messageList.setAdapter(messageListAdapter);
                }
            }
        });
    }
    private void selfDistruct(ChatPicture chatPicture) {
        chatPicture.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                fullImage.setVisibility((INVISIBLE));
                fullImage.setImageDrawable(null);
                runPicQuery();
            }
        });
    }
}
