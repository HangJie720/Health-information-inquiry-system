package com.ijustyce.school.activity;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.ijustyce.fastandroiddev.base.BaseActivity;
import com.ijustyce.fastandroiddev.baseLib.utils.CommonTool;
import com.ijustyce.fastandroiddev.baseLib.utils.IJson;
import com.ijustyce.fastandroiddev.baseLib.utils.RegularUtils;
import com.ijustyce.fastandroiddev.baseLib.utils.ToastUtil;
import com.ijustyce.school.R;
import com.ijustyce.school.constant.HttpConstant;
import com.ijustyce.school.http.HttpAccount;
import com.ijustyce.school.model.UserInfo;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by yc on 16-3-14.    注册界面
 */
public class RegisterActivity extends BaseActivity {

    @Bind(R.id.label)
    TextView label;

    @Bind(R.id.name)
    EditText name;

    @Bind(R.id.email)
    EditText email;

    @Bind(R.id.password)
    EditText password;

    @Bind(R.id.passwordAgain)
    EditText passwordAgain;

    @Bind(R.id.isTeacher)
    CheckBox isTeacher;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void afterCreate() {
        super.afterCreate();

        label.setText("注册");
    }

    private synchronized void register(){

        String emailText = CommonTool.getText(email);
        String nameText = CommonTool.getText(name);
        String passwordText = CommonTool.getText(password);
        String passwordAgainText = CommonTool.getText(passwordAgain);

        if (!RegularUtils.isEmail(emailText)){

            ToastUtil.show(mContext, "邮箱格式错误!");
            return;
        }

        if (nameText == null || nameText.length() < 2){

            ToastUtil.show(mContext, "姓名格式错误!");
            return;
        }

        if (passwordText == null || passwordText.length() < 6){

            ToastUtil.show(mContext, "密码不能少于6位！");
            return;
        }

        if (!passwordText.equals(passwordAgainText)){

            ToastUtil.show(mContext, "两次密码输入不一致!");
            return;
        }

        showProcess("正在为您注册账号!");
        if (!HttpAccount.register(getTAG(), emailText, passwordText, nameText,
                isTeacher.isChecked() ? 1 : 0, mContext, httpListener)){
            dismiss();
        }
        findViewById(R.id.register).setClickable(false);
    }

    @Override
    public void onFailed(int code, String msg, String taskId){

        if (handler != null) {
            handler.post(enableClick);
        }
    }

    @Override
    public void onSuccess(String json, String msg){

        if (handler != null) {
            handler.post(enableClick);
        }
        UserInfo tmp = IJson.fromJson(json, UserInfo.class);
        if (tmp == null || tmp.getResult() == null){
            return;
        }

        if (tmp.getResult().getCode().equals(HttpConstant.SUCCESS)){

            ToastUtil.show(mContext, "注册成功，请登陆");
            newActivity(LoginActivity.class);
            finish();
        }else{
            ToastUtil.show(mContext, tmp.getResult().getMsg());
        }
    }

    private Runnable enableClick = new Runnable() {
        @Override
        public void run() {
            findViewById(R.id.register).setClickable(true);
        }
    };

    @OnClick({R.id.back, R.id.register})
    public void onClick(View view){

        if (view == null){

            return;
        }

        switch (view.getId()){

            case R.id.back:
                this.finish();
                break;

            case R.id.register:
                register();
                break;
        }
    }
}
