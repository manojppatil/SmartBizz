package com.smartbizz.Util;

public class Constants {
    public static final String EMPTY = "";
    public interface Environment{
        String STAGING = "staging";
        String PRODUCTION = "prod";
    }
    public interface RequestCode {
        int CHECK_ALL_PERMISSIONS = 0x001;
        int CHECK_SETTINGS = 0x0102;
        int WRITE_EXTERNAL_STORAGE = 0x0103;
        int REQUEST_CODE_PLAY_SERVICES_RESOLUTION = 0x0104;
        int GET_CURRENT_ADDRESS = 0x0105;
        int READ_EXTERNAL_STORAGE_PERMISSION = 0x106;
        int CAMERA = 0x107;
        int GALLERY = 0x108;
        int VI_KYC_SDK_REQ_CODE =7879;
    }

    public interface Extras {
        String URL = "url";
        String MOBILE_NUMBER = "mobileNumber";
        String ADDRESS = "address";
        String LOCATION = "location";
        String APPLICATIONS = "applications";
        String APP_STATUS = "appStatus";
        String BeatId = "beat_plan_id";
    }

    public interface PrefKeys {
        String AUTH_TOKEN = "authToken";
        String STUDENT_ID = "student_id";
        String AUTH_TOKEN_DATE = "authTokenDate";
//        String MOBILE_NUMBER = "mobileNumber";
        String MOBILE = "mobile";
        String EMAIL = "email";
        String Address = "address";
        String SENDERID = "sender_id";
        String API_KEY = "api_key";
        String BRANDNAME = "BrandName";
        String COMPANYTYPE = "company_type";
        String WHATSAPPLINK = "Whatsapplink";
        String PASSWORD = "password";
        String LOGGED_ID = "logged_id";
        String NAME = "name";
        String LOGO = "logo";
        String FIRST_NAME = "first_name";

    }

    public interface AppStage {
        String INTRO_COMPLETED = "intro_completed";
        String PERMISSIONS_COMPLETED = "permissions_completed";
        String MOBILE_VERIFIED = "mobile_verified";
    }

}
