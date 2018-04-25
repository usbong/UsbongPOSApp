package usbong.android.utils;

import java.util.Vector;

public class UsbongConstants {
    private UsbongConstants() {

    }
    
    public static final String USBONG_STORE_DB_SQL = "usbong_store.sql";
    
    //added by Mike, 20180209
    //HTTP constants
    public static final int REGISTRATION_TIMEOUT = 10000;
    public static final int WAIT_TIMEOUT = 30 * 1000;
    //public static final String HOSTNAME = "192.168.56.1";
    public static final String HOSTNAME = "store.usbong.ph";
    
    //update this on prod
    public static final String USBONG_STORE_PRODUCTS_LIST_DOWNLOAD = "https://" + HOSTNAME + "/app+server/getProductList.php";

    //JSON constants
    public static final String product_id = "product_id";
    public static final String merchant_id = "merchant_id";
    public static final String product_type_id = "product_type_id";
    public static final String name = "name";
    public static final String price = "price";
    public static final String previous_price = "previous_price";
    public static final String language = "language";
    public static final String author = "author";
    public static final String supplier = "supplier";
    public static final String description = "description";
    public static final String image_location = "image_location";
    public static final String format = "format";
    public static final String quantity_in_stock = "quantity_in_stock";
    public static final String translator = "translator";
    public static final String pages = "pages";
            
    //TREE types
	public static final String TREE_TYPE_SELL = "usbong_specialty_bookstore_sell";
	public static final String TREE_TYPE_REQUEST = "usbong_specialty_bookstore_request";
	public static final String TREE_TYPE_BUY = "buy";

	public static final String ITEMS_LIST = "items_list";
	public static final String ITEM_VARIABLE_NAME = "item_details";
	public static final String ITEM_IMAGE_NAME = "item_image";

	//added by Mike, 20180419
	public final static String ITEM_PRODUCT_OVERVIEW="item_product_overview";
	
	//added by Mike, 20170330
	public static final String ITEMS_LIST_DEFAULT = "items_list_books";
	public static final String ITEMS_LIST_BOOKS = "items_list_books";
	public static final String ITEMS_LIST_PROMOS = "items_list_promos";
	public static final String ITEMS_LIST_BEVERAGES = "items_list_beverages";
	public static final String ITEMS_LIST_COMICS = "items_list_comics";
	public static final String ITEMS_LIST_MANGA = "items_list_manga";
	public static final String ITEMS_LIST_TOYS_AND_COLLECTIBLES = "items_list_toys_and_collectibles";

	//added by Mike, 20170207
    public static final String MY_ACCOUNT_DETAILS= "MyAccountDetails";
    public static final String EMAIL_ADDRESS= "support@usbong.ph";//"order@retrocc.ph";	

    //edited by Mike, 20180206
    public static final String ORDER_EMAIL_ADDRESS= "order@usbong.ph"; //"usbong.ph@gmail.com";
    public static final String REQUEST_EMAIL_ADDRESS= "request@usbong.ph";
    public static final String SELL_EMAIL_ADDRESS= "sell@usbong.ph";
    public static final String HELP_EMAIL_ADDRESS= "help@usbong.ph";
    public static final String FEEDBACK_EMAIL_ADDRESS= "feedback@usbong.ph";
    
    //added by Mike, 20170225
	public final static int MEETUP=0;
	public final static int SHIPPING=1;
	public final static int defaultPreference=SHIPPING; //default
	
	public final static int CASH_UPON_MEETUP=0;
	public final static int BANK_DEPOSIT=1;
	public final static int PERA_PADALA=2;	
	public final static int defaultModeOfPayment=BANK_DEPOSIT;	//default
    
	//added by Mike, 20170327
	public final static int ISBN_10=0;
	public final static int ISBN_13=1;
	
	//added by Mike, 20170330
	public final static int BUY_ACTIVITY=0;
	public final static int SELL_ACTIVITY=1;
	public final static int REQUEST_ACTIVITY=2;
	public final static int ITEM_LIST_ACTIVITY=3; //UsbongDecisionTreeEngineActivity		
	
	//added by Mike, 20170520
	public final static int PRODUCT_TYPE_ALL=1;
	public final static int PRODUCT_TYPE_BOOKS=2;
	public final static int PRODUCT_TYPE_BEVERAGES=3;
	public final static int PRODUCT_TYPE_BOOKS_BEVERAGES=4;
	public final static int PRODUCT_TYPE_PROMOS=5;
	public final static int PRODUCT_TYPE_COMICS=6;
	public final static int PRODUCT_TYPE_MANGA=7;
	public final static int PRODUCT_TYPE_TOYS_AND_COLLECTIBLES=8;
	public final static int PRODUCT_TYPE_TEXTBOOKS=9;
	public final static int PRODUCT_TYPE_CHILDRENS=10;
	public final static int PRODUCT_TYPE_FOOD=11;
	public final static int PRODUCT_TYPE_MISCELLANEOUS=12;
	public final static int PRODUCT_TYPE_MEDICAL=13;
	
	//added by Mike, 20170725
	public final static String PRODUCT_TYPE_BOOKS_STRING ="books";
	public final static String PRODUCT_TYPE_BEVERAGES_STRING ="beverages";
//	public final static String PRODUCT_TYPE_BOOKS_BEVERAGES_STRING ="beverages";
	public final static String PRODUCT_TYPE_PROMOS_STRING ="promos";
	public final static String PRODUCT_TYPE_COMICS_STRING ="comics";
	public final static String PRODUCT_TYPE_MANGA_STRING ="manga";
	public final static String PRODUCT_TYPE_TOYS_AND_COLLECTIBLES_STRING ="toys_and_collectibles";	
	public final static String PRODUCT_TYPE_TEXTBOOKS_STRING ="textbooks";
	public final static String PRODUCT_TYPE_CHILDRENS_STRING ="childrens";
	public final static String PRODUCT_TYPE_FOOD_STRING = "food";
	public final static String PRODUCT_TYPE_MISCELLANEOUS_STRING ="miscellaneous";
	public final static String PRODUCT_TYPE_MEDICAL_STRING ="medical";
	
	//added by Mike, 20170525
	public final static int USBONG_MAIN_ACTIVITY=0;
	public final static int NOT_USBONG_MAIN_ACTIVITY=1;

	//added by Mike, 20170526
	public final static int MERCHANT_USBONG_SPECIALTY_BOOKSTORE=1;

	//added by Mike, 20170528
	public final static String MERCHANT_NAME="merchantName";
	
    //SCREEN types constants
	public static final int YES_NO_DECISION_SCREEN=0;	
	public static final int MULTIPLE_RADIO_BUTTONS_SCREEN=1;	
	public static final int MULTIPLE_CHECKBOXES_SCREEN=2;	
	public static final int AUDIO_RECORD_SCREEN=3;
	public static final int PHOTO_CAPTURE_SCREEN=4;	
	public static final int TEXTFIELD_SCREEN=5;	
	public static final int TEXTFIELD_WITH_UNIT_SCREEN=6;	
	public static final int TEXTFIELD_NUMERICAL_SCREEN=7;
	public static final int TEXTAREA_SCREEN=8;
	public static final int TEXT_DISPLAY_SCREEN=9;	
	public static final int IMAGE_DISPLAY_SCREEN=10;
	public static final int TEXT_IMAGE_DISPLAY_SCREEN=11;
	public static final int IMAGE_TEXT_DISPLAY_SCREEN=12;
	public static final int CLASSIFICATION_SCREEN=13;		
	public static final int DATE_SCREEN=14;	
	public static final int TIMESTAMP_DISPLAY_SCREEN=15;		
	public static final int GPS_LOCATION_SCREEN=16;		
	public static final int VIDEO_FROM_FILE_SCREEN=17;	
	public static final int VIDEO_FROM_FILE_WITH_TEXT_SCREEN=18;	
	public static final int LINK_SCREEN=19;			
	public static final int SEND_TO_WEBSERVER_SCREEN=20;		
	public static final int SEND_TO_CLOUD_BASED_SERVICE_SCREEN=21;	
	public static final int PAINT_SCREEN=22;
	public static final int QR_CODE_READER_SCREEN=23;
	public static final int CLICKABLE_IMAGE_DISPLAY_SCREEN=24;
	public static final int TEXT_CLICKABLE_IMAGE_DISPLAY_SCREEN=25;
	public static final int CLICKABLE_IMAGE_TEXT_DISPLAY_SCREEN=26;
	public static final int DCAT_SUMMARY_SCREEN=27;			
	public static final int MULTIPLE_RADIO_BUTTONS_WITH_ANSWER_SCREEN=28;	
	public static final int TEXTFIELD_WITH_ANSWER_SCREEN=29;	
	public static final int TEXTAREA_WITH_ANSWER_SCREEN=30;	
	public static final int SIMPLE_ENCRYPT_SCREEN=31;	
	public static final int YOUTUBE_VIDEO_SCREEN=32;	
	public static final int YOUTUBE_VIDEO_WITH_TEXT_SCREEN=33;	
	
	public static final int END_STATE_SCREEN=34;		

    //added by Mike, 20170801
	public static final String FROM_SENDING_EMAIL="fromSendingEmail";
    
    //JSON constants
	public static String MY_TREE = "MY_TREE";
	public static String CURR_USBONG_NODE = "CURR_USBONG_NODE";
	public static String NEXT_USBONG_NODE_IF_YES = "NEXT_USBONG_NODE_IF_YES";
	public static String NEXT_USBONG_NODE_IF_NO = "NEXT_USBONG_NODE_IF_NO";
	public static String USBONG_ANSWER_CONTAINER_COUNTER = "USBONG_ANSWER_CONTAINER_COUNTER";
	public static String USBONG_NODE_CONTAINER_COUNTER = "USBONG_NODE_CONTAINER_COUNTER";
	public static String DECISION_TRACKER_CONTAINER = "DECISION_TRACKER_CONTAINER";
	public static String DECISION_TRACKER_CONTAINER_SIZE = "DECISION_TRACKER_CONTAINER_SIZE";
	public static String USBONG_ANSWER_CONTAINER = "USBONG_ANSWER_CONTAINER";
	public static String USBONG_ANSWER_CONTAINER_SIZE = "USBONG_ANSWER_CONTAINER_SIZE";
	public static String USBONG_NODE_CONTAINER = "USBONG_NODE_CONTAINER";
	public static String USBONG_NODE_CONTAINER_SIZE = "USBONG_NODE_CONTAINER_SIZE";
/*
	//API KEY
    public static final String YOUTUBE_API_KEY = "AIzaSyCQ-Awkvj5nq5j5_9GqCKwxDzEsxjVfEIc";
*/    
    //BUNDLE KEY
    public static final String BUNDLE = "UDTEA_BUNDLE";
    public static final String JSON_KEY = "UDTEA_JSON_KEY";
    public static final String UTREE_KEY = "UTREE_KEY";
    
    public static final String FROM_MY_YOUTUBE_ACTIVITY_TO_MAIN_MENU = "FROM_MY_YOUTUBE_ACTIVITY_TO_MAIN_MENU";
    
    public static final String ALL_LOCAL_LANGUAGES_PRODUCT_ID = "usbong.specialty_bookstore.all_local_languages_001";
    public static final String ALL_FOREIGN_LANGUAGES_PRODUCT_ID = "usbong.specialty_bookstore.all_foreign_languages_001";
    public static final String DEFAULT_PRICE = "0.99USD (15PhP)"; //updated by Mike, 20161010
    public static final String MY_PURCHASED_ITEMS = "MyPurchasedItems"; //added by Mike, 20160427

    //SETTINGS 
    public static final int AUTO_NARRATE = 0;
    public static final int AUTO_PLAY = 1;
    public static final int AUTO_LOOP = 2;
    public static final String AUTO_NARRATE_STRING = " Auto-Narrate";
    public static final String AUTO_PLAY_STRING = " Auto-Play";    
    public static final String AUTO_LOOP_STRING = " Auto-Loop";    
    
    public static final String MY_SAVED_LANGUAGE_SETTING = "MySavedLanguageSetting";

    public static final String MY_UTTERANCE_ID = "myUsbongTTSId";
}
