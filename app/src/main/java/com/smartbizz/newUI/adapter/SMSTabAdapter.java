package com.smartbizz.newUI.adapter;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.smartbizz.newUI.fragments.CreateGroupFragment;
import com.smartbizz.newUI.fragments.MSmsSenderFragment;
import com.smartbizz.newUI.fragments.SMSReportFragment;
import com.smartbizz.newUI.fragments.SMSSenderIDUpdateFragment;
import com.smartbizz.newUI.newViews.SMSBalanceActivity;
import com.smartbizz.newUI.newViews.SMSReportActivity;

public class SMSTabAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public SMSTabAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Bundle bundle = new Bundle();
                bundle.putString("msg", "");
                MSmsSenderFragment fragobj = new MSmsSenderFragment();
                fragobj.setArguments(bundle);
//                MSmsSenderFragment homeFragment = new MSmsSenderFragment();
                return fragobj;
            case 1:
                CreateGroupFragment createGroupFragment = new CreateGroupFragment();
                return createGroupFragment;
            case 2:
                SMSSenderIDUpdateFragment smsSenderIDUpdateFragment = new SMSSenderIDUpdateFragment();
                return smsSenderIDUpdateFragment;
            case 3:
                SMSReportFragment smsReportFragment = new SMSReportFragment();
                return smsReportFragment;
            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }

}