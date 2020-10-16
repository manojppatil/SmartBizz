package com.smartbizz.newUI.fragments;

import android.app.Fragment;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smartbizz.R;
import com.smartbizz.Util.CommonUtil;
import com.smartbizz.Util.Constants;
import com.smartbizz.Util.PreferenceManager;
import com.smartbizz.Util.VerticalLineDecorator;
import com.smartbizz.newUI.adapter.TemplateMsgAdapter;
import com.smartbizz.newUI.network.ApiConstants;
import com.smartbizz.newUI.network.NetworkManager;
import com.smartbizz.newUI.newViews.PostCardTabActivity;
import com.smartbizz.newUI.newViews.TemplateActivity;
import com.smartbizz.newUI.pojo.Category;
import com.smartbizz.newUI.pojo.Requests;
import com.smartbizz.newUI.pojo.SMSTemplates;
import com.smartbizz.newUI.pojo.Templatemsg;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TemplateCardFragment extends BaseFragment{
    View view;
    String val;
    TemplateMsgAdapter beatAdapter;
    List<Templatemsg> smsTemplatesList;
    public static final int REQUEST = 112;
    public int beatCount = 0;
    private Category category;
    private TextView txtNoData;
    Fragment currentFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_templatecard, container, false);
        txtNoData = view.findViewById(R.id.txtNoData);

        val = getArguments().getString("someInt");
        ((TemplateActivity) activity).recyclerView = view.findViewById(R.id.recycler_templatecard);
//        c.setText("Fragment - " + val);
        new TemplateCardFragment.AsyncTaskLoadLogo().execute();

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        reloadData();
        BeatListAPI(val);
    }

    public static TemplateCardFragment addfrag(int val, SMSTemplates smsTemplates) {
        TemplateCardFragment fragment = new TemplateCardFragment();
        Bundle args = new Bundle();
        args.putString("someInt", smsTemplates.getId());
        fragment.setArguments(args);

        return fragment;
    }

    public void reloadData() {

        smsTemplatesList = null;
        beatAdapter = null;

        smsTemplatesList = new ArrayList<>();

        beatAdapter = new TemplateMsgAdapter(activity, smsTemplatesList, currentFragment);

//        beatAdapter.setLoadMoreListener(() -> {
//
//            ((PostCardTabActivity) activity).recyclerView.post(new Runnable() {
//                @Override
//                public void run() {
//
//                    BeatListAPI("3");
//                }
//            });
//            //Calling loadMore function in Runnable to fix the
//        });

        ((TemplateActivity) activity).recyclerView.setHasFixedSize(true);

        ((TemplateActivity) activity).recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        ((TemplateActivity) activity).recyclerView.addItemDecoration(new VerticalLineDecorator(2));
        ((TemplateActivity) activity).recyclerView.setAdapter(beatAdapter);

    }

    private void BeatListAPI(String id) {

        NetworkManager.getInstance(activity).getTemplatesCatgList(activity,id, response -> {
            if (response.isSuccess()) {
                JSONObject jsonObject = response.getResponse();
                if (jsonObject != null) {
                    JSONObject resultObj = jsonObject.optJSONObject(ApiConstants.Keys.RESULT);
                    if (resultObj != null) {
                        JSONArray sms_category = resultObj.optJSONArray("sms_category");

                        if (sms_category != null && sms_category.length() > 0) {
//                            mBeatsList.remove(mBeatsList.size() - 1);
                            beatCount = smsTemplatesList.size();

                            int size = sms_category.length();
                            for (int i = 0; i < size; i++) {
                                JSONObject applicationJson = sms_category.optJSONObject(i);
                                if (applicationJson != null) {
                                    Templatemsg templatemsg = new Templatemsg(sms_category.optJSONObject(i));
                                    smsTemplatesList.add(templatemsg);
                                }
                            }
                            if (beatCount == smsTemplatesList.size()) {
                                beatAdapter.setMoreDataAvailable(false);
                                CommonUtil.makeToast(activity, "No More Data Available");
                            }
                            beatAdapter.notifyDataChanged();
                            beatAdapter.setMoreDataAvailable(false);
                            txtNoData.setVisibility(View.GONE);
                        }else{
                            txtNoData.setVisibility(View.VISIBLE);
                        }
                    }
                }
            } else {
                makeToast(response.getMessage());
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //do here
                } else {
                    Toast.makeText(activity, "The app was not allowed to read your store.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public class AsyncTaskLoadLogo extends AsyncTask<String, String, Bitmap> {
        private final static String TAG = "AsyncTaskLoadImage";

        public void AsyncTaskLoadImage() {
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmapLogo = null;
            try {
                URL url = new URL(ApiConstants.BASE_URL + "/" + PreferenceManager.getString(activity, Constants.PrefKeys.LOGO));
//                URL url = new URL("https://digiepost.in/assets/uploads/gallery/20/20_1596904590.jpg");
                try {
                    bitmapLogo = BitmapFactory.decodeStream((InputStream) url.getContent()).copy(Bitmap.Config.ARGB_8888, true);
                } catch (NullPointerException e) {
                    bitmapLogo = null;
                    e.printStackTrace();
                }
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
            return bitmapLogo;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            try {

                float aspectRatio = bitmap.getWidth() /
                        (float) bitmap.getHeight();

                int width = 250;
                int height = Math.round(width / aspectRatio);

                bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);

//                bitmap = getCircularBitmap(bitmap);

                bitmap = Bitmap.createScaledBitmap(bitmap, 250, 250, true);
                // Add a border around circular bitmap
//                bitmap = addBorderToCircularBitmap(bitmap, 5, Color.WHITE);
                // Add a shadow around circular bitmap
//                bitmap = addShadowToCircularBitmap(bitmap, 4, Color.LTGRAY);
                // Set the ImageView image as drawable object
                ((PostCardTabActivity)activity).bitmapLogo = bitmap;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}


