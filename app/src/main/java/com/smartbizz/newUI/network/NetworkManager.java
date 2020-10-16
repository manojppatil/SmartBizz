package com.smartbizz.newUI.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.smartbizz.Util.Constants;
import com.smartbizz.Util.MyLogger;
import com.smartbizz.Util.PreferenceManager;
import com.smartbizz.newUI.pojo.DataPart;
import com.smartbizz.newUI.pojo.Login;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NetworkManager {

    private static final String TAG = NetworkManager.class.getSimpleName();

    private static NetworkManager sInstance;

    private RequestQueue mRequestQueue;

    private NetworkManager(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public static NetworkManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new NetworkManager(context);
        }
        return sInstance;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected() && activeNetworkInfo.isAvailable();
    }

//    public void login(Context context, Login login, final Response.Listener<NetworkResponse> responseListener) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("username", login.getUsername());
//        params.put("password", login.getPassword());
//        params.put("source", "mobile");
//        try {
//            params.put("version", (context.getPackageManager().getPackageInfo(context.getPackageName(), 0)).versionName);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        sendJsonRequest(context, Request.Method.POST, ApiConstants.Url.URL_LOGIN, new JSONObject(params), responseListener);
//    }

    public void logoff(Context context, String empid, final Response.Listener<NetworkResponse> responseListener) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", empid);
        sendJsonRequest(context, Request.Method.POST, ApiConstants.Url.URL_LOGOFF, new JSONObject(params), responseListener);
    }

    public void refreshToken(Context context, String userid, final Response.Listener<NetworkResponse> responseListener) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", userid);
        sendJsonRequest(context, Request.Method.POST, ApiConstants.Url.URL_REFRESH_TOKEN, new JSONObject(params), responseListener);
    }

    public void getBeatlist(Context context, JSONObject params, final Response.Listener<NetworkResponse> responseListener) {

        sendJsonRequest(context, Request.Method.POST, ApiConstants.Url.URL_BEAT_LIST, params, responseListener);
    }

    public void getActivityHistory(Context context, JSONObject params, final Response.Listener<NetworkResponse> responseListener) {

        sendJsonRequest(context, Request.Method.POST, ApiConstants.Url.URL_ACTIVITY_HISTORY, params, responseListener);
    }

    public void getOTP(Context context, Login login, final Response.Listener<NetworkResponse> responseListener) {

        Map<String, Object> params = new HashMap<>();
        params.put("mobile", login.getMobile());
        params.put("email", login.getEmail());
//        params.put("name", login.getName());
        sendFormRequest(context, Request.Method.POST, ApiConstants.Url.URL_GET_OTP, params, responseListener);
    }

    public void resendOTP(Context context, final Response.Listener<NetworkResponse> responseListener) {
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", PreferenceManager.getString(context, Constants.PrefKeys.MOBILE));
        params.put("email", PreferenceManager.getString(context, Constants.PrefKeys.EMAIL));
        sendFormRequest(context, Request.Method.POST, ApiConstants.Url.URL_RESEND_OTP, params, responseListener);
    }

    public void verifyOTP(Context context, String otp, final Response.Listener<NetworkResponse> responseListener) {
        Map<String, Object> params = new HashMap<>();
        params.put("first_name", PreferenceManager.getString(context, Constants.PrefKeys.FIRST_NAME));
        params.put("mobile", PreferenceManager.getString(context, Constants.PrefKeys.MOBILE));
        params.put("email", PreferenceManager.getString(context, Constants.PrefKeys.EMAIL));
        params.put("otp", otp);
        sendFormRequest(context, Request.Method.POST, ApiConstants.Url.URL_VERIFY_OTP, params, responseListener);
    }

    public void likeShareCount(Context context, String like, String share, String CategoryId, String imgId, final Response.Listener<NetworkResponse> responseListener) {
        Map<String, Object> params = new HashMap<>();
        params.put("like", like);
        params.put("user_id", PreferenceManager.getString(context, Constants.PrefKeys.LOGGED_ID));
        params.put("image_id", imgId);
        params.put("share", share);
        sendFormRequest(context, Request.Method.POST, ApiConstants.Url.URL_LIKE_SHARE_COUNT, params, responseListener);
    }

    public void getApplicationsList(Context context, final Response.Listener<NetworkResponse> responseListener) {
        Map<String, Object> params = new HashMap<>();
        sendJsonRequest(context, Request.Method.GET, ApiConstants.Url.GET_ALL_REQUEST, new JSONObject(params), responseListener);
    }

    public void putRequest(Context context, String category, String comments, final Response.Listener<NetworkResponse> responseListener) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", PreferenceManager.getString(context, Constants.PrefKeys.LOGGED_ID));
        params.put("gallery_category_id", category);
        params.put("comments", comments);
        sendFormRequest(context, Request.Method.POST, ApiConstants.Url.URL_PUT_REQUEST, params, responseListener);
    }

    public void getBrandPromotion(Context context, String categoryTypeId, final Response.Listener<NetworkResponse> responseListener) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", PreferenceManager.getString(context, Constants.PrefKeys.LOGGED_ID));
        params.put("gallery_category_id", categoryTypeId);
        sendFormRequest(context, Request.Method.POST, ApiConstants.Url.URL_GET_BRAND_PROMOTION, params, responseListener);
    }

    public void getLikeSharePromotion(Context context, String categoryTypeId, final Response.Listener<NetworkResponse> responseListener) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", PreferenceManager.getString(context, Constants.PrefKeys.LOGGED_ID));
        params.put("category_type", categoryTypeId);
        sendFormRequest(context, Request.Method.POST, ApiConstants.Url.URL_GET_LIKE_SHARE_PROMOTION, params, responseListener);
    }

    public void getProfile(Context context, final Response.Listener<NetworkResponse> responseListener) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", PreferenceManager.getString(context, Constants.PrefKeys.LOGGED_ID));
        sendFormRequest(context, Request.Method.POST, ApiConstants.Url.URL_GET_PROFILE, params, responseListener);
    }

    public void getCategory(Context context, final Response.Listener<NetworkResponse> responseListener) {
        Map<String, Object> params = new HashMap<>();
        sendFormRequest(context, Request.Method.POST, ApiConstants.Url.URL_GET_CATEGORY, params, responseListener);
    }

    public void putUserAction(Context context, String request_id, String isAccepted, final Response.Listener<NetworkResponse> responseListener) {
        Map<String, Object> params = new HashMap<>();
        String url = ApiConstants.Url.URL_PUT_USERACTION + "/" + request_id + "/" + isAccepted;
        sendJsonRequest(context, Request.Method.GET, url, new JSONObject(params), responseListener);
    }

    public void putUploadBankStmt(Context context, String BrandName, String firstName, String email, String address, String companyType, String filePath, final Response.Listener<NetworkResponse> responseListener) {
        Map<String, String> params = new HashMap<>();
        params.put(ApiConstants.Keys.LOGGED_ID, PreferenceManager.getString(context, Constants.PrefKeys.LOGGED_ID));
        params.put("id", PreferenceManager.getString(context, Constants.PrefKeys.LOGGED_ID));
        params.put("brand_name", BrandName);
        params.put("first_name", firstName);
        params.put("email", email);
        params.put("address", address);
        params.put("company_type", companyType);
        params.put("mobile", PreferenceManager.getString(context, Constants.PrefKeys.MOBILE));
        DataPart dataPart = new DataPart(filePath);
        sendMultipartRequest(context, Request.Method.POST, ApiConstants.Url.URL_EDIT_PROFILE, params, dataPart, responseListener);
    }

    public void getSenderId(Context context, final Response.Listener<NetworkResponse> responseListener) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", PreferenceManager.getString(context, Constants.PrefKeys.LOGGED_ID));
        sendFormRequest(context, Request.Method.POST, ApiConstants.Url.URL_GET_SENDER_ID, params, responseListener);
    }

    public void putSMSContactGrp(Context context, String Message, String groupid, final Response.Listener<NetworkResponse> responseListener) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", PreferenceManager.getString(context, Constants.PrefKeys.LOGGED_ID));
        params.put("message", Message);
        params.put("mobile", PreferenceManager.getString(context, Constants.PrefKeys.MOBILE));
        params.put("group_id", groupid);
        sendFormRequest(context, Request.Method.POST, ApiConstants.Url.URL_PUT_SMS_UPLOAD, params, responseListener);
    }

    public void putSMSContactList(Context context, String Message, String ContactList, final Response.Listener<NetworkResponse> responseListener) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", PreferenceManager.getString(context, Constants.PrefKeys.LOGGED_ID));
        params.put("message", Message);
        params.put("mobile", PreferenceManager.getString(context, Constants.PrefKeys.MOBILE));
        params.put("contact_list", ContactList);
        sendFormRequest(context, Request.Method.POST, ApiConstants.Url.URL_PUT_SMS_UPLOAD, params, responseListener);
    }

    public void createContactGrp(Context context, String GrpName, String ContactList, final Response.Listener<NetworkResponse> responseListener) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", PreferenceManager.getString(context, Constants.PrefKeys.LOGGED_ID));
        params.put("group_name", GrpName);
        params.put("phone_number", ContactList);
        sendFormRequest(context, Request.Method.POST, ApiConstants.Url.URL_CREATE_CONTACT_GRP, params, responseListener);
    }

    public void getContactGrp(Context context, final Response.Listener<NetworkResponse> responseListener) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", PreferenceManager.getString(context, Constants.PrefKeys.LOGGED_ID));
        sendFormRequest(context, Request.Method.POST, ApiConstants.Url.URL_GET_CONTACT_GRP, params, responseListener);
    }

    public void putSenderId(Context context, String applicationno, final Response.Listener<NetworkResponse> responseListener) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", PreferenceManager.getString(context, Constants.PrefKeys.LOGGED_ID));
        params.put("application_no", PreferenceManager.getString(context, Constants.PrefKeys.LOGGED_ID));
        params.put("sender_id", PreferenceManager.getString(context, Constants.PrefKeys.SENDERID));
        sendFormRequest(context, Request.Method.POST, ApiConstants.Url.URL_PUT_SENDER_ID, params, responseListener);
    }

    public void putSMSBulkUpload(Context context, String Message, String filePath, final Response.Listener<NetworkResponse> responseListener) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", PreferenceManager.getString(context, Constants.PrefKeys.LOGGED_ID));
        params.put("message", Message);
        params.put("mobile", PreferenceManager.getString(context, Constants.PrefKeys.MOBILE));
        DataPart dataPart = new DataPart(filePath);
        sendMultipartRequest(context, Request.Method.POST, ApiConstants.Url.URL_PUT_SMS_UPLOAD, params, dataPart, responseListener);
    }

    public void getMessageId(Context context, final Response.Listener<NetworkResponse> responseListener) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", PreferenceManager.getString(context, Constants.PrefKeys.LOGGED_ID));
        sendFormRequest(context, Request.Method.POST, ApiConstants.Url.URL_GET_MESSAGE_ID, params, responseListener);
    }

    public void getSMSServiceRequest(Context context, final Response.Listener<NetworkResponse> responseListener) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", PreferenceManager.getString(context, Constants.PrefKeys.LOGGED_ID));
        params.put("request_approve_status", "1");
        sendFormRequest(context, Request.Method.POST, ApiConstants.Url.URL_GET_REQUEST_ID, params, responseListener);
    }

    public void getBalanceSMS(Context context, final Response.Listener<NetworkResponse> responseListener) {
        Map<String, Object> params = new HashMap<>();
//        sendJsonRequest(context, Request.Method.GET, "http://sms.digimitra.in/V2/http-balance-api.php?apikey=PfN9QTxgRGwua1FP&format=json", new JSONObject(params), responseListener);
        sendJsonRequest(context, Request.Method.GET, ApiConstants.Url.URL_GET_SMS_BALANCE + "?apikey=" + PreferenceManager.getString(context, Constants.PrefKeys.API_KEY) + "&format=json", new JSONObject(params), responseListener);
    }

    public void getReport(Context context, final Response.Listener<NetworkResponse> responseListener) {
        Map<String, Object> params = new HashMap<>();
        //http://sms.digimitra.in/V2/http-dlr.php?apikey=PfN9QTxgRGwua1FP&msgid=64183530297343&format=json
//        sendJsonRequest(context, Request.Method.POST, "http://sms.digimitra.in/V2/http-dlr.php?apikey=PfN9QTxgRGwua1FP&msgid=64183530297343&format=json", new JSONObject(params), responseListener);
        sendJsonRequest(context, Request.Method.GET, ApiConstants.Url.URL_GET_SMS_REPORT + PreferenceManager.getString(context, Constants.PrefKeys.API_KEY) + "&msgid=" + "64183530297343" + "&format=json", new JSONObject(params), responseListener);
    }

    private void sendJsonRequest(@NonNull final Context context, int method, String url, final JSONObject params, final Response
            .Listener<NetworkResponse> responseListener) {
        //   if (isNetworkConnected(context)) {
        final NetworkRequest request = new NetworkRequest(context, method, url, params == null ? null : params.toString(),
                responseListener);
        request.setTag(url);
        mRequestQueue.add(request);
        /*} else if (context instanceof Activity) {
            //  DialogUtil.showInternetDialog((Activity) context);
        }*/
    }

    private void sendFormRequest(@NonNull final Context context, int method, String url, final Map params, final Response
            .Listener<NetworkResponse> responseListener) {
        final NetworkFormRequest request = new NetworkFormRequest(context, method, url, params == null ? null : params,
                responseListener);
        request.setTag(url);
        mRequestQueue.add(request);
    }

    private void sendMultipartRequest(@NonNull final Context context, int method, String url, Map<String, String> params, DataPart dataPart, final Response
            .Listener<NetworkResponse> responseListener) {
        final NetworkMultipartRequest request = new NetworkMultipartRequest(context, method, url, params, dataPart, responseListener);
        request.setTag(url);
        request.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(request);
    }

    public void getTemplatesCatgList(Context context, String categoryTypeId, final Response.Listener<NetworkResponse> responseListener) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", PreferenceManager.getString(context, Constants.PrefKeys.LOGGED_ID));
        params.put("id", categoryTypeId);
        sendJsonRequest(context, Request.Method.GET, ApiConstants.Url.URL_GET_TEMPLATE_CATEGORY, new JSONObject(params), responseListener);
    }

    public void add_catalog(Context context, String ItemName, String ItemPrice, String ItemDesc, String ItemLink, String ItemCode, String filePath, final Response.Listener<NetworkResponse> responseListener) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", PreferenceManager.getString(context, Constants.PrefKeys.LOGGED_ID));
        params.put("item_name", ItemName);
        params.put("price", ItemPrice);
        params.put("description", ItemDesc);
        params.put("link", ItemLink);
        params.put("item_code", ItemCode);
        params.put("image", filePath);
//        DataPart dataPart = new DataPart(filePath);
//        sendMultipartRequest(context, Request.Method.POST, ApiConstants.Url.URL_ADD_CATALOG,params, dataPart, responseListener);
        sendFormRequest(context, Request.Method.POST, ApiConstants.Url.URL_ADD_CATALOG, params, responseListener);
    }
}
