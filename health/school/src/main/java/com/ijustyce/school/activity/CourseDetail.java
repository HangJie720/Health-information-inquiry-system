package com.ijustyce.school.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ijustyce.fastandroiddev.base.BaseListActivity;
import com.ijustyce.fastandroiddev.baseLib.utils.CommonTool;
import com.ijustyce.fastandroiddev.baseLib.utils.ILog;
import com.ijustyce.fastandroiddev.baseLib.utils.ImageUtils;
import com.ijustyce.fastandroiddev.baseLib.utils.RegularUtils;
import com.ijustyce.fastandroiddev.baseLib.utils.StringUtils;
import com.ijustyce.fastandroiddev.irecyclerview.CommonHolder;
import com.ijustyce.fastandroiddev.irecyclerview.IAdapter;
import com.ijustyce.school.IApplication;
import com.ijustyce.school.R;
import com.ijustyce.school.constant.HttpConstant;
import com.ijustyce.school.http.HttpAccount;
import com.ijustyce.school.model.BiJi;
import com.ijustyce.school.model.CourseModel;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by yc on 16-3-19.    课程详情
 */
public class CourseDetail extends BaseListActivity<BiJi.DataEntity> {

    CourseModel.DataEntity model;

//    @Bind(R.id.label)
//    TextView label;

    @Bind(R.id.right)
    TextView right;

    @Bind(R.id.right1)
    TextView right1;

    @Override
    public Class getType() {
        return BiJi.class;
    }

    @Override
    public void afterCreate() {

        //   label.setText("课程详情");
        right.setText(IApplication.isTeacher() ? "上传资料" : "新建笔记");
        right.setVisibility(View.VISIBLE);
        right1.setText("上传资料");
        right1.setVisibility(IApplication.isTeacher() ? View.GONE : View.VISIBLE);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null || !bundle.containsKey("item")) {
            ILog.e("===CourseDetail===", "no bundle or key...");
            finish();
            return;
        }
        model = (CourseModel.DataEntity) bundle.getSerializable("item");

        if (model == null) {
            ILog.e("===CourseDetail===", "model is null...");
            finish();
        }

        bundle.clear();

        ILog.i("===view===", "now add view...");
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_kebiao, null);
        CommonHolder commonHolder = new CommonHolder(view, mContext);
        createView(commonHolder, model);
        mIRecyclerView.addHeaderView(view);
    }

    public void createView(CommonHolder holder, final CourseModel.DataEntity object) {

        holder.setText(R.id.course, getResString(R.string.item_course).replace("#course#", object.getName()));
        holder.setText(R.id.time, getResString(R.string.item_time).replace("#time#", object.get_time()));
        holder.setText(R.id.address, getResString(R.string.item_address).replace("#address#", object.getAddress()));
        holder.setText(R.id.teacher, getResString(R.string.item_teacher).replace("#teacher#", object.getTeacherName()));
    }

    @Override
    public boolean getMoreData() {

        if (model == null) {
            ILog.e("===CourseDetail===", "model is null...");
            finish();
            return false;
        }
        return HttpAccount.getBiJi(getTAG(), model.getId(), pageNo, mContext, httpListener);
    }

    @Override
    public IAdapter<BiJi.DataEntity> buildAdapter(Context mContext, List<BiJi.DataEntity> data) {
        return new BiJiAdapter(data, mContext);
    }

    @OnClick({R.id.back, R.id.right, R.id.right1})
    public void onClick(View view) {

        if (view == null) {
            return;
        }

        switch (view.getId()) {

            case R.id.back:
                finish();
                break;

            case R.id.right:
                Bundle bundle = new Bundle();
                bundle.putInt("id", StringUtils.getInt(model.getId()));
                Intent intent = new Intent(mContext, NewBiJi.class);
                newActivity(intent.putExtras(bundle));
                break;

            case R.id.right1:
                Bundle bundle1 = new Bundle();
                bundle1.putInt("id", StringUtils.getInt(model.getId()));
                Intent intent1 = new Intent(mContext, NewBiJi2.class);
                newActivity(intent1.putExtras(bundle1));
                break;


            default:
                break;
        }
    }

    public class BiJiAdapter extends IAdapter<BiJi.DataEntity> {

        @Override
        public void createView(CommonHolder holder, final BiJi.DataEntity object) {
            holder.setText(R.id.title, object.getDescript());
            holder.setText(R.id.content, object.getInfo());

            if (RegularUtils.isImage(object.getHead())) {

                ImageUtils.load(mContext, (ImageView) holder.getView(R.id.header),
                        HttpConstant.FILES + object.getHead());
            } else {
                ((ImageView) holder.getView(R.id.header)).setImageBitmap
                        (CommonTool.drawableToBitmap(getResources().getDrawable(R.mipmap.default_head)));
            }

            holder.setOnClickListener(R.id.item, new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle1 = new Bundle();
                    bundle1.putSerializable("item", object);
                    newActivity(new Intent(mContext, NewBiJi.class).putExtras(bundle1));
                }
            });
        }

        public BiJiAdapter(List<BiJi.DataEntity> mData, Context mContext) {
            super(mData, mContext);
        }

        @Override
        public CommonHolder createViewHolder(Context mContext, ViewGroup parent) {
            return CommonHolder.getInstance(R.layout.item_biji, mContext, parent);
        }
    }
}
