package com.zia.magiccard.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

import com.zia.magiccard.R;

public class VideoActivity extends AppCompatActivity {

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        videoView = (VideoView)findViewById(R.id.video_videoview);
    }
}
