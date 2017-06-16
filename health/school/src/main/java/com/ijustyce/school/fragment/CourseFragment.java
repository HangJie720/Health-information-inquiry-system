package com.ijustyce.school.fragment;

import android.content.Context;
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
import com.ijustyce.school.http.HttpAccount;
import com.ijustyce.school.model.CourseModel;
import com.ijustyce.school.model.UserInfo;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CourseFragment extends BaseListFragment<CourseModel.DataEntity> {

    private CourseAdapter adapter;

    @Override
    public boolean getMoreData() {

        return HttpAccount.getAllKeCheng(getTAG(), mContext, pageNo, httpListener);
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
    }

    private void doItemClick(CourseModel.DataEntity model){

        if (!IApplication.isLogin()){
            return;
        }

        if (IApplication.isTeacher()){

            doTeacherClick(model);
            return;
        }

        doStudentClick(model);
    }

    private void doStudentClick(final CourseModel.DataEntity model){

        if (!IApplication.isLogin() || model == null){

            return;
        }

        SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE);
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

    private void doTeacherClick(final CourseModel.DataEntity model){

        if (!IApplication.isLogin() || model == null ||
                !model.getTeacherId().equals(IApplication.getUserInfo().getId())){

            return;
        }

        SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE);
        dialog.setTitleText("删除课程").setContentText("你确定删除这门课程吗？")
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

        if (!HttpAccount.deleteKeCheng(id, getTAG(), mContext, listener)){
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

    private class CourseAdapter extends IAdapter<CourseModel.DataEntity>{

        @Override
        public CommonHolder createViewHolder(Context mContext, ViewGroup parent) {
            return CommonHolder.getInstance( R.layout.item_course, mContext, parent);
        }

        @Override
       public void createView(CommonHolder holder, final CourseModel.DataEntity object) {

           holder.setText(R.id.course, getResString(R.string.item_course).replace("#course#", "" + object.getName()));
           holder.setText(R.id.time, getResString(R.string.item_time).replace("#time#",
                   "" + object.getDay() + "  " + object.get_time()));
           holder.setText(R.id.address, getResString(R.string.item_address).replace("#address#", "" + object.getAddress()));
           holder.setText(R.id.teacher, getResString(R.string.item_teacher).replace("#teacher#", "" + object.getTeacherName()));
            holder.setOnLongClickListener(R.id.item, new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    doItemClick(object);
                    return false;
                }
            });
       }

        public CourseAdapter(List<CourseModel.DataEntity> mData, Context mContext) {
            super(mData, mContext);
        }
    }
}
