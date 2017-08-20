package com.zia.magiccard.Presenter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

/**
 * Created by zia on 17-8-20.
 */

public interface ChangePresenterImp {
    void setRootViewClick();
    void sendGetImageIntent(int code);
    String getFixedImagePath(Bitmap bitmap);
    void sendGetFixedIntent(Uri uri, int code);
    void setImageView(Bitmap bitmap);
    void saveData();
    void initData();
}
