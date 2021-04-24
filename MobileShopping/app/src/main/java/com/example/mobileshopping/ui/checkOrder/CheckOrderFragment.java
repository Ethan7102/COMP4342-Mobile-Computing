package com.example.mobileshopping.ui.checkOrder;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mobileshopping.APIUrl;
import com.example.mobileshopping.R;
import com.example.mobileshopping.VolleySingleton;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class CheckOrderFragment extends Fragment {

    TextView txtEmail;
    TextView txtCode;
    ListView lvOrderDetail;
    MaterialButton btnConfirm;
    HttpURLConnection con;
    JSONArray orderDetail;
    String[] orderDetailList;
    private RequestQueue queue;
    String url = APIUrl.url+"/orderDetail.php";
    private CheckOrderViewModel checkOrderViewModel;
    double amount;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        checkOrderViewModel =
                new ViewModelProvider(this).get(CheckOrderViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        //final TextView textView = root.findViewById(R.id.text_notifications);
        checkOrderViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        queue = VolleySingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue();
        txtEmail = root.findViewById(R.id.txtEmail);
        txtCode = root.findViewById(R.id.txtCode);
        lvOrderDetail = root.findViewById(R.id.lvOrder);
        btnConfirm = root.findViewById(R.id.btnConfirm);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString();
                String code = txtCode.getText().toString();
                System.out.println(email+"  "+code);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("response", response);
                        amount=0;
                        try {
                            System.out.println(response);
                            JSONObject responseJSON = new JSONObject(response);
                            orderDetail = responseJSON.getJSONArray("product");
                            orderDetailList = new String[orderDetail.length()+3];
                            String productName="Product Name";
                            String qty="Qty";
                            String price = "Price";
                            String str = String.format("%-50s %8s %5s", productName,price, qty);
                            orderDetailList[0] = str;
                            System.out.println(orderDetail.length());
                            for(int i = 0; i < orderDetail.length(); i++){
                                JSONObject product = orderDetail.getJSONObject(i);
                                productName = product.optString("productName");
                                System.out.println("productName: "+productName);
                                price = "$" + product.optString("price");
                                amount+=product.optDouble("price")*product.optInt("quantity");
                                qty = product.optString("quantity");
                                str = String.format("%-50s %8s %5s", productName,price,qty);
                                orderDetailList[i+1] = str;
                            }
                            str = "Total amount: $"+amount;
                            orderDetailList[orderDetail.length()+1] = str;
                            str = "Order status:";
                            orderDetailList[orderDetail.length()+2] = str;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (orderDetailList != null) {
                            ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, orderDetailList);
                            lvOrderDetail.setAdapter(adapter);
                        }
                        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(txtCode.getWindowToken(), 0);
                    }
                }, (VolleyError error) -> {
                    error.printStackTrace();
                    //tv_name.setText("That didn't work!");
                })
                {
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<>();
                        params.put("email",email);
                        params.put("code",code);
                        return params;
                    }
                };
                Log.i("test:", stringRequest.toString());
                queue.add(stringRequest);


            }
        });

        URL url = null;
        InputStream inputStream = null;
        String result = "";

        return root;
    }
}