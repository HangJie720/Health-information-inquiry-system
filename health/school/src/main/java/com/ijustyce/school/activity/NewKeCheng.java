package com.ijustyce.school.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ijustyce.fastandroiddev.base.BaseActivity;
import com.ijustyce.fastandroiddev.baseLib.utils.CommonTool;
import com.ijustyce.fastandroiddev.baseLib.utils.ToastUtil;
import com.ijustyce.school.R;
import com.ijustyce.school.constant.HttpConstant;
import com.ijustyce.school.http.HttpAccount;
import com.ijustyce.school.model.UserInfo;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by yc on 16-3-19.    发布课程
 */
public class NewKeCheng extends BaseActivity<UserInfo> {

    @Bind(R.id.time)
    Spinner selectTime;
    @Bind(R.id.week)
    Spinner selectWeek;
    @Bind(R.id.name)
    EditText name;
    @Bind(R.id.address)
    EditText address;

    @Bind(R.id.label)
    TextView label;

    String[] weeks, times;

    @Override
    public int getLayoutId() {
        return R.layout.activity_new_kecheng;
    }

    @OnClick({R.id.submit, R.id.back})
    public void onClick(View view) {

        if (view == null) {
            return;
        }

        switch (view.getId()){

            case R.id.submit:
                doSubmit();
                break;

            case R.id.back:
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    public void afterCreate() {
        super.afterCreate();

        weeks = getResources().getStringArray(R.array.week);
        times = getResources().getStringArray(R.array.time);

        label.setText("发布课程");
    }

    private String getWeek(int position) {

        if (position < 0 || position >= weeks.length) {
            return null;
        }
        return weeks[position];
    }

    private String getTime(int position) {

        if (position < 0 || position >= times.length) {
            return null;
        }
        return times[position];
    }

    private synchronized void doSubmit() {

        int index = selectTime == null ? 0 : selectTime.getSelectedItemPosition() + 1;
        String week = selectWeek == null ? null : getWeek(selectWeek.getSelectedItemPosition());
        String time = selectTime == null ? null : getTime(selectTime.getSelectedItemPosition());

        String nameText = CommonTool.getText(name);
        String addressText = CommonTool.getText(address);

        if (week == null || time == null || nameText == null || addressText == null){

            ToastUtil.show(mContext, "选择或输入不能为空！");
            return;
        }
        if (nameText.length() < 2 || addressText.length() < 4){

            ToastUtil.show(mContext, "课程名不能小于2，地点不能小于4");
            return;
        }

        showProcess("正在发布课程...");
        if (!HttpAccount.newKeCheng(week, time, addressText, nameText,
                getTAG(), index, mContext, httpListener)){

            dismiss(2000);
        }
    }

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

        if (userInfo.getResult().getCode().equals(HttpConstant.SUCCESS)){

            ToastUtil.show(mContext, "课程发布成功!");
            finish();
        }else{
            ToastUtil.show(mContext, userInfo.getResult().getMsg());
        }
    }
}
