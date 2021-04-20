package com.example.mobileshopping.ui.notifications;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.net.Uri;
import android.content.Intent;
import android.content.ActivityNotFoundException;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobileshopping.APIUrl;
import com.example.mobileshopping.R;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


public class NotificationsFragment extends Fragment {

    TextView txtEmail;
    TextView txtCode;
    ListView lvOrder;
    MaterialButton btnConfirm;
    HttpURLConnection con;
    JSONArray orders;
    String[] orderList;


    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        //final TextView textView = root.findViewById(R.id.text_notifications);
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });

        txtEmail = root.findViewById(R.id.txtEmail);
        txtCode = root.findViewById(R.id.txtCode);
        lvOrder = root.findViewById(R.id.lvOrder);
        btnConfirm = root.findViewById(R.id.btnConfirm);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString();
                String code = txtCode.getText().toString();
                System.out.println(email);

                URL url = null;
                InputStream inputStream = null;
                String result = "";

                try {
                    url = new URL(APIUrl.url+"/orderDetail.php");
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    Log.d("connectServer", "process start");

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    ExecutorService service = Executors.newSingleThreadExecutor();
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                con.connect();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    Future<?> f = service.submit(r);
                    f.get(1, TimeUnit.SECONDS);

                    inputStream = con.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        result += line;
                    }
                    Log.d("connectServer", "process complete");
                    inputStream.close();

                    JSONObject responseJSON = new JSONObject(result);
                    orders = responseJSON.getJSONArray("products");
                    for(int i = 0; i < orders.length(); i++){
                        JSONObject order = orders.getJSONObject(i);
                        String productName = order.optString("productName");
                        String price = order.optString("price");
                        String quantity = order.optString("quantity");
                        orderList[i] = "Product Name: " + productName + "\nPrice: " + price + "\nQuantity: " + quantity;
                    }
                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, orderList);
                    lvOrder.setAdapter(adapter);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        URL url = null;
        InputStream inputStream = null;
        String result = "";



        return root;
    }
}