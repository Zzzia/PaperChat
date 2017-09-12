package com.zia.magiccard.Presenter;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zia.magiccard.Bean.UserData;
import com.zia.magiccard.Model.ChangeModel;
import com.zia.magiccard.Util.UserUtil;
import com.zia.magiccard.R;
import com.zia.magiccard.Util.PermissionsUtil;
import com.zia.magiccard.Util.PullUtil;
import com.zia.magiccard.View.ChangeActivity;
import com.zia.magiccard.View.ChangeActivityImp;
import com.zia.magiccard.View.Fragments.MeFragment;
import com.zia.magiccard.View.MainActivity;

/**
 * Created by zia on 17-8-20.
 */

public class ChangePresenter implements ChangePresenterImp {

    private ChangeActivityImp imp;
    private ChangeModel model;

    public ChangePresenter(ChangeActivityImp imp){
        this.imp = imp;
        model = new ChangeModel(imp.getActivity());
    }
    @Override
    public void sendGetImageIntent(int code) {
        if(PermissionsUtil.hasDiskPermission(imp.getActivity(),ChangeActivity.GET_DISK_PERMISSION)){//如果有权限才执行
            Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imp.getActivity().startActivityForResult(intent,code);
        }
    }

    @Override
    public String getFixedImagePath(Bitmap bitmap) {
        // 图像保存到文件中，并获取路径，等待上传
        return model.saveBitmap(bitmap);
    }

    //请求裁剪照片
    @Override
    public void sendGetFixedIntent(Uri uri, int code) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = imp.getActivity().managedQuery(uri, proj, null, null, null);
        cursor.moveToFirst();
        Intent intent = new Intent();
        intent.setAction("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");// mUri是已经选择的图片Uri
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);// 输出图片大小
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        imp.getActivity().startActivityForResult(intent, code);
    }

    @Override
    public void setImageView(Bitmap bitmap) {
        bitmap = Bitmap.createScaledBitmap(bitmap,300,300,true);
        imp.getHeadImage().setImageBitmap(bitmap);
        if(MeFragment.head != null){
            MeFragment.head.setImageBitmap(bitmap);
        }
    }

    @Override
    public void saveData() {
        imp.getSaveButton().setText("正在保存");
        imp.getSaveButton().setClickable(false);
        model.saveData(imp.getEditText().getText().toString(), imp.isBoy(), imp.getBitmapPath(), new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e == null){
                    imp.toast("保存信息成功！");
                    //更新数据
                    PullUtil.pullCurrentUserData();
                }else{
                    imp.toast("保存失败?");
                }
                imp.getSaveButton().setText("保存");
                imp.getSaveButton().setClickable(true);
                imp.getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void initData() {
        if(MainActivity.userData != null){
            if(MainActivity.userData.getIntroduce() != null){
                imp.getEditText().setText(MainActivity.userData.getIntroduce());
            }
            if(MainActivity.userData.isboy()){
                imp.setBoy();
            }else{
                imp.setGirl();
            }
            if(MainActivity.userData.getHeadUrl() != null){
                Glide.with(imp.getActivity()).load(MainActivity.userData.getHeadUrl()).into(imp.getHeadImage());
            }
        }else{

            UserUtil.getUserById(AVUser.getCurrentUser().getObjectId(), new UserUtil.OnUserGet() {
                @Override
                public void getUserData(UserData userData) {
                    MainActivity.userData = userData;
                    if(userData.getIntroduce() != null){
                        imp.getEditText().setText(userData.getIntroduce());
                    }
                    if(userData.isboy()){
                        imp.setBoy();
                    }else{
                        imp.setGirl();
                    }
                    if(userData.getHeadUrl() != null){
                        Glide.with(imp.getActivity()).load(userData.getHeadUrl()).into(imp.getHeadImage());
                    }
                }

                @Override
                public void onError(AVException e) {
                    e.printStackTrace();
                    imp.toast("获取信息错误，请检查网络");
                }
            });
        }
    }

    @Override
    public void setRootViewClick() {
        View view = imp.getActivity().findViewById(R.id.change_CardView);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
