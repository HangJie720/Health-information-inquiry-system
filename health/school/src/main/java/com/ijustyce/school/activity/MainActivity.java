package com.ijustyce.school.activity;

import android.view.View;
import android.widget.TextView;

import com.ijustyce.fastandroiddev.base.BaseTabActivity;
import com.ijustyce.fastandroiddev.umenglib.UpdateTool;
import com.ijustyce.school.IApplication;
import com.ijustyce.school.R;
import com.ijustyce.school.fragment.CourseFragment;
import com.ijustyce.school.fragment.KebiaoFragment;
import com.ijustyce.school.fragment.MeFragment;

import butterknife.Bind;

public class MainActivity extends BaseTabActivity {

    @Bind(R.id.right)
    TextView right;
    @Override
    public void addFragment() {

        mFragmentList.add(new CourseFragment());
        mFragmentList.add(new KebiaoFragment());
        mFragmentList.add(new MeFragment());
    }

    @Override
    public void onPageSelect(int position) {
        super.onPageSelect(position);
        if (position != 0){
            right.setVisibility(View.INVISIBLE);
        }else {
            right.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void afterCreate() {

        addTab(R.layout.tab_course, R.id.radioButton);
        addTab(R.layout.tab_kebiao, R.id.radioButton);
        addTab(R.layout.tab_me, R.id.radioButton);

        UpdateTool.update(this);
        onPageSelect(0);

        right.setText(IApplication.isTeacher() ? "发布课程" : "搜索课程");
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newActivity(IApplication.isTeacher() ? NewKeCheng.class : SearchCourse.class);
            }
        });
    }
}
