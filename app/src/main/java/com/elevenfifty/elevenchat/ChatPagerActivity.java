package com.elevenfifty.elevenchat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.elevenfifty.elevenchat.Models.ChatUser;

public class ChatPagerActivity extends Activity {
    private static final String TAG = "ChatPagerActivity";
    private int lastPage = 1;
    static CameraView cameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_pager);

        cameraView = (CameraView)LayoutInflater.from(this).inflate(R.layout.camera_page, null);

        ViewPager viewPager = (ViewPager)findViewById(R.id.viewpager);
        ChatPagerAdapter pagerAdapter = new ChatPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        viewPager.setCurrentItem(1);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
                if (i2 == 0) {
                    if (i == 1) {
                        if (lastPage != 1) {
                            lastPage = 1;
                            cameraView.startCamera();
                        }
                    } else {
                        lastPage = i;
                        cameraView.stopCamera();
                    }
                }
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    protected void onPause() {
        if (lastPage == 1) {
            cameraView.stopCamera();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (lastPage == 1) {
            cameraView.startCamera();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void logout(View view) {
        ChatUser.logOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }






    public static class ChatPagerAdapter extends PagerAdapter {
        final LayoutInflater inflater;
        final Context context;

        public ChatPagerAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = new View(context);
            switch (position) {
                case 0:
                    view = inflater.inflate(R.layout.message_page, null);
                    break;
                case 1:
                    view = cameraView;
                    break;
                case 2:
                    view = inflater.inflate(R.layout.friend_list_page, null);
                    break;
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}