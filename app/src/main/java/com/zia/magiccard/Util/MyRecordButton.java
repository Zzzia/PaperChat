package com.zia.magiccard.Util;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by zia on 17-8-24.
 */

public class MyRecordButton extends android.support.v7.widget.AppCompatImageView {

    private float y;
    private RecordListener recordListener;
    private AudioRecorder audioRecorder;


    public MyRecordButton(Context context) {
        super(context);
    }

    public MyRecordButton(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyRecordButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void setRecordListener(RecordListener recordListener){
        this.recordListener = recordListener;
    }

    public interface RecordListener{
        void onBegin();
        void onCancel();
        void onFinish();
        void onTimeChange();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(recordListener == null) return true;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("record", "开始录音");
                recordListener.onBegin();
                File appDir = new File(Environment.getExternalStorageDirectory(), "PaperChat");
                if (!appDir.exists()) {
                    appDir.mkdir();
                }
                audioRecorder = new AudioRecorder(appDir.getPath()+"/record.pcm");
                audioRecorder.start();
                y = event.getY();
                break;

            case MotionEvent.ACTION_UP:
                Log.e("record", "up");
                //recordManager.stopRecord();
                audioRecorder.stop();
                recordListener.onFinish();
                if(event.getY()-y < -200){
                    Log.e("record",event.getY()-y+"  取消录音");
                    recordListener.onCancel();
                    break;
                }
                Log.e("record","录音结束");
                break;

            case MotionEvent.ACTION_CANCEL:
                Log.e("record", "cancel");
                audioRecorder.stop();
                recordListener.onCancel();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }
        return true;
    }
}
