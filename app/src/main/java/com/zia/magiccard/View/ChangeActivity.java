package com.zia.magiccard.View;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zia.magiccard.Base.BaseActivity;
import com.zia.magiccard.Presenter.ChangePresenter;
import com.zia.magiccard.Presenter.ChangePresenterImp;
import com.zia.magiccard.R;

public class ChangeActivity extends BaseActivity implements ChangeActivityImp {

    private Button boy,girl,save;
    private ImageView head;
    private EditText introduceEdit;
    private TextView editWatcher;
    private boolean isBoy = true;
    private LinearLayout rootView;
    private ChangePresenterImp presenterImp;
    private static final int GET_IMAGE = 1;
    private static final int GET_FIXED_IMAGE = 2;
    public static final int GET_DISK_PERMISSION = 1;
    private static final String TAG = "ChangeActivityTest";
    private String bitmapPath = null;

    @Override
    protected void onCreated() {
        presenterImp.setRootViewClick();
        presenterImp.initData();
        setSexClick();//设置性别选择
        setEditText();//设置editText
        setHeadClick();//设置头像监听
        setSaveClick();//保存按钮监听
        setBackGroundClick();//点击背景返回
    }

    private void setBackGroundClick() {
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setSaveClick() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenterImp.saveData();
            }
        });
    }

    private void setHeadClick() {
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //包括权限申请
                presenterImp.sendGetImageIntent(GET_IMAGE);
            }
        });
    }

    private void setEditText() {
        introduceEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editWatcher.setText(introduceEdit.getText().toString().length()+"/30");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setSexClick() {
        boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBoy();
            }
        });
        girl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setGirl();
            }
        });
    }

    //相机返回
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GET_IMAGE:
                if (data != null) {
                    Uri uri = data.getData();
                    Log.d(TAG,uri.toString());
                    presenterImp.sendGetFixedIntent(uri,GET_FIXED_IMAGE);
                }
                break;
            case GET_FIXED_IMAGE:
                Bitmap bitmap = data.getParcelableExtra("data");
                if(bitmap == null) break;
                presenterImp.setImageView(bitmap);
                bitmapPath = presenterImp.getFixedImagePath(bitmap);
                Log.d(TAG,bitmapPath);
                break;
        }
    }

    //裁剪返回
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case GET_DISK_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){ //同意权限申请
                    toast("申请权限成功！");
                }else { //拒绝权限申请
                    toast("没有获取到权限哦..");
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void findWidgets() {
        presenterImp = new ChangePresenter(this);
        rootView = $(R.id.change_root);
        editWatcher = $(R.id.change_edit_watcher);
        boy = $(R.id.change_boy);
        girl = $(R.id.change_girl);
        save = $(R.id.change_save);
        head = $(R.id.change_head);
        introduceEdit = $(R.id.change_introduce);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_change;
    }

    @Override
    protected void beforeSetContentView() {

    }

    @Override
    public EditText getEditText() {
        return introduceEdit;
    }

    @Override
    public boolean isBoy() {
        return isBoy;
    }

    @Override
    public ImageView getHeadImage() {
        return head;
    }

    @Override
    public String getBitmapPath() {
        return bitmapPath;
    }

    @Override
    public Button getSaveButton() {
        return save;
    }

    @Override
    public void setBoy() {
        boy.setTextColor(Color.BLUE);
        girl.setTextColor(Color.BLACK);
        isBoy = true;
    }

    @Override
    public void setGirl() {
        boy.setTextColor(Color.BLACK);
        girl.setTextColor(Color.parseColor("#FF6EB4"));
        isBoy = false;
    }

}
