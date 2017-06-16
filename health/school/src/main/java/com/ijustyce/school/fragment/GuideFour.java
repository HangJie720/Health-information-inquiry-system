package com.ijustyce.school.fragment;

import android.view.View;

import com.ijustyce.fastandroiddev.base.BaseFragment;
import com.ijustyce.fastandroiddev.baseLib.utils.ILog;
import com.ijustyce.school.R;
import com.ijustyce.school.activity.LoginActivity;
import com.ijustyce.school.activity.MainActivity;
import com.ijustyce.school.activity.RegisterActivity;

import butterknife.OnClick;

/**
 * Created by yc on 16-3-14.    第三个引导页
 */
public class GuideFour extends BaseFragment {

    @Override
    public int getLayoutId() {
        return R.layout.fragment_guide_four;
    }

    @OnClick({R.id.login, R.id.register})
    public void onClick(View view){

        if (view == null){

            return;
        }

        switch (view.getId()){

            case R.id.login:
                LoginActivity.doLogin(new LoginActivity.LoginCallBack() {
                    @Override
                    public void onSuccess() {

                        ILog.i("===login===", "login success ...");
                        if (mContext != null){
                            newActivity(MainActivity.class);
                            getActivity().finish();
                        }
                    }
                }, getActivity());
                break;

            case R.id.register:
                newActivity(RegisterActivity.class);
                getActivity().finish();
                break;

            default:
                break;
        }
    }
}