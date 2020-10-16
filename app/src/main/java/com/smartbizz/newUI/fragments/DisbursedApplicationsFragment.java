package com.smartbizz.newUI.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.smartbizz.R;
import com.smartbizz.Util.Constants;
import com.smartbizz.Util.DialogUtil;
import com.smartbizz.newUI.interfaces.OnApplicationEditListener;
import com.smartbizz.newUI.network.NetworkManager;
import com.smartbizz.newUI.newViews.DesignActivity;
import com.smartbizz.newUI.pojo.Category;
import com.smartbizz.newUI.pojo.MCategory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.smartbizz.newUI.adapter.SpinnerAdapter;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class DisbursedApplicationsFragment extends BaseFragment implements OnApplicationEditListener, AdapterView.OnItemSelectedListener {

    private RecyclerView rvApplications;
    private AppCompatSpinner spinnerCategory;
    List<MCategory> spCategoryList;
    private String categoryId = Constants.EMPTY;
    private Button btnSubmit;
    private EditText etRemarks;

//    private String baseUrl;

    public DisbursedApplicationsFragment() {
        // Required empty public constructor
    }
//    public DisbursedApplicationsFragment(String baseurl) {
//        baseUrl = baseurl;
//    }

//    public static DisbursedApplicationsFragment newInstance(List<Application> list, String baseurl) {
//        DisbursedApplicationsFragment fragment = new DisbursedApplicationsFragment(baseurl);
//        Bundle args = new Bundle();
//        args.putSerializable(Constants.Extras.APPLICATIONS, (Serializable) list);
//        fragment.setArguments(args);
//        return fragment;
//    }

    public static DisbursedApplicationsFragment newInstance(List<Category> list) {
        DisbursedApplicationsFragment fragment = new DisbursedApplicationsFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.Extras.APPLICATIONS, (Serializable) list);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_disbursed_applications, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setAdapter();
    }

    private void initViews(View view) {

        rvApplications = view.findViewById(R.id.rvApplications);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        etRemarks = view.findViewById(R.id.etRemarks);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        spinnerCategory.setOnItemSelectedListener(this);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    private void submit() {

        if (categoryId == null && etRemarks.getText().toString().trim().length() < 1) {
            return;
        }
        DialogUtil.showProgressDialog(activity);
        NetworkManager.getInstance(activity).putRequest(activity, categoryId, etRemarks.getText().toString(), response -> {
            DialogUtil.dismissProgressDialog();
            if (response.isSuccess()) {
                JSONObject jsonObject = response.getResponse();
                if (jsonObject != null) {
                    try {
                        makeToast(response.getResponse().optString("result"));
                        etRemarks.setText("");
                        spCategoryList.clear();
                        ((DesignActivity) activity).getApplicationList();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                try {
                    makeToast(response.getResponse().optString("result"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void setLoanTypeAdapter(List<MCategory> list) {
        //Rented/Owned Adapter
        SpinnerAdapter loanTypeSpinnerAdapter = new SpinnerAdapter<>(activity, R.layout.spinner_item, list);
        loanTypeSpinnerAdapter.setDropDownViewResource(R.layout.spinner_drop_down_item);
        spinnerCategory.setAdapter(loanTypeSpinnerAdapter);
        spinnerCategory.setSelection(0);
    }

    private void setAdapter() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            List<Category> disbursedList = (List<Category>) bundle.getSerializable(Constants.Extras.APPLICATIONS);

            spCategoryList = new ArrayList<>();
            spCategoryList.add(new MCategory("-1", "Select Category"));

            for (int i = 0; i < disbursedList.size(); i++) {
                spCategoryList.add(new MCategory(disbursedList.get(i).getId(), disbursedList.get(i).getName()));
            }
            setLoanTypeAdapter(spCategoryList);

        }
    }

    @Override
    public void onEdit(Object object) {

    }

    @Override
    public void onContinue(Object object) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0) {
            if (parent.getId() == R.id.spinnerCategory) {
                categoryId = ((MCategory) parent.getSelectedItem()).getId();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
