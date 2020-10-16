package com.smartbizz.newUI.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.smartbizz.R;
import com.smartbizz.Util.DialogUtil;
import com.smartbizz.newUI.network.NetworkManager;

import org.json.JSONException;

import static com.smartbizz.Util.DialogUtil.dismissProgressDialog;


public class SMSReportFragment extends BaseFragment {

    View view;
    private TextView tvSMSBalance;
    public Button btnRefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_s_m_s_report, container, false);


        tvSMSBalance = view.findViewById(R.id.tvSMSBalance);
        btnRefresh = view.findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(view -> getSMSBalance());

//        getSMSBalance();
        return view;

    }

    private void getSMSBalance() {
        DialogUtil.showProgressDialog(getActivity());
        NetworkManager.getInstance(getActivity()).getBalanceSMS(getActivity(), response -> {
            dismissProgressDialog();
            String msgcnt = "0";
            try {
                if (response.getResponse() != null) {
                    msgcnt = String.valueOf(response.getResponse().getJSONObject("data").get("balance"));
                } else {
                    makeToast(response.getMessage());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            tvSMSBalance.setText("Your current SMS Balance is " + msgcnt);

        });

    }

}