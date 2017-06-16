package com.ijustyce.school.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ijustyce.fastandroiddev.base.BaseActivity;
import com.ijustyce.fastandroiddev.baseLib.utils.CommonTool;
import com.ijustyce.fastandroiddev.baseLib.utils.FileUtils;
import com.ijustyce.fastandroiddev.baseLib.utils.ILog;
import com.ijustyce.fastandroiddev.baseLib.utils.ImageUtils;
import com.ijustyce.fastandroiddev.baseLib.utils.RegularUtils;
import com.ijustyce.fastandroiddev.baseLib.utils.ToastUtil;
import com.ijustyce.fastandroiddev.net.DownManager;
import com.ijustyce.fastandroiddev.net.FormFile;
import com.ijustyce.fastandroiddev.net.HttpParams;
import com.ijustyce.fastandroiddev.net.INetWork;
import com.ijustyce.fastandroiddev.net.ProcessListener;
import com.ijustyce.school.IApplication;
import com.ijustyce.school.R;
import com.ijustyce.school.constant.Constant;
import com.ijustyce.school.constant.HttpConstant;
import com.ijustyce.school.http.HttpAccount;
import com.ijustyce.school.model.BiJi;
import com.ijustyce.school.model.UserInfo;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by yc on 16-3-19.    创建笔记
 */
public class NewBiJi extends BaseActivity<UserInfo> {

    @Bind(R.id.label)
    TextView label;

    @Bind(R.id.right)
    TextView right;

    @Bind(R.id.title)
    EditText titleEdit;

    @Bind(R.id.content)
    EditText contentEdit;

    @Bind(R.id.teacher)
    RelativeLayout teacher;

    @Bind(R.id.img)
    ImageView pic;

    @Bind(R.id.type)
    ImageView type;

    private int id;
    private String path;

    private BiJi.DataEntity model;
    private static final int SELECT_FILE = 100;

    private DownManager downManager;
    private int isFujian = 0;

    @Override
    public void afterCreate() {

        Bundle bundle = getIntent().getExtras();
        if (bundle == null){
            finish();
            return;
        }

        id = bundle.containsKey("id") ? bundle.getInt("id") : -1;
        model = bundle.containsKey("item") ? (BiJi.DataEntity) bundle.getSerializable("item") : null;
        if (id <= 0 && model == null){
            finish();
            return;
        }

        label.setText(model == null ? (IApplication.isTeacher() ? "上传资料" : "新建笔记") : "笔记浏览");
        right.setText(model == null ? "保存" : "删除");

        //  如果是老师，而且实体不为空（老师阅读学生笔记），不允许删除
        right.setVisibility(model == null || (IApplication.isTeacher() && model.getTeacher() == 1)
                || (!IApplication.isTeacher() && model.getTeacher() == 0) ? View.VISIBLE : View.GONE);

        if (IApplication.isTeacher() && model == null){
            teacher.setVisibility(View.VISIBLE);
            contentEdit.setVisibility(View.GONE);
        }

        if (model != null){

            String info = "" + model.getInfo();
            titleEdit.setText(model.getDescript());
            titleEdit.setEnabled(false);
            contentEdit.setText(info);
            CommonTool.setEditAble(false, contentEdit);
            type.setVisibility(View.VISIBLE);
            if (info.endsWith(".doc"))
                type.setImageBitmap(CommonTool.drawableToBitmap(getResources().getDrawable(R.mipmap.word)));
            else if (info.endsWith(".mp4"))
                type.setImageBitmap(CommonTool.drawableToBitmap(getResources().getDrawable(R.mipmap.vedio)));
            else if (info.endsWith(".ppt"))
                type.setImageBitmap(CommonTool.drawableToBitmap(getResources().getDrawable(R.mipmap.ppt)));

            if (RegularUtils.isImage(model.getInfo())){
                contentEdit.setVisibility(View.GONE);
                pic.setVisibility(View.VISIBLE);
                ImageUtils.load(mContext, pic, HttpConstant.FILES + model.getInfo());
            }
        }
    }

    private void doDelete(){

        showProcess("正在删除...");
        if(!HttpAccount.deleteBiJi(getTAG(), model.getId(), mContext, httpListener)){

            dismiss(2000);
        }
    }

    private void uploadFile(){

        if (path == null){

            saveText();
            return;
        }
        showProcess("正在上传文件...");
        File f = new File(path);
        FormFile[] file = new FormFile[1];
        file[0] = new FormFile(f.getName(), f, "uploadfile", null);
        INetWork.uploadFile(file, mContext, HttpParams.create(getTAG(), HttpConstant.UPLOAD), new ProcessListener() {
            @Override
            public void onProcess(long total, int current) {

            }

            @Override
            public void onError() {
                dismiss();
                ToastUtil.show(mContext, "文件上传失败");
            }

            @Override
            public void onSuccess(String response) {

                dismiss();
                if (handler == null){
                    handler = new Handler();
                }
                isFujian = 1;
                handler.post(saveText);
            }

            @Override
            public void onDownload(String file, String fileUri) {

            }
        });
    }

    private void saveText(){

        String title = CommonTool.getText(titleEdit);
        String content = IApplication.isTeacher() ?
                (path == null ? title : path.substring(path.lastIndexOf("/") + 1)) : CommonTool.getText(contentEdit);

        if (content == null || content.length() < 1){
            ToastUtil.show(mContext, IApplication.isTeacher() ? "请选择文件！" : "请输入内容!");
            return;
        }

        if (title == null || title.length() < 1){
            title = content.substring(0, content.length() < 30 ? content.length() : 30);
        }

        showProcess("正在保存...");
        if(!HttpAccount.newBiJi(getTAG(), String.valueOf(id),
                title, content, isFujian, mContext, httpListener)){

            dismiss(2000);
        }
    }

    private void submit(){

        //  如果有实体， 则是删除操作
        if (model != null){
            doDelete();
            return;
        }

        //  如果不是老师或者未选择文件 则仅仅保存文本
        if (!IApplication.isTeacher() || path == null){
            saveText();
        }

        //  否则上传文件
        else{
            uploadFile();
        }
    }

    private Runnable saveText = new Runnable() {
        @Override
        public void run() {
            saveText();
        }
    };

    @Override
    public Class getType() {
        return UserInfo.class;
    }

    @Override
    public void onSuccess(String object, String taskId) {

        UserInfo userInfo = getData();
        if (userInfo == null || userInfo.getResult() == null){

            return;
        }

        ToastUtil.show(mContext, userInfo.getResult().getMsg());
        if (HttpConstant.SUCCESS.equals(userInfo.getResult().getCode())) {

            finish();
        }
    }

    private void contentClick(){

        if (model == null || !model.isFuJian()){
            return;
        }

        //  如果是 mp4 则播放视频
        if (model.getInfo() != null && model.getInfo().endsWith(".mp4")){

            Bundle bundle2 = new Bundle();
            bundle2.putString("uri", model.getInfo().startsWith("http://") ?
                    model.getInfo() : HttpConstant.FILES + model.getInfo());
            newActivity(new Intent(mContext, VideoPlay.class).putExtras(bundle2));
            return;
        }

        SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);
        dialog.setTitleText("下载资料").setContentText("你确定下载这份资料吗？")
                .setConfirmText("下载").setCancelText("取消")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        sweetAlertDialog.cancel();
                        down();
                    }
                }).show();
    }

    private void down(){

        String f = FileUtils.getAvailablePath(mContext, Constant.FILE_PATH) + "/" + model.getInfo();
        final File file = new File(f);
        if (file.exists()){
            FileUtils.showFile(file.getParent(), mContext);
            ToastUtil.show(mContext, "文件已下载到: " + f);
            return;
        }if (downManager != null){
            ToastUtil.show(mContext, "文件下载中，请稍后...");
        }else{
            ToastUtil.show(mContext, "文件将下载到: " + f);
            downManager = new DownManager(mContext, HttpConstant.FILES + model.getInfo(), new DownManager.downloadListener() {
                @Override
                public void onDownloadFinish(String filePath, String fileUri) {
                    FileUtils.showFile(file.getParent(), mContext);
                }
            });
            downManager.setSaveFilePath(file);
            downManager.startDown();
        }
    }

    @OnClick({R.id.right, R.id.back, R.id.select, R.id.content})
    public void onClick(View view){

        if (view == null){
            return;
        }

        switch (view.getId()){

            case R.id.back:
                finish();
                break;

            case R.id.select:
                CommonTool.chooseFile(this, "mp4", SELECT_FILE);
                break;

            case R.id.content:
                contentClick();
                break;

            case R.id.right:
                submit();
                break;

            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){

        super.onActivityResult(requestCode, resultCode, intent);
        if (RESULT_OK != resultCode){
            ILog.i("===failed===");
            return;
        }

        switch (requestCode){

            case SELECT_FILE:
                Uri uri = intent.getData();
                path = FileUtils.getPath(this, uri);
                ILog.i("===path===", path);
                break;

            default:
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_new_biji;
    }
}
