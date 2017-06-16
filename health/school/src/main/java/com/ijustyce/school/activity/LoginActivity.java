package com.ijustyce.school.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ijustyce.fastandroiddev.base.BaseActivity;
import com.ijustyce.fastandroiddev.baseLib.utils.CommonTool;
import com.ijustyce.fastandroiddev.baseLib.utils.IJson;
import com.ijustyce.fastandroiddev.baseLib.utils.RegularUtils;
import com.ijustyce.fastandroiddev.baseLib.utils.ToastUtil;
import com.ijustyce.school.IApplication;
import com.ijustyce.school.R;
import com.ijustyce.school.constant.HttpConstant;
import com.ijustyce.school.http.HttpAccount;
import com.ijustyce.school.model.UserInfo;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by yc on 16-3-14.    登录界面
 */
public class LoginActivity extends BaseActivity<UserInfo> {

    @Bind(R.id.label)
    TextView label;

    @Bind(R.id.userId)
    EditText userId;

    @Bind(R.id.password)
    EditText password;

    private String pw, userEmail;

    private static LoginCallBack callBack;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void afterCreate() {
        super.afterCreate();

        label.setText("登录");
    }

    private void doLogin(){

        pw = CommonTool.getText(password);
        userEmail = CommonTool.getText(userId);

        if (!RegularUtils.isEmail(userEmail)){

            ToastUtil.show(mContext, "邮箱格式错误!");
            return;
        }

        showProcess("正在为你登陆...");
        if(!HttpAccount.login(getTAG(), userEmail, pw, mContext, httpListener)){
            dismiss();
        }
        findViewById(R.id.login).setClickable(false);
    }

    @Override
    public void onFailed(int code, String msg, String taskId){

        if (handler != null) {
            handler.post(enableClick);
        }
    }

    @Override
    public void onSuccess(String object, String taskId) {

        if (handler != null) {
            handler.post(enableClick);
        }
        UserInfo userInfo = getData();
        if (userInfo == null || userInfo.getResult() == null){

            return;
        }

        if (HttpConstant.SUCCESS.equals(userInfo.getResult().getCode())) {

            saveUserInfo(userInfo);
            if (callBack != null) {
                callBack.onSuccess();
            }
            finish();
        }
        else{
            ToastUtil.show(mContext, userInfo.getResult().getMsg());
        }
    }

    private Runnable enableClick = new Runnable() {
        @Override
        public void run() {
            findViewById(R.id.login).setClickable(true);
        }
    };

    @Override
    public Class getType(){

        return UserInfo.class;
    }

    private void saveUserInfo(UserInfo userInfo){

        userInfo.setPw(pw);
        userInfo.setUserId(userEmail);

        IApplication.setUserInfo(userInfo);

        SharedPreferences shared = getSharedPreferences("userInfo", MODE_PRIVATE);
        shared.edit().clear().putString("userInfo", IJson.toJson(userInfo, UserInfo.class)).apply();
    }

    @OnClick({R.id.register, R.id.login, R.id.back})
    public void onClick(View view){

        if (view == null){
            return;
        }

        switch (view.getId()){

            case R.id.register:
                newActivity(RegisterActivity.class);
                finish();
                break;

            case R.id.login:
                doLogin();
                break;

            case R.id.back:
                finish();
                break;

            default:
                break;
        }
    }

    public static void doLogin(LoginCallBack callBack, Activity activity){

        LoginActivity.callBack = callBack;
        activity.startActivity(new Intent(activity, LoginActivity.class));
    }

    public interface LoginCallBack{

        public void onSuccess();
    }
}
