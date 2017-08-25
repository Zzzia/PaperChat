package com.zia.magiccard.View;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zia.magiccard.Base.BaseActivity;
import com.zia.magiccard.Bean.MarkdownData;
import com.zia.magiccard.R;
import com.zia.magiccard.Util.MarkdownUtil;
import com.zia.magiccard.Util.ScreenUtil;
import com.zzhoujay.richtext.RichText;

import static com.zia.magiccard.View.ChatActivity.FOR_MARKDOWN;

public class MarkDownActivity extends AppCompatActivity {

    private TextView textView,preview,publish;
    private EditText editText;
    private ImageView random;
    private ScrollView scrollView;
    private LinearLayout root;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        setContentView(R.layout.activity_mark_down);
        textView = (TextView)findViewById(R.id.markdown_tv);
        editText = (EditText)findViewById(R.id.markdown_edit);
        random = (ImageView)findViewById(R.id.markdown_random);
        preview = (TextView)findViewById(R.id.markdown_preview);
        publish = (TextView)findViewById(R.id.markdown_publish);
        root = (LinearLayout)findViewById(R.id.markdown_root);
        scrollView = (ScrollView)findViewById(R.id.markdown_scrollview);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //实时渲染markdown
                RichText.fromMarkdown(editText.getText().toString()).into(textView);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //markdown功能1--无序列表
        random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = editText.getText().toString();
                if(str.isEmpty()) return;
                //当前光标位置
                int currentPosition = editText.getSelectionStart();
                int lastSpacePosition = getLatestSpacePosition(str,currentPosition);
//                int lastSpacePosition = str.lastIndexOf('\n');
                Log.e("current",currentPosition+"");
                Log.e("lastSpace",lastSpacePosition+"");
                //插入无序列表
                str = stringInsert(str,"* ",lastSpacePosition);
                //添加两个回车，移动光标
                String result = str + "\n\n";
                editText.setText(result);
                editText.setSelection(currentPosition + 4);
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
        //预览
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                int width = ScreenUtil.bulid(getApplicationContext()).getPxWide();
//                int height = ScreenUtil.bulid(getApplicationContext()).getPxHiget();
//                View preview = LayoutInflater.from(getApplicationContext()).inflate(R.layout.markdown_preview,null);
//                TextView previewTv = preview.findViewById(R.id.markdown_preview_textview);
//                PopupWindow popupWindow = new PopupWindow(preview,width/6*5,height/4*3);
//                popupWindow.setFocusable(true);
//                popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
//                popupWindow.setOutsideTouchable(true);
//                popupWindow.showAtLocation(root, Gravity.CENTER,0,0);
//                RichText.fromMarkdown(editText.getText().toString()).into(previewTv);
                MarkdownUtil.previewMarkDown(getApplicationContext(),root,editText.getText().toString());
            }
        });
        //发送
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("markdown",editText.getText().toString());
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    //字符串插入
    private String stringInsert(String raw,String insert,int position){
        return raw.substring(0,position)+insert+raw.substring(position,raw.length());
    }

    private int getLatestSpacePosition(String raw,int position){
        for( int i = position;i > 0;i--){
            if(raw.charAt(i-1) == '\n'){
                return i-1;
            }
        }
        return 0;
    }

}
