package com.zia.magiccard.Util;

import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;

/**
 * Created by zia on 17-8-24.
 */

public class AudioRecorder implements AudioManagerImp {

    private String path;
    private MediaRecorder mRecorder;

    public AudioRecorder(String path){
        this.path = path;
        mRecorder = new MediaRecorder();
    }

    /*
     * 开始录音
     * @return boolean
     */
    @Override
    public void start() {
        //设置音源为Micphone
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //设置封装格式
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(path);
        //设置编码格式
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e("AudioRecorder", "prepare() failed");
        }
        //录音
        mRecorder.start();
    }

    /*
     * 停止录音
     * @return boolean
     */
    @Override
    public void stop() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }
}
