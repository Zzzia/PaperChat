package com.zia.magiccard.Util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zia on 17-8-23.
 */

public class TimeUtil {

    public static String getDateString(Long date){
        long minutes = (System.currentTimeMillis() - date) / 60000;

        if(minutes < 2){
            return "一分钟前";
        }
        else if (minutes >= 2 && minutes <= 60*24){
            DateFormat todayFormat = new SimpleDateFormat("HH:mm");
            return "今天 " + todayFormat.format(date);
        }
        else if(minutes >= 60*24 && minutes < 60*24*7){
            DateFormat yesterdayFormat = new SimpleDateFormat("E HH:mm");
            return yesterdayFormat.format(date);
        }
        else{
            DateFormat dateFormat = new SimpleDateFormat("MM/dd HH:mm");
            return dateFormat.format(date);
        }
    }

//    public static String getDateString(Date date){
//
//    }
}
