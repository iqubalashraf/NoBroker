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
    LinearLayoutManager linearLayoutManager;
    Activity activity;
    MainAdapter mainAdapter;
    View no_internet_layout;
    TextView button_try_again;
    TextView app_name, apply_filter;
    RelativeLayout layout_filter_button;
    View filter_layout;
    TextView rk_1, bhk_1, bhk_2, bhk_3, bhk_4, bhk_4_more;             // No of bedrooms buttons
    TextView apartment, ih_villa, if_bf;                               // Property type buttons
    TextView fully_furnished, semi_furnished, not_furnished;           // Furnishing type buttons
    private RecyclerView recycler_view;
    private boolean isFilterLayoutVisiable = false;
    EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;

    int stopCall = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GeneralUtil.updateStatusBarColor(this, getString(R.string.brand_color_dark_string));
        initializeViews();
        initializeOnClickListener();
        ApplicationClass.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onBackPressed() {
        if (isFilterLayoutVisiable) {
            showMainLayout();
        } else {
            GeneralUtil.showExitConformationDialog(activity);
        }
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
        if (isConnected) {
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
        filter_layout = findViewById(R.id.filter_layout);
        apply_filter = filter_layout.findViewById(R.id.apply_filter);
        rk_1 = filter_layout.findViewById(R.id.rk_1);
        bhk_1 = filter_layout.findViewById(R.id.bhk_1);
        bhk_2 = filter_layout.findViewById(R.id.bhk_2);
        bhk_3 = filter_layout.findViewById(R.id.bhk_3);
        bhk_4 = filter_layout.findViewById(R.id.bhk_4);
        bhk_4_more = filter_layout.findViewById(R.id.bhk_4_more);
        apartment = filter_layout.findViewById(R.id.apartment);
        ih_villa = filter_layout.findViewById(R.id.ih_villa);
        if_bf = filter_layout.findViewById(R.id.if_bf);
        fully_furnished = filter_layout.findViewById(R.id.fully_furnished);
        semi_furnished = filter_layout.findViewById(R.id.semi_furnished);
        not_furnished = filter_layout.findViewById(R.id.not_furnished);
    }

    private void initializeOnClickListener() {
        button_try_again.setOnClickListener(this);
        apply_filter.setOnClickListener(this);
        rk_1.setOnClickListener(this);
        bhk_1.setOnClickListener(this);
        bhk_2.setOnClickListener(this);
        bhk_3.setOnClickListener(this);
        bhk_4.setOnClickListener(this);
        bhk_4_more.setOnClickListener(this);
        apartment.setOnClickListener(this);
        ih_villa.setOnClickListener(this);
        if_bf.setOnClickListener(this);
        fully_furnished.setOnClickListener(this);
        semi_furnished.setOnClickListener(this);
        not_furnished.setOnClickListener(this);
        layout_filter_button.setOnClickListener(this);
        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                Log.e(TAG, "1) Current page " + current_page);
                if (GeneralUtil.checkConnection()) {
                    fetchData(current_page + "");
                } else {
                    GeneralUtil.showMessage(getString(R.string.text_no_internet_connection));
                }
            }
        };
        recycler_view.addOnScrollListener(endlessRecyclerOnScrollListener);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_try_again:
                if (GeneralUtil.checkConnection()) {
                    fetchData("0");
                } else {
                    showNoInternetLayout();
                }
                break;
            case R.id.layout_filter_button:
                showFilterLayout();
                break;
            case R.id.apply_filter:
                if(isFilterApplied()){
                    endlessRecyclerOnScrollListener.setLoading(false);
                    endlessRecyclerOnScrollListener.setTotalItemCount(getFilteredData(data).size());
                    endlessRecyclerOnScrollListener.setPreviousTotal(getFilteredData(data).size());
                }
                updateRecyclerView(data);
                showMainLayout();
                break;
            case R.id.rk_1:
                toggleView(rk_1);
                break;
            case R.id.bhk_1:
                toggleView(bhk_1);
                break;
            case R.id.bhk_2:
                toggleView(bhk_2);
                break;
            case R.id.bhk_3:
                toggleView(bhk_3);
                break;
            case R.id.bhk_4:
                toggleView(bhk_4);
                break;
            case R.id.bhk_4_more:
                toggleView(bhk_4_more);
                break;
            case R.id.apartment:
                toggleView(apartment);
                break;
            case R.id.ih_villa:
                toggleView(ih_villa);
                break;
            case R.id.if_bf:
                toggleView(if_bf);
                break;
            case R.id.fully_furnished:
                toggleView(fully_furnished);
                break;
            case R.id.semi_furnished:
                toggleView(semi_furnished);
                break;
            case R.id.not_furnished:
                toggleView(not_furnished);
                break;

        }
    }

    private void fetchData(String index) {
        Log.d(TAG, "Fetch data called index : " + index);
        if (index.equals("0")) {
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
                            updateRecyclerView(data);
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
        } else {
            String url = GeneralUtil.BASE_URL + index;
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        if (response != null) {
                            PayloadMainApi payloadMainApi = new Gson().fromJson(response, PayloadMainApi.class);
                            data.addAll(payloadMainApi.getData());
                            if(isFilterApplied()){
                                if(stopCall<5){
                                    if (getFilteredData(payloadMainApi.getData()).size() ==0){
                                        int temp =endlessRecyclerOnScrollListener.getCurrent_page() + 1;
                                        fetchData(temp+"");
                                        endlessRecyclerOnScrollListener.setCurrent_page(temp);
                                        stopCall++;
                                    }else {
                                        updateRecyclerView(data);
                                        stopCall = 0;
                                    }
                                }else {
                                    GeneralUtil.showMessage("No more houses");
                                }

                            }else {
                                updateRecyclerView(data);
                                stopCall = 0;
                            }
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

    private void updateRecyclerView(List<Data> data){
        List<Data> temp = new ArrayList<>();
        mainAdapter.setData(temp);
        mainAdapter.notifyDataSetChanged();
        if(isFilterApplied()){
            mainAdapter.setData(getFilteredData(data));
        }else {
            mainAdapter.setData(data);
        }
        mainAdapter.notifyDataSetChanged();
    }

    private void showMainLayout() {
        isFilterLayoutVisiable = false;
        recycler_view.setVisibility(View.VISIBLE);
        no_internet_layout.setVisibility(View.GONE);
        filter_layout.setVisibility(View.GONE);
    }

    private void showNoInternetLayout() {
        isFilterLayoutVisiable = false;
        recycler_view.setVisibility(View.GONE);
        no_internet_layout.setVisibility(View.VISIBLE);
        filter_layout.setVisibility(View.GONE);
    }

    private void showFilterLayout() {
        isFilterLayoutVisiable = true;
        recycler_view.setVisibility(View.GONE);
        no_internet_layout.setVisibility(View.GONE);
        filter_layout.setVisibility(View.VISIBLE);
    }

    private void toggleView(TextView textView) {
        if (textView.isSelected()) {
            textView.setSelected(false);
            textView.setBackgroundColor(getResources().getColor(R.color.background_color));
            textView.setTextColor(getResources().getColor(R.color.black));
        } else {
            textView.setSelected(true);
            textView.setBackgroundColor(getResources().getColor(R.color.brand_color));
            textView.setTextColor(getResources().getColor(R.color.white));
        }
    }

    private List<Data> getFilteredData(List<Data> data1) {
        List<Data> data = new ArrayList<>();
        data.addAll(data1);
        if (rk_1.isSelected() || bhk_1.isSelected() || bhk_2.isSelected() || bhk_3.isSelected() || bhk_4.isSelected() || bhk_4_more.isSelected()) {
            for (int i = 0; i < data.size(); i++) {
                if (!(GeneralUtil.isBothTrue(rk_1.isSelected(), data.get(i).getType().equals(GeneralUtil.KEY_RK1)) ||
                        GeneralUtil.isBothTrue(bhk_1.isSelected(), data.get(i).getType().equals(GeneralUtil.KEY_BHK1)) ||
                        GeneralUtil.isBothTrue(bhk_2.isSelected(), data.get(i).getType().equals(GeneralUtil.KEY_BHK2)) ||
                        GeneralUtil.isBothTrue(bhk_3.isSelected(), data.get(i).getType().equals(GeneralUtil.KEY_BHK3)) ||
                        GeneralUtil.isBothTrue(bhk_4.isSelected(), data.get(i).getType().equals(GeneralUtil.KEY_BHK4)) ||
                        GeneralUtil.isBothTrue(bhk_4_more.isSelected(), data.get(i).getType().equals(GeneralUtil.KEY_BHK4_MORE)))) {
                    data.remove(i);
                    i--;
                }
            }
        }

        if (apartment.isSelected() || ih_villa.isSelected() || if_bf.isSelected()) {
            for (int i = 0; i < data.size(); i++) {
                if (!(GeneralUtil.isBothTrue(apartment.isSelected(), data.get(i).getBuildingType().equals(GeneralUtil.KEY_APARTMENT)) ||
                        GeneralUtil.isBothTrue(ih_villa.isSelected(), data.get(i).getBuildingType().equals(GeneralUtil.KEY_IH_VILLA)) ||
                        GeneralUtil.isBothTrue(if_bf.isSelected(), data.get(i).getBuildingType().equals(GeneralUtil.KEY_IF)))) {
                    data.remove(i);
                    i--;
                }
            }
        }

        if (fully_furnished.isSelected() || semi_furnished.isSelected() || not_furnished.isSelected()) {
            for (int i = 0; i < data.size(); i++) {
                if (!(GeneralUtil.isBothTrue(fully_furnished.isSelected(), data.get(i).getFurnishing().equals(GeneralUtil.KEY_FULLY_FURNISHED)) ||
                        GeneralUtil.isBothTrue(semi_furnished.isSelected(), data.get(i).getFurnishing().equals(GeneralUtil.KEY_SEMI_FURNISHED)) ||
                        GeneralUtil.isBothTrue(not_furnished.isSelected(), data.get(i).getFurnishing().equals(GeneralUtil.KEY_NOT_FURNISHED)))) {
                    data.remove(i);
                    i--;
                }
            }
        }
        return data;
    }

    private boolean isFilterApplied(){
        return rk_1.isSelected() || bhk_1.isSelected() || bhk_2.isSelected() || bhk_3.isSelected() || bhk_4.isSelected() || bhk_4_more.isSelected() ||
                apartment.isSelected() || ih_villa.isSelected() || if_bf.isSelected()||
                fully_furnished.isSelected() || semi_furnished.isSelected() || not_furnished.isSelected();
    }

}
