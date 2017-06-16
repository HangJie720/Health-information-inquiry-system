package com.ijustyce.school.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.ijustyce.fastandroiddev.base.BaseTabFragment;
import com.ijustyce.school.R;

public class KebiaoFragment extends BaseTabFragment {

    private String[] days;
    @Override
    public void addTitle() {

        days = getResources().getStringArray(R.array.week);

        mTitleList.add(getResString(R.string.week_1));
        mTitleList.add(getResString(R.string.week_2));
        mTitleList.add(getResString(R.string.week_3));
        mTitleList.add(getResString(R.string.week_4));
        mTitleList.add(getResString(R.string.week_5));
        mTitleList.add(getResString(R.string.week_6));
        mTitleList.add(getResString(R.string.week_7));
    }

    private String getDay(int id){

        if (id < 0 || id >= days.length){
            return null;
        }
        return days[id];
    }

    @Override
    public void addFragment() {

        for (int i = 0; i < 7; i++){

            Fragment day1 = new KebiaoItemFragment();
            Bundle tmp1 = new Bundle();
            tmp1.putString("day", getDay(i));
            day1.setArguments(tmp1);

            mFragmentList.add(day1);
        }
        setScrollMode();
    }
}