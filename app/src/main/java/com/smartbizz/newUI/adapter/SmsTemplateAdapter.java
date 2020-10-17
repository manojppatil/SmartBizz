package com.smartbizz.newUI.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartbizz.R;
import com.smartbizz.newUI.newViews.SmsTemplateActivity;
import com.smartbizz.newUI.newViews.TemplateActivity;
import com.smartbizz.newUI.pojo.SMSTemplates;

import java.util.List;

public class SmsTemplateAdapter extends RecyclerView.Adapter<SmsTemplateAdapter.ViewHolder> {
    Context context;
    String Tag;
    List<SMSTemplates> smsTemplatesList;

    public SmsTemplateAdapter(Context context, List<SMSTemplates> smsTemplates, String tag) {
        this.context = context;
        this.smsTemplatesList = smsTemplates;
        this.Tag = tag;
    }

    public SmsTemplateAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (Tag.equalsIgnoreCase("ROW")) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sms_temprow_layout, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sms_tempcat_layout, parent, false);
        }

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SMSTemplates smsTemplates = smsTemplatesList.get(position);
        holder.smscat_name.setText(smsTemplates.getCategory());
        holder.temp_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             Intent intent = new Intent(context, TemplateActivity.class);
             context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return smsTemplatesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView smscat_icon;
        TextView smscat_name;
        LinearLayout temp_cat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            smscat_icon = itemView.findViewById(R.id.smscat_icon);
            smscat_name = itemView.findViewById(R.id.smscat_name);
            temp_cat = itemView.findViewById(R.id.temp_cat_layout);
        }
    }
}
