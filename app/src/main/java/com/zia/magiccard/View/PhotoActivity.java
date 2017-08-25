package com.zia.magiccard.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.zia.magiccard.Base.BaseActivity;
import com.zia.magiccard.R;

public class PhotoActivity extends BaseActivity {

    private PhotoView photoView;
    private LinearLayout root;

    @Override
    protected void onCreated() {
        String url = getIntent().getStringExtra("url");
        if(url != null){
            Glide.with(this).load(url).into(photoView);
        }
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void findWidgets() {
        photoView = $(R.id.photoView);
        root = $(R.id.photo_root);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_photo;
    }

    @Override
    protected void beforeSetContentView() {

    }
}
