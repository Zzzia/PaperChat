package com.zia.magiccard.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.cjt2325.cameralibrary.JCameraView;
import com.cjt2325.cameralibrary.lisenter.ErrorLisenter;
import com.cjt2325.cameralibrary.lisenter.JCameraLisenter;
import com.zia.magiccard.Adapter.MessageRecyclerAdapter;
import com.zia.magiccard.Base.BaseActivity;
import com.zia.magiccard.Bean.ConversationData;
import com.zia.magiccard.Bean.UserData;
import com.zia.magiccard.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraActivity extends BaseActivity implements ChatImp {

    private static final String TAG = "CameraActivity";
    private JCameraView jCameraView;
    private String basePath;

    @Override
    protected void onCreated() {
        initCamera();
    }

    private void initCamera() {
        File appDir = new File(Environment.getExternalStorageDirectory(), "PaperChat");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        basePath = appDir.getPath();
        jCameraView.setSaveVideoPath(basePath + "/photo");
        //设置只能录像或只能拍照或两种都可以（默认两种都可以）
        jCameraView.setFeatures(JCameraView.BUTTON_STATE_BOTH);
        //设置视频质量
        jCameraView.setMediaQuality(JCameraView.MEDIA_QUALITY_MIDDLE);
        //JCameraView监听
        jCameraView.setErrorLisenter(new ErrorLisenter() {
            @Override
            public void onError() {
                //打开Camera失败回调
                Log.i("CJT", "open camera error");
            }
            @Override
            public void AudioPermissionError() {
                //没有录取权限回调
                Log.i("CJT", "AudioPermissionError");
            }
        });

        jCameraView.setJCameraLisenter(new JCameraLisenter() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                //获取图片bitmap
                Log.i("JCameraView", "bitmap = " + bitmap.getWidth());
                Intent intent = new Intent();
                String path = saveBitmap(bitmap);
                intent.putExtra("photo",path);
                setResult(RESULT_OK,intent);
                quit();
            }

            @Override
            public void recordSuccess(String url, Bitmap firstFrame) {
                //获取视频路径
                Log.i("CJT", "url = " + url);
                Intent intent = new Intent();
                intent.putExtra("videoPath",url);
                String path = saveBitmap(firstFrame);
                intent.putExtra("photo",path);
                setResult(RESULT_OK,intent);
                quit();
            }

            @Override
            public void quit() {
                //退出按钮
                CameraActivity.this.finish();
            }
        });
    }

    private String saveBitmap(Bitmap bitmap) {
        Log.e(TAG, "保存图片");
        File f = new File(basePath, "photo/picture.jpg");
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            Log.i(TAG, "已经保存");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f.getPath();
    }

    @Override
    protected void findWidgets() {
        jCameraView = $(R.id.jcameraview);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_camera;
    }

    @Override
    protected void beforeSetContentView() {
        //全屏
        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(option);
    }

    @Override
    public UserData getUserData() {
        return null;
    }

    @Override
    public ConversationData getConversationData() {
        return null;
    }

    @Override
    public EditText getEditText() {
        return null;
    }

    @Override
    public MessageRecyclerAdapter getMessageAdapter() {
        return null;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return null;
    }

    @Override
    public ProgressDialog getDialog() {
        return null;
    }
}
