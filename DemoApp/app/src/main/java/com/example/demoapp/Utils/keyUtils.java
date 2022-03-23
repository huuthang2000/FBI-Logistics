package com.example.demoapp.Utils;

public class keyUtils {

    public static final int LIMIT_MESSAGE = 30;
    public static final int LIMIT_LOCATION = 20;
    public static final int MAX_FAMILY = 5;
    public static final int MIN_SPEED_AVERAGE = 10;

    public static final long TIME_TRIAL = 86400000L * 3L;

    public static final int LOWEST_SPEED_TO_WARN = 0;
    public static final int THE_FASTEST_SPEED_TO_WARN = 70;

    public static final int LIMIT_MESSAGE_NOTIFICATION = 1;

    public static final int REQUEST_CODE_PICK_IMAGE = 1255;
    public static final int REQUEST_CAMERA = 1585;
    public static final int REQUEST_PERMISSION = 1544;
    public static final int REQUEST_PERMISSION_CALL_PHONE = 1524;
    public static final int REQUEST_LOGIN = 1295;
    public static final int MAXBYTE = 2000000;
    public static final String DEFAULT_DATETIME_FORMAT = "ddMMyyyy";

//    public static final String LINK_DATABASE = "https://life24h-79106.firebaseio.com/";
//    public static final String LINK_STORAGE = "gs://life24h-1f235.appspot.com";
    public static final String REF_STORAGE_AVATAR = "avatar";
    public static final String REF_STORAGE_MESSAGE = "message";
    public static final String message = "message";
    public static final String membersListSeen = "membersListSeen";
    public static final String emergencyAssistanceList = "emergencyAssistanceList";
    public static final String membersListReceived = "membersListReceived";
    public static final String membersList = "membersList";
    public static final String inviteCode = "inviteCode";
    public static final String timeCreate = "timeCreate";
    public static final String bought = "bought";
    public static final String timeUpdate = "timeUpdate";
    public static final String commonName = "commonName";
    public static final String chatName = "chatName";
    public static final String name = "name";
    public static final String gender = "gender";
    public static final String phone = "phone";


    //Root node FireBase
    public static final String IDFamilyList = "IDFamilyList";
    public static final String familyList = "familyList";
    public static final String history = "historyList";
    public static final String drivingDetailHistory = "drivingDetailHistory";
    public static final String drivingDetails = "drivingDetails";
    public static final String inviteCodeList = "inviteCodeList";
    public static final String accountList = "accountList";
    public static final String chatList = "chatList";
    public static final String recentLocations = "recentLocations";
    public static final String messageList = "messageList";
    public static final String listReceived = "listReceived";
    public static final String listSeen = "listSeen";

    public static final String location = "location";
    public static final String GetGPSNow = "GetGPSNow";
    public static final String Safe_Area_Notification = "Safe_Area_Notification";
    public static final String areaList = "areaList";
    public static final String avatar = "avatar";
    public static final String TYPE_HELP = "help";
    public static final String TYPE_ABNORMAL_SPEED = "abnormal_speed";


    public static final String batteryPercent = "batteryPercent";

    public static final String EMAIL = "email";
    public static final String GOOGLE = "google";


    public static final String KEY_DATA_ACCOUNT = "dataAccount";
    public static final String KEY_USERNAME = "userName";
    public static final String KEY_OBJECT_ACCOUNT = "object_account";
    public static final String KEY_TOKEN = "keyToken";
    public static final String KEY_AD = "key_ad";
    public static final String KEY_SHOW_DIALOG = "key_show_dialog";
    public static final String isShow = "isShow";
    public static final String isSee = "isSee";
    public static final String NULL = "null";

    public static final String ONLINE = "online";
    public static final String TEXT = "text";
    public static final String PICTURE = "picture";
    public static final String MULTI_PICTURE = "multi_picture";
    public static final String OFFLINE = "offline";
    public static final String MALE = "male";
    public static final String FEMALE = "female";
    public static final String OTHER = "other";

    public static final String ResultRegister = "ResultRegister";
    public static final String ResultLogin = "ResultLogin";
    public static final String ResultLogout = "ResultLogout";
    public static final String ResultAutoLogin = "ResultAutoLogin";
    public static final String ResultSettingProfile = "ResultSettingProfile";
    public static final String ResultUploadAvatar = "ResultUploadAvatar";
    public static final String getMessageList = "getMessageList";
    public static final String getChatDetail = "getChatDetail";
    public static final String getAllListChatOfUid = "getAllListChatOfUid";
    public static final String editDetailMSG = "editDetailChat";
    public static final String removeMember = "removeMember";
    public static final String leaveGroup = "leaveGroup";
    public static final String postMessage = "postMessage";
    public static final String getAllImageFromMessage = "getAllImageFromMessage";
    public static final String getFamilyInformation = "getFamilyInformation";
    public static final String getAllAccountInformationInFamily = "getAllAccountInformationInFamily";
    public static final String getAllIDFamilyByUserID = "getAllIDFamilyByUserID";
    public static final String getSpeedOfUser = "getCurrentSpeedOfUser";
    public static final String totalMessage = "totalMessage";
    public static final String getAllSpeedOfUser = "getAllSpeedOfUser";
    public static final String forgetPassword = "forgetPassword";
    public static final String updatePassword = "updatePassword";
    public static final String checkTimeTrial = "checkTimeTrial";

    //KEY BUNDLE
    public static final String data = "data";
    public static final String dataPassword = "dataPassword";
    public static final String password = "password";
    public static final String activity = "activity";
    public static final String dataIDChat = "dataIDChat";
    public static final String dataURLImage = "dataURLImage";
    public static final String dataChat = "dataChat";
    public static final String dataEmergencyAssistance = "dataEmergencyAssistance";
    public static final String dataMessage = "dataMessage";
    public static final String dataType = "dataType";
    public static final String DEFAULT = "DEFAULT";
    public static final String NewGroup = "NewGroup";
    public static final String NewMessage = "NewMessage";
    public static final String Place_Item_Add = "Place_Item_Add";
    public static final String dataUid = "dataUid";


    //Notification message
    public static final String notificationId = "notificationId";
    public static final String CHANNEL_ID = "life24h_channel_01";
    public static final String TAG_NOTIFICATION = "NOTIFICATION_MESSAGE";
    public static final String KEY_TEXT_REPLY = "com.family.life24h.utils.notifications";
    public static final String KEY_TUTORIAL = "KEY_TUTORIAL";
    public static final String FLAG_TUTORIAL = "FLAG_TUTORIAL";


    //KEY STATUS
    public static final String UPLOADING = "uploading";
    public static final String SUCCESS = "SUCCESS";
    public static final String FAIL = "FAIL";


    //KEY SharedPreferences
    public static final String KEY_FAMILY_ID = "currentFamilyID";
    public static final String ID = "ID";
    public static final String topSpeed = "topSpeed";
    public static final String SAFE_AREA = "safeArea";
    public static final String LATITUDE = "latitude";
    public static final String LONGTITUDE = "longitude";
    public static final String LATITUDEOLD = "latitude_old";
    // use for class GPSService
    public static final String IDFAMILY = "id_family";
    public static final String LONGTITUDEOLD = "longitude_old";
    public static final String TIMELOCATION = "time_location";
    public static final String LATITUDEOLDACCOUNT = "latitude_old_account";
    public static final String LONGTITUDEOLDACCOUNT = "latitude_old_account";
    public static final String NAME_AREA = "name_area";
    public static final String RADIUS_AREA = "radius_area";
    // MAP_TYPE_TERRAIN = 3; MAP_TYPE_NORMAL = 1; MAP_TYPE_SATELLITE = 2;
    public static final String MAPTYPE = "maptype";
    public static final String GPS = "gps";
    public static final String STATUS = "status";

    // type use for listen internet online or offline
    public static final String NETWORK = "network";
    public static final String TYPE = "type";
    //Folder save avatar
    public static final String avatarLife24h = "avatarLife24h";

    //Type message
    public static final String GROUP = "group";
    public static final String PRIVATE = "private";


    public static final String BROADCAST_DETECTED_ACTIVITY = "activity_intent";

    public static final long DETECTION_INTERVAL_IN_MILLISECONDS = 10 * 1000;

    public static final int CONFIDENCE = 70;
    public static final String CHECK_WELCOME = "check_welcome";

    public static final String RESTART_INTENT = "com.family.life24h.restarter";


}
