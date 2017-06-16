package com.ijustyce.school.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ijustyce.fastandroiddev.base.BaseFragment;
import com.ijustyce.fastandroiddev.baseLib.utils.CommonTool;
import com.ijustyce.fastandroiddev.baseLib.utils.FileUtils;
import com.ijustyce.fastandroiddev.baseLib.utils.IJson;
import com.ijustyce.fastandroiddev.baseLib.utils.ILog;
import com.ijustyce.fastandroiddev.baseLib.utils.ImageUtils;
import com.ijustyce.fastandroiddev.baseLib.utils.ToastUtil;
import com.ijustyce.fastandroiddev.net.FormFile;
import com.ijustyce.fastandroiddev.net.HttpParams;
import com.ijustyce.fastandroiddev.net.INetWork;
import com.ijustyce.fastandroiddev.net.ProcessListener;
import com.ijustyce.fastandroiddev.ui.CircleImageView;
import com.ijustyce.fastandroiddev.umenglib.UpdateTool;
import com.ijustyce.school.IApplication;
import com.ijustyce.school.R;
import com.ijustyce.school.activity.AboutActivity;
import com.ijustyce.school.activity.GuideActivity;
import com.ijustyce.school.constant.Constant;
import com.ijustyce.school.constant.HttpConstant;
import com.ijustyce.school.http.HttpAccount;
import com.ijustyce.school.model.UserInfo;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;

public class MeFragment extends BaseFragment {

    @Bind(R.id.logout)
    Button logout;

    @Bind(R.id.cacheSize)
    TextView cacheSize;

    @Bind(R.id.userName)
    TextView userName;

    @Bind(R.id.userMobile)
    TextView userMobile;

    @Bind(R.id.headimgIcon)
    CircleImageView header;

    private final static int SELECT_PIC = 0;
    private final static int CROP_PIC = 1;

    private String tmpFile;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_me;
    }

    @OnClick({R.id.aboutUs, R.id.recommendShare, R.id.versionUpdate, R.id.cache, R.id.logout,
            R.id.headimgIcon})
    public void onClick(View view){

        if (view == null){
            return;
        }
        switch (view.getId()){

            case R.id.aboutUs:
                newActivity(AboutActivity.class);
                break;

            case R.id.recommendShare:
                CommonTool.systemShare(mContext, getResString(R.string.share));
                break;

            case R.id.versionUpdate:
                UpdateTool.forceUpdate(mContext);
                break;

            case R.id.cache:
                clearCache();
                break;

            case R.id.headimgIcon:
                selectPic();
                break;

            case R.id.logout:
                IApplication.setUserInfo(null);
                SharedPreferences shared = mContext.
                        getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                shared.edit().clear().apply();
                newActivity(GuideActivity.class);
                getActivity().finish();
                break;

            default:
                break;
        }
    }

    private synchronized void clearCache(){

        showProcess("正在为您清理缓存");

        String file = FileUtils.getAvailablePath(mContext, Constant.FILE_PATH);
        if (file != null) {
            FileUtils.deleteFile(file);
        }
        dismiss(2000);
    }

    private void cropPic(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 96);
        intent.putExtra("outputY", 96);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_PIC);
    }

    private void selectPic() {

        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if (intent.resolveActivity(mContext.getPackageManager()) != null) {
            startActivityForResult(intent, SELECT_PIC);
        } else {
            ToastUtil.show(mContext, "系统没有图片选择软件！");
        }
    }

    @Override
    public void onSuccess(String object, String taskId){

        ILog.i("===me===", "result is " + object);
        UserInfo userInfo = IJson.fromJson(object + "", UserInfo.class);
        if (userInfo == null || userInfo.getResult() == null){
            return;
        }
        String head = null;
        if (!HttpConstant.UPLOAD.equals(taskId)){

            ImageUtils.load(mContext, header,
                    HttpConstant.FILES + IApplication.getHead());
            return;
        }else{
            head = userInfo.getResult().getMsg();
        }
        HttpAccount.upHead(getTAG(), head, mContext, httpListener);

        SharedPreferences shared = mContext.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String result = shared.getString("userInfo", null);
        if (result != null){
            UserInfo tmp = IJson.fromJson(result, UserInfo.class);
            UserInfo.DataEntity data = tmp == null ? null : tmp.getUserData();
            if (data != null) {
                data.setHead(head);
                tmp.setData(data);
                IApplication.setUserInfo(tmp);
                shared.edit().putString("userInfo", IJson.toJson(tmp, UserInfo.class)).apply();
            }
        }
        ImageUtils.load(mContext, header,
                HttpConstant.FILES + IApplication.getHead());
    }

    private void editHead(){

        if (tmpFile == null){
            return;
        }
        showProcess("正在上传头像...");
        File f = new File(tmpFile);
        FormFile[] file = new FormFile[1];
        file[0] = new FormFile(f.getName(), f, "uploadfile", null);
        INetWork.uploadFile(file, mContext, HttpParams.create(getTAG(), HttpConstant.UPLOAD), new ProcessListener() {
            @Override
            public void onProcess(long total, int current) {

            }

            @Override
            public void onError() {
                dismiss();
                ToastUtil.show(mContext, "头像上传失败");
            }

            @Override
            public void onSuccess(String response) {
                MeFragment.this.onSuccess(response, HttpConstant.UPLOAD);
            }

            @Override
            public void onDownload(String file, String fileUri) {

            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            case CROP_PIC:

                if (resultCode == Activity.RESULT_OK) {
                    if (data == null){
                        return;
                    }if(data.hasExtra("data")){

                        Bitmap bmap = data.getParcelableExtra("data");
                        if(bmap == null){
                            ToastUtil.showTop(mContext, "文件打开失败！");
                            return ;
                        }
                        tmpFile = FileUtils.getAvailablePath(mContext, Constant.FILE_PATH)
                                + "/.tmp/";
                        File tmp = new File(tmpFile);
                        if (!tmp.exists()) {
                            tmp.mkdirs();
                        }
                        tmpFile = tmpFile + System.currentTimeMillis() + ".png";
                        bmap = CommonTool.bitmapToRound(bmap);
                        FileUtils.savBitmapToPng(bmap, tmpFile);
                        bmap.recycle();
                        editHead();
                    }
                }
                break;

            case SELECT_PIC:
                if (resultCode == Activity.RESULT_OK) {
                    if (data == null) {
                        return;
                    }
                    Uri uri = data.getData();
                    cropPic(uri);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void doResume() {
        super.doResume();

        boolean isLogin = IApplication.isLogin();

        logout.setVisibility(isLogin ? View.VISIBLE : View.GONE);
        userName.setText(isLogin ? IApplication.getUserInfo().getName() : "");
        userMobile.setText(isLogin ? IApplication.getUserInfo().getEmail() : "");

        String file = FileUtils.getAvailablePath(mContext, Constant.FILE_PATH);
        cacheSize.setText(file == null ? "0M" : "" + FileUtils.getDirSize(new File(file)));

        if (IApplication.getHead() == null){
            return;
        }
        ImageUtils.load(mContext, header,
                HttpConstant.FILES + IApplication.getHead());
    }
}
