package com.zia.magiccard.Util;

import android.media.MediaPlayer;
import android.util.Log;

/**
 * Created by zia on 17-8-24.
 */

public class AudioPlayer implements AudioManagerImp {
    private String path;

    private MediaPlayer mPlayer;
    public AudioPlayer(String path){
        this.path = path;
        mPlayer = new MediaPlayer();
    }

    @Override
    public void start() {
        try {
            //设置要播放的文件
            mPlayer.setDataSource(path);
            mPlayer.prepare();
            //播放
            mPlayer.start();
        }catch(Exception e){
            Log.e("AudioPlayer", "prepare() failed");
        }

    }

    @Override
    public void stop() {
        mPlayer.stop();
        mPlayer.release();
        mPlayer = null;
    }
}
