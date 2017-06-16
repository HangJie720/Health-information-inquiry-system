package com.ijustyce.school.activity;

import android.view.View;
import android.widget.TextView;

import com.ijustyce.fastandroiddev.base.BaseActivity;
import com.ijustyce.fastandroiddev.baseLib.utils.CommonTool;
import com.ijustyce.school.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by yc on 16-3-20.
 */
public class AboutActivity extends BaseActivity {

    @Bind(R.id.label)
    TextView label;

    @Bind(R.id.about_version_info)
    TextView version;

    @Override
    public int getLayoutId() {

        return R.layout.activity_about;
    }

    @Override
    public void afterCreate() {

        label.setText("关于");
        String versionInfo = CommonTool.getVersion(mContext);
        version.setText(versionInfo);
    }

    @OnClick({R.id.back})
    public void onClick(View view){

        if (view == null){
            return;
        }

        switch (view.getId()){

            case R.id.back:
                finish();
                break;

            default:
                break;
        }
    }
}
