package com.elevenfifty.elevenchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;

import java.io.File;


public class ConfirmImageActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_image);

        ImageView confirmImage = (ImageView)findViewById(R.id.confirm_image);

        LoadImageTask task = new LoadImageTask(confirmImage);
        File imagePath = new File(getCacheDir() + File.separator +
                        CameraView.IMAGE_NAME);
        task.execute(imagePath.toString());


    }

    public void retakeImage(View view) {
        finish();
    }

    public void sendImage(View view) {
        // start a new activity after we make it
        Intent intent = new Intent(this, SendImageActivity.class);
        startActivity(intent);
        finish();
    }
}
