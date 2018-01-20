package app.nobroker;

import android.content.res.Resources;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Locale;

import app.nobroker.utils.ConnectivityReceiver;

/**
 * Created by ashrafiqubal on 20/01/18.
 */

public class GeneralUtil {
    public static final String BASE_URL = "http://www.nobroker.in/api/v1/property/filter/region/ChIJLfyY2E4UrjsRVq4AjI7zgRY/?lat_lng=12.9279232,77.6271078&rent=0,500000&travelTime=30&pageNo=";
    public static final String IMAGE_BASE_URL = "http://d3snwcirvb4r88.cloudfront.net/images/";
    public static final String KEY_SEMI_FURNISHED = "SEMI_FURNISHED", KEY_FULLY_FURNISHED = "FULLY_FURNISHED", KEY_NOT_FURNISHED = "NOT_FURNISHED";
    public static final String KEY_FAMILY = "FAMILY", KEY_ANYONE = "ANYONE", KEY_BACHELOR = "BACHELOR";

    public static boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        return isConnected;
    }

    public static float dpFromPx(final float px) {
        return px / ApplicationClass.getParentContext().getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final float dp) {
        return dp * ApplicationClass.getParentContext().getResources().getDisplayMetrics().density;
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static void showMessage(String message) {
        Toast.makeText(ApplicationClass.getParentContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static String getPriceToDisplay(int price) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        String string = formatter.format(price);
        return string.substring(0, string.length() - 3);
    }

    public static String getFurnishingTextToDisplay(String KEY) {
        if (KEY != null){
            if (KEY.equals(KEY_FULLY_FURNISHED)) {
                return "Fully Furnished";
            } else if (KEY.equals(KEY_SEMI_FURNISHED)) {
                return "Semi Furnished";
            } else if (KEY.equals(KEY_NOT_FURNISHED)) {
                return "Not Furnished";
            } else {
                return "";
            }
        }else {
            return "";
        }

    }

    public static String getLeaseTypeToDisplay(String KEY) {
        if (KEY != null){
            if (KEY.equals(KEY_FAMILY)) {
                return "Family";
            } else if (KEY.equals(KEY_ANYONE)) {
                return "Anyone";
            } else if (KEY.equals(KEY_BACHELOR)) {
                return "Bachelor";
            } else {
                return "";
            }
        }else {
            return "";
        }
    }
}
