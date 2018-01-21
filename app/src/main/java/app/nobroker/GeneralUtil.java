package app.nobroker;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import android.app.AlertDialog;
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
    public static final String KEY_RK1 = "RK1", KEY_BHK1 = "BHK1", KEY_BHK2 = "BHK2", KEY_BHK3 = "BHK3", KEY_BHK4 = "BHK4", KEY_BHK4_MORE = "BHK4MORE";
    public static final String KEY_APARTMENT = "AP", KEY_IH_VILLA = "IH", KEY_IF = "IF";

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

    public static void showExitConformationDialog(final Activity activity) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle("Exit App?");
        dialog.setCancelable(true);
        dialog.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                // TODO Auto-generated method stub
                activity.finish();
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialog.show();
    }

    public static boolean isBothTrue(boolean value1, boolean value2){  // Returns true only when both value are true.
        if(value1)
            if (value2)
                return true;
        return false;
    }
    public static void updateStatusBarColor(Activity activity, String color) {// Color must be in hexadecimal fromat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color));
        }
    }
}
