package app.nobroker;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import app.nobroker.utils.ConnectivityReceiver;

/**
 * Created by ashrafiqubal on 20/01/18.
 */

public class ApplicationClass extends Application {
    public static final String TAG = ApplicationClass.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private static Context context;

    private static ApplicationClass mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getParentContext(){
        return context;
    }

    public static synchronized ApplicationClass getInstance() {
        if (mInstance == null) {
            mInstance = new ApplicationClass();
        }
        return mInstance;
    }
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }

        return mRequestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }
    public <T> void addToRequestQueue(Request<T> req) {
//        req.setRetryPolicy(GeneralUtil.getDeaultRetryPolicy());
        req.setTag(TAG);
        getRequestQueue().add(req);
    }
    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
