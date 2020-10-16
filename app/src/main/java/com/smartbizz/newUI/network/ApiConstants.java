package com.smartbizz.newUI.network;

public class ApiConstants {

    public static final String BASE_URL = "https://digiepost.in";

    public interface Url {
        String URL_CHECK_VERSION = BASE_URL + "/login";
        String URL_LOGIN = BASE_URL + "/login";
        String URL_GET_OTP = BASE_URL + "/pure/getOtp";
        String URL_RESEND_OTP = BASE_URL + "/pure/resendOtp";
        String URL_VERIFY_OTP = BASE_URL + "/pure/verifyOtp";
        String URL_REFRESH_TOKEN = BASE_URL + "/refresh-token";
        String URL_LOGOFF = BASE_URL + "/logout";
        String URL_BEAT_LIST = BASE_URL + "/beatplan/list";
        String URL_ACTIVITY_HISTORY = BASE_URL + "/meeting/activitylist";
        String GET_ALL_REQUEST = BASE_URL + "/pure/get_all_requests/1";
        String URL_PUT_REQUEST = BASE_URL + "/pure/create_request";
        String URL_GET_BRAND_PROMOTION = BASE_URL + "/pure/get_brand_promotion";
        String URL_GET_LIKE_SHARE_PROMOTION = BASE_URL + "/pure/get_like_share_promotion";
        String URL_PUT_USERACTION = BASE_URL + "/pure/user_action";
        String URL_LIKE_SHARE_COUNT = BASE_URL + "/pure/put_like_Share_count";
        String URL_EDIT_PROFILE = BASE_URL + "/pure/upload_user_logo";
        String URL_GET_PROFILE = BASE_URL + "/pure/get_user_data";
        String URL_GET_CATEGORY = BASE_URL + "/pure/get_all_category";
        String URL_GET_SENDER_ID = BASE_URL + "/pure/get_sender_id";
        String URL_PUT_SMS_UPLOAD = BASE_URL + "/pure/send_bulk_sms";
        String URL_CREATE_CONTACT_GRP = BASE_URL + "/pure/create_group";
        String URL_GET_CONTACT_GRP = BASE_URL + "/pure/get_group_dropdown";
        String URL_GET_MESSAGE_ID = BASE_URL + "/pure/get_msg_id";
        String URL_GET_SMS_BALANCE = "http://sms.digimitra.in/V2/http-balance-api.php";
        String URL_GET_SMS_REPORT = "http://sms.digimitra.in/V2/http-dlr.php?apikey=";
        String URL_PUT_SENDER_ID = BASE_URL +  "/pure/put_sender_id";
        String URL_GET_REQUEST_ID = BASE_URL +  "/pure/sms_service_request";
        String URL_GET_TEMPLATE_CATEGORY = BASE_URL +  "/pure/get_sms_category";
        String URL_ADD_CATALOG = BASE_URL +  "/pure/add_catlog";

    }

    public interface Header {
        String AUTHORIZATION = "Authorization";
        String CONTENT_TYPE = "Content-Type";
        String ACCEPT = "Accept";
        String CLIENT_TIME = "Client-Time";
        String API_KEY = "apikey";
        String CONNECTION = "Connection";
        String APPLICATION_JSON = "application/json";
    }

    public interface StatusCode {
        boolean CODE_1 = true;
        boolean CODE_2 = false;
    }

    public interface Keys {
        String APPLICANT_ID = "applicant_id";
        String LEAD_ID = "lead_id";
        String AUTH_TOKEN = "token";
        String STUDENT_ID = "student_id";
        String RESULT = "result";
        String LOGGED_ID = "logged_id";
        String NAME = "name";
        String FIRST_NAME = "first_name";
        String EMAIL = "email";
        String MOBILE = "mobile";
        String MOBILE_NO = "mobile_no";
        String LAST_LOGIN = "last_login";
    }

    public interface ApiStatus {
    }

    public interface RootKeys {
        String STATUS = "status";
        String MESSAGE = "message";
        String IS_EDITABLE_FLAG = "is_editable_flag";
    }
}
