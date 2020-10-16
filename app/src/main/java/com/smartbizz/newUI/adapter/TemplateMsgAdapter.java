package com.smartbizz.newUI.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartbizz.R;
import com.smartbizz.newUI.fragments.MSmsSenderFragment;
import com.smartbizz.newUI.newViews.SMSTabActivity;
import com.smartbizz.newUI.pojo.Templatemsg;

import java.util.List;

public class TemplateMsgAdapter extends RecyclerView.Adapter<TemplateMsgAdapter.ViewHolder> {
    Context context;
    List<Templatemsg> templatemsgList;
    private Fragment currentFragment;
    boolean isLoading = false, isMoreDataAvailable = true;
    OnTextClickListener listener;

    public TemplateMsgAdapter(Context context, List<Templatemsg> templatemsgs,final Fragment currentFragment) {
        this.context = context;
        this.templatemsgList = templatemsgs;
        this.currentFragment = currentFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Templatemsg templatemsg = templatemsgList.get(position);
        holder.tv_msg.setText(templatemsg.getSms());

        holder.iv_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", holder.tv_msg.getText().toString());
                clipboardManager.setPrimaryClip(clip);
                Toast.makeText(context, "Message copied", Toast.LENGTH_SHORT).show();
            }
        });

        holder.iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, holder.tv_msg.getText().toString());
                context.startActivity(Intent.createChooser(share, "Share Message"));

            }
        });

        holder.btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, SMSTabActivity.class);
                intent.putExtra("selectedmsg",holder.tv_msg.getText().toString());
                context.startActivity(intent);

                Bundle args = new Bundle();
                args.putString("sms", holder.tv_msg.getText().toString());
                //set Fragmentclass Arguments
                MSmsSenderFragment fragobj = new MSmsSenderFragment()   ;
                fragobj.setArguments(args);
                FragmentManager fragmentManager = currentFragment.getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.sms_layout, currentFragment);
                fragmentTransaction.hide(currentFragment);
                fragmentTransaction.addToBackStack(currentFragment.getClass().getSimpleName());
                fragmentTransaction.commit();

            }
        });
    }

    public void notifyDataChanged() {
        notifyDataSetChanged();
        isLoading = false;
    }

    @Override
    public int getItemCount() {
        return templatemsgList.size();
    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_msg;
        ImageView iv_copy, iv_share;
        Button btn_select;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_msg = itemView.findViewById(R.id.tv_msg);
            iv_copy = itemView.findViewById(R.id.iv_msg_copy);
            iv_share = itemView.findViewById(R.id.iv_msg_share);
            btn_select = itemView.findViewById(R.id.btn_msg_select);
        }
    }

    interface OnTextClickListener {
        void onTextClick(Templatemsg data);
    }
}
