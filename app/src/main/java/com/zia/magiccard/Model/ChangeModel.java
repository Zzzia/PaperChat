package com.zia.magiccard.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;

/**
 * Created by zia on 17-8-20.
 */

public class ChangeModel {

    private Context context;
    private static final String TAG = "HeadImageModelTest";

    public ChangeModel(Context context){
        this.context = context;
    }

    /**
     * 图像保存到文件中，并获取路径，等待上传
     * @param bitmap bitmap
     * @return 路径
     */
    public String saveBitmap(Bitmap bitmap){
        FileOutputStream foutput = null;
        String imagePath = null;
        try {
            File appDir = new File(Environment.getExternalStorageDirectory(), "MagicCard");
            if (!appDir.exists()) {
                appDir.mkdir();
            }
            String fileName = "head.jpg";
            File file = new File(appDir, fileName);
            foutput = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, foutput);
            imagePath = file.getAbsolutePath();
            Log.d(TAG, "save image path:" + imagePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return imagePath;
    }

    public void saveData(String introduce, boolean isboy, String bitmapPath, final SaveCallback callback){
        final AVUser user = AVUser.getCurrentUser();
        user.put("isboy",isboy);
        user.put("introduce",introduce);
        AVFile avFile = null;
        try {
            if (bitmapPath != null)
            {
                avFile = AVFile.withAbsoluteLocalPath("head", bitmapPath);
            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        if(avFile != null){
            final AVFile finalAvFile = avFile;
            avFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if(e == null){
                        user.put("head", finalAvFile);
                        user.saveInBackground(callback);
                    }
                    else{
                        e.printStackTrace();
                    }
                }
            });

        }
    }
}
