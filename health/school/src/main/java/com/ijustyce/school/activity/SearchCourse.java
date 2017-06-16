package com.ijustyce.school.activity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ijustyce.fastandroiddev.base.BaseListActivity;
import com.ijustyce.fastandroiddev.baseLib.utils.IJson;
import com.ijustyce.fastandroiddev.baseLib.utils.StringUtils;
import com.ijustyce.fastandroiddev.baseLib.utils.ToastUtil;
import com.ijustyce.fastandroiddev.irecyclerview.CommonHolder;
import com.ijustyce.fastandroiddev.irecyclerview.IAdapter;
import com.ijustyce.fastandroiddev.net.HttpListener;
import com.ijustyce.school.IApplication;
import com.ijustyce.school.R;
import com.ijustyce.school.http.HttpAccount;
import com.ijustyce.school.model.CourseModel;
import com.ijustyce.school.model.UserInfo;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by yc on 16-3-22.    搜索课程
 */
public class SearchCourse extends BaseListActivity<CourseModel.DataEntity> {

    private CourseAdapter adapter;

    @Bind(R.id.editText)
    EditText editText;

    @Bind(R.id.right)
    TextView right;

    @Override
    public boolean getMoreData() {

        String key = getKeyWord();
        return key != null && HttpAccount.searchKeCheng(getTAG(), key, mContext, pageNo, httpListener);
    }

    private String getKeyWord(){

        return editText == null || StringUtils.isEmpty(editText.getText().toString())
                ? null : editText.getText().toString();
    }

    @Override
    public IAdapter<CourseModel.DataEntity> buildAdapter(Context mContext, List<CourseModel.DataEntity> data) {

        if (adapter == null){
            adapter = new CourseAdapter(data, mContext);
        }
        return adapter;
    }

    @Override
    public void afterCreate() {
        super.afterCreate();

        editText.setVisibility(View.VISIBLE);
        right.setVisibility(View.VISIBLE);
        right.setText("搜索");
    }

    @OnClick({R.id.back, R.id.right})
    public void onClick(View view){

        if (view == null){
            return;
        }

        switch (view.getId()){

            case R.id.back:
                backPress();
                break;

            case R.id.right:
                pageNo = 1;
                doResume();
                break;

            default:
                break;
        }
    }

    private void doStudentClick(final CourseModel.DataEntity model){

        if (!IApplication.isLogin() || model == null){

            return;
        }

        SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);
        dialog.setTitleText("选定课程").setContentText("你确定选定这门课程吗？")
                .setConfirmText("选定").setCancelText("取消")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        sweetAlertDialog.cancel();
                        select(model.getId());
                    }
                }).show();
    }

    private void select(String id){

        showProcess("正在为您选课...");
        if (!HttpAccount.newKeBiao(getTAG(), id, mContext, listener)){
            dismiss(2000);
        }
    }

    private HttpListener listener = new HttpListener() {
        @Override
        public void success(String object, String taskId) {

            dismiss();
            UserInfo userInfo = IJson.fromJson(object, UserInfo.class);
            if (userInfo == null || userInfo.getResult() == null){
                return;
            }
            ToastUtil.show(mContext, userInfo.getResult().getMsg());
        }

        @Override
        public void fail(int code, String msg, String taskId) {
            dismiss();
        }
    };

    @Override
    public Class getType() {
        return CourseModel.class;
    }

    private class CourseAdapter extends IAdapter<CourseModel.DataEntity> {

        public CourseAdapter(List<CourseModel.DataEntity> mData, Context mContext) {
            super(mData, mContext);
        }

        @Override
        public CommonHolder createViewHolder(Context mContext, ViewGroup parent) {
            return CommonHolder.getInstance(R.layout.item_course, mContext, parent);
        }

        @Override
        public void createView(CommonHolder holder, final CourseModel.DataEntity object) {

            holder.setText(R.id.course, getResString(R.string.item_course).replace("#course#", "" + object.getName()));
            holder.setText(R.id.time, getResString(R.string.item_time).replace("#time#", "" + object.get_time()));
            holder.setText(R.id.address, getResString(R.string.item_address).replace("#address#", "" + object.getAddress()));
            holder.setText(R.id.teacher, getResString(R.string.item_teacher).replace("#teacher#", "" + object.getTeacherName()));

            holder.setOnLongClickListener(R.id.item, new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    doStudentClick(object);
                    return false;
                }
            });
        }
    }
}
