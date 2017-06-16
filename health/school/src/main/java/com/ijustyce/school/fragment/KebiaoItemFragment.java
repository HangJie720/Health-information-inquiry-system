package com.ijustyce.school.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ijustyce.fastandroiddev.base.BaseListFragment;
import com.ijustyce.fastandroiddev.baseLib.utils.IJson;
import com.ijustyce.fastandroiddev.baseLib.utils.ToastUtil;
import com.ijustyce.fastandroiddev.irecyclerview.CommonHolder;
import com.ijustyce.fastandroiddev.irecyclerview.IAdapter;
import com.ijustyce.fastandroiddev.net.HttpListener;
import com.ijustyce.school.IApplication;
import com.ijustyce.school.R;
import com.ijustyce.school.activity.CourseDetail;
import com.ijustyce.school.http.HttpAccount;
import com.ijustyce.school.model.CourseModel;
import com.ijustyce.school.model.UserInfo;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class KebiaoItemFragment extends BaseListFragment<CourseModel.DataEntity> {

    private CourseAdapter adapter;
    private String day;
    @Override
    public boolean getMoreData() {

        return day != null && HttpAccount.getKeBiao(getTAG(), day, pageNo, mContext, httpListener);
    }

    @Override
    public void afterCreate() {
        super.afterCreate();

        Bundle bundle = getArguments();
        day = bundle == null ? null : bundle.getString("day");

        View header = LayoutInflater.from(mContext).inflate(R.layout.view_top_kebiao2, null);
   //     mIRecyclerView.addHeaderView(header);
    }

    private void doDelete(final CourseModel.DataEntity model){

        if (!IApplication.isLogin() || model == null){

            return;
        }

        SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE);
        dialog.setTitleText("退选课程").setContentText("你确定退选这门课程吗？")
                .setConfirmText("确定").setCancelText("取消")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        sweetAlertDialog.cancel();
                        delete(model.getId());
                    }
                }).show();
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

    private void delete(String id){

        if (!HttpAccount.deleteKeBiao(getTAG(), id, mContext, listener)){
            dismiss(2000);
        }
        else if (handler != null){
            handler.postDelayed(refresh, 1000);
        }
    }

    private Runnable refresh = new Runnable() {
        @Override
        public void run() {
            refresh();
        }
    };

    @Override
    public Class getType() {
        return CourseModel.class;
    }

    @Override
    public IAdapter<CourseModel.DataEntity> buildAdapter(Context mContext, List<CourseModel.DataEntity> data) {

        CourseModel.DataEntity first = new CourseModel.DataEntity();
        if (adapter == null) {
            adapter = new CourseAdapter(data, mContext);
        }
        return adapter;
    }

    private class CourseAdapter extends IAdapter<CourseModel.DataEntity> {

        public CourseAdapter(List<CourseModel.DataEntity> mData, Context mContext) {
            super(mData, mContext);
        }

        @Override
        public CommonHolder createViewHolder(Context mContext, ViewGroup parent) {
            return CommonHolder.getInstance( R.layout.item_kebiao, mContext, parent);
        }

        @Override
        public void createView(CommonHolder holder, final CourseModel.DataEntity object) {

            holder.setText(R.id.course, getResString(R.string.item_course).replace("#course#", object.getName()));
            holder.setText(R.id.time, getResString(R.string.item_time).replace("#time#", object.get_time()));
            holder.setText(R.id.address, getResString(R.string.item_address).replace("#address#", object.getAddress()));
            holder.setText(R.id.teacher, getResString(R.string.item_teacher).replace("#teacher#", object.getTeacherName()));

//            holder.setText(R.id.course,  object.getName());
//            holder.setText(R.id.time,  object.get_time());
//            holder.setText(R.id.address, object.getAddress());
//            holder.setText(R.id.teacher, object.getTeacherName());

            if (object.getTeacherEmail() == null || object.getId() == null) return;
            holder.setOnClickListener(R.id.item, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mContext == null){
                        return;
                    }
                    Bundle tmp = new Bundle();
                    tmp.putSerializable("item", object);
                    Intent intent = new Intent(mContext, CourseDetail.class);
                    intent.putExtras(tmp);
                    mContext.startActivity(intent);
                }
            });

            if (IApplication.isTeacher()){
                return;
            }

            holder.setOnLongClickListener(R.id.item, new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    doDelete(object);
                    return false;
                }
            });
        }
    }
}