package com.smartbizz.newUI.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.smartbizz.Util.DialogUtil;
import com.smartbizz.BuildConfig;
import com.smartbizz.R;
import com.smartbizz.Util.CommonUtil;
import com.smartbizz.Util.Globle;
import com.smartbizz.Util.PreferenceManager;
import com.smartbizz.Util.VerticalLineDecorator;
import com.smartbizz.newUI.MainApplication;
import com.smartbizz.newUI.adapter.PostCardAdapter;
import com.smartbizz.newUI.network.ApiConstants;
import com.smartbizz.newUI.network.NetworkManager;
import com.smartbizz.newUI.newViews.PostCardActivity;
import com.smartbizz.newUI.pojo.Requests;
import com.smartbizz.Util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostCardFragment extends BaseFragment implements View.OnClickListener {

    public static Context context;
    public static Fragment mFragment;
    MainApplication mainApplication;
    public static FragmentTransaction transaction;

    public static View view;
    public static final int REQUEST = 112;

    //    public RecyclerView recyclerView;
    public PostCardAdapter beatAdapter;
    List<Requests> mBeatsList;
    public int beatCount = 0;
    public static String loanTypeId = Constants.EMPTY;

    public PostCardFragment() {
        // Required empty public constructor
    }

    public static PostCardFragment newInstance(String CategoryId) {
        PostCardFragment fragment = new PostCardFragment();
        loanTypeId = CategoryId;
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_postcard, container, false);

        context = getContext();
        mFragment = new PostCardFragment();
        transaction = getFragmentManager().beginTransaction();
        mainApplication = new MainApplication();

        ((PostCardActivity) context).recyclerView = view.findViewById(R.id.recycler_beat_view);

        new AsyncTaskLoadLogo().execute();

        return view;
    }

    public class AsyncTaskLoadLogo extends AsyncTask<String, String, Bitmap> {
        private final static String TAG = "AsyncTaskLoadImage";

        public void AsyncTaskLoadImage() {
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmapLogo = null;
            try {
                    URL url = new URL(ApiConstants.BASE_URL + "/" + PreferenceManager.getString(context, Constants.PrefKeys.LOGO));
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

                ((PostCardActivity)activity).bitmapLogo = bitmap;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        reloadData();

        BeatListAPI();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void reloadData() {

        mBeatsList = null;
        beatAdapter = null;

        mBeatsList = new ArrayList<>();

        beatAdapter = new PostCardAdapter(activity, mBeatsList);

        beatAdapter.setLoadMoreListener(() -> {

            ((PostCardActivity) context).recyclerView.post(new Runnable() {
                @Override
                public void run() {

                    BeatListAPI("3");
                }
            });
            //Calling loadMore function in Runnable to fix the
        });

        ((PostCardActivity) context).recyclerView.setHasFixedSize(true);

        ((PostCardActivity) context).recyclerView.setLayoutManager(new LinearLayoutManager(context));
        ((PostCardActivity) context).recyclerView.addItemDecoration(new VerticalLineDecorator(2));
        ((PostCardActivity) context).recyclerView.setAdapter(beatAdapter);

    }

    private void BeatListAPI() {

        NetworkManager.getInstance(activity).getBrandPromotion(activity,loanTypeId, response -> {
            if (response.isSuccess()) {
                JSONObject jsonObject = response.getResponse();
                if (jsonObject != null) {
                    JSONObject resultObj = jsonObject.optJSONObject(ApiConstants.Keys.RESULT);
                    if (resultObj != null) {
                        JSONArray categoryArray = resultObj.optJSONArray("category");
                        JSONArray requestsArray = resultObj.optJSONArray("requests");

                        if (requestsArray != null && requestsArray.length() > 0) {
//                            mBeatsList.remove(mBeatsList.size() - 1);
                            beatCount = mBeatsList.size();

                            int size = requestsArray.length();
                            for (int i = 0; i < size; i++) {
                                JSONObject applicationJson = requestsArray.optJSONObject(i);
                                if (applicationJson != null) {
                                    Requests myrequest = new Requests(requestsArray.optJSONObject(i));
                                    mBeatsList.add(myrequest);
                                }
                            }
                            if (beatCount == mBeatsList.size()) {
                                beatAdapter.setMoreDataAvailable(false);
                                CommonUtil.makeToast(activity, "No More Data Available");
                            }
                            beatAdapter.notifyDataChanged();
                            beatAdapter.setMoreDataAvailable(false);
                        }
                    }
                }
            } else {
                makeToast(response.getMessage());
            }
        });

    }

    private void BeatListAPI(String offset) {

        mBeatsList.add(new Requests("load"));
        beatAdapter.notifyItemInserted(mBeatsList.size() - 1);

        if (!NetworkManager.isNetworkAvailable(activity)) {
            CommonUtil.makeToast(activity, activity.getResources().getString(R.string.please_check_your_network_connection));

        } else {

            NetworkManager.getInstance(activity).getBrandPromotion(activity, loanTypeId, response -> {
                if (response.isSuccess()) {
                    JSONObject jsonObject = response.getResponse();
                    if (jsonObject != null) {
                        JSONObject resultObj = jsonObject.optJSONObject(ApiConstants.Keys.RESULT);
                        if (resultObj != null) {
                            JSONArray categoryArray = resultObj.optJSONArray("category");
                            JSONArray requestsArray = resultObj.optJSONArray("requests");

                            beatCount = mBeatsList.size();

                            if (requestsArray != null && requestsArray.length() > 0) {
                                mBeatsList.remove(mBeatsList.size() - 1);

                                int size = requestsArray.length();
                                for (int i = 0; i < size; i++) {
                                    JSONObject applicationJson = requestsArray.optJSONObject(i);
                                    if (applicationJson != null) {
                                        Requests myrequest = new Requests(requestsArray.optJSONObject(i));
                                        mBeatsList.add(myrequest);
                                    }
                                }
                                if (beatCount == mBeatsList.size()) {
                                    beatAdapter.setMoreDataAvailable(false);
                                    CommonUtil.makeToast(activity, "No More Data Available");
                                }
                                beatAdapter.setMoreDataAvailable(false);

                                beatAdapter.notifyDataChanged();
                            }
                        }
                    }
                } else {
                    makeToast(response.getMessage());
                }
            });

        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        }
    }

}
