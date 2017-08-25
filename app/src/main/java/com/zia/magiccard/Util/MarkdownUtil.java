package com.zia.magiccard.Util;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zia.magiccard.R;
import com.zzhoujay.richtext.RichText;

/**
 * Created by zia on 17-8-25.
 */

public class MarkdownUtil {
    public static void previewMarkDown(Context context, View root, String md){
        int width = ScreenUtil.bulid(context).getPxWide();
        int height = ScreenUtil.bulid(context).getPxHiget();
        View preview = LayoutInflater.from(context).inflate(R.layout.markdown_preview,null);
        TextView previewTv = preview.findViewById(R.id.markdown_preview_textview);
        PopupWindow popupWindow = new PopupWindow(preview,width/6*5,height/4*3);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(root, Gravity.CENTER,0,0);
        RichText.fromMarkdown(md).into(previewTv);
    }
}
