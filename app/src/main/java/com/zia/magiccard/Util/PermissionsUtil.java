package com.zia.magiccard.Util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;


/**
 * Created by zia on 17-7-24.
 */

public class PermissionsUtil {

    private PermissionsUtil(){};

    public static boolean hasDiskPermission(Activity activity,int code){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            //表示未授权时进行授权
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, code);
            return false;
        }
        return true;
    }
}
