package com.smartbizz.newUI.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.smartbizz.newUI.fragments.TemplateCardFragment;
import com.smartbizz.newUI.pojo.SMSTemplates;

import java.util.ArrayList;
import java.util.List;

public class TemplateTabAdapter extends FragmentStatePagerAdapter {
    int totalTabs;
    private List<SMSTemplates> smsTemplatesList = new ArrayList<>();

    public TemplateTabAdapter(@NonNull FragmentManager fm, int totalTabs, List<SMSTemplates> smsTemplatesList) {
        super(fm);
        this.smsTemplatesList = smsTemplatesList;
        this.totalTabs = totalTabs;
    }

    @Override
    public TemplateCardFragment getItem(int position) {
        return TemplateCardFragment.addfrag(position, smsTemplatesList.get(position));
    }

    @Override
    public int getCount() {
        return totalTabs;
    }

}