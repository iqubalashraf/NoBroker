package app.nobroker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import app.nobroker.adapters.MainAdapter;
import app.nobroker.dataModels.Data;
import app.nobroker.dataModels.PayloadMainApi;
import app.nobroker.utils.ConnectivityReceiver;
import app.nobroker.utils.EndlessRecyclerOnScrollListener;

public class MainActivity extends AppCompatActivity
        implements ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener {
    private final String TAG = MainActivity.class.getSimpleName();
    List<Data> data = new ArrayList<>();
    private RecyclerView recycler_view;
    LinearLayoutManager linearLayoutManager;
    Activity activity;
    MainAdapter mainAdapter;
    View no_internet_layout;
    TextView button_try_again;
    TextView app_name, apply_filter;
    RelativeLayout layout_filter_button;
    View filter_layout;

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_try_again:
                if(GeneralUtil.checkConnection()){
                    fetchData("0");
                }else {
//                    GeneralUtil.showMessage(getString(R.string.text_no_internet_connection));
                    showNoInternetLayout();
                }
                break;
            case R.id.layout_filter_button:
                showFilterLayout();
                break;
            case R.id.apply_filter:

                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_main);
        initializeViews();
        initializeOnClickListener();
        ApplicationClass.getInstance().setConnectivityListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (GeneralUtil.checkConnection()) {
            if (data.size() == 0)
                fetchData("0");
        } else {
//            GeneralUtil.showMessage(getString(R.string.text_no_internet_connection));
            showNoInternetLayout();
        }

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if(isConnected){
            if (data.size() == 0)
                fetchData("0");
        }
    }

    private void initializeViews() {
        activity = this;
        recycler_view = findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(activity);
        recycler_view.setLayoutManager(linearLayoutManager);
        mainAdapter = new MainAdapter(activity);
        recycler_view.setAdapter(mainAdapter);
        no_internet_layout = findViewById(R.id.no_internet_layout);
        button_try_again = no_internet_layout.findViewById(R.id.button_try_again);
        app_name = findViewById(R.id.app_name);
        layout_filter_button = findViewById(R.id.layout_filter_button);
        layout_filter_button.setOnClickListener(this);
        filter_layout = findViewById(R.id.filter_layout);
        apply_filter = filter_layout.findViewById(R.id.apply_filter);
        apply_filter.setOnClickListener(this);
        recycler_view.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                Log.e(TAG, "Current page " + current_page);
                if (GeneralUtil.checkConnection()) {
                    fetchData(current_page + "");
                } else {
                    GeneralUtil.showMessage(getString(R.string.text_no_internet_connection));
                }

            }

        });
    }

    private void initializeOnClickListener(){
        button_try_again.setOnClickListener(this);
    }

    private void fetchData(String index) {
        Log.d(TAG, "Fetch data called");
        if(index.equals("0")){
            final ProgressDialog progressDialog = new ProgressDialog(activity);
            progressDialog.setTitle("Please wait...");
            progressDialog.setMessage("While we are fetching data. It may take time depending upon your internet speed");
            progressDialog.show();
            data.clear();
            String url = GeneralUtil.BASE_URL + index;
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        if (response != null) {
                            showMainLayout();
                            PayloadMainApi payloadMainApi = new Gson().fromJson(response, PayloadMainApi.class);
                            data = payloadMainApi.getData();
                            mainAdapter.setData(data);
                            mainAdapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }
                    } catch (Exception e) {

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            ApplicationClass.getInstance().addToRequestQueue(request);
        }else {
            String url = GeneralUtil.BASE_URL + index;
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        if (response != null) {
                            PayloadMainApi payloadMainApi = new Gson().fromJson(response, PayloadMainApi.class);
                            data.addAll(payloadMainApi.getData());
                            mainAdapter.setData(data);
                            mainAdapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            ApplicationClass.getInstance().addToRequestQueue(request);
        }

    }

    private void showMainLayout(){
        recycler_view.setVisibility(View.VISIBLE);
        no_internet_layout.setVisibility(View.GONE);
        filter_layout.setVisibility(View.GONE);
    }
    private void showNoInternetLayout(){
        recycler_view.setVisibility(View.GONE);
        no_internet_layout.setVisibility(View.VISIBLE);
        filter_layout.setVisibility(View.GONE);
    }
    private void showFilterLayout(){
        recycler_view.setVisibility(View.GONE);
        no_internet_layout.setVisibility(View.GONE);
        filter_layout.setVisibility(View.VISIBLE);
    }
}
