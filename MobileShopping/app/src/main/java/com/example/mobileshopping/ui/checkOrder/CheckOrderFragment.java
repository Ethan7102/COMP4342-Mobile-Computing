package com.example.mobileshopping.ui.checkOrder;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    JSONArray orders;
    String[] orderDetailList;
    private RequestQueue queue;
    String url = APIUrl.url+"/orderDetail.php";
    private CheckOrderViewModel checkOrderViewModel;

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

                InputStream inputStream = null;
                String result = "";
                orderDetailList = new String[10];
                orderDetailList[0] = "Product Name\t\tQty\tPrice\n";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("response", response);
                        try {
                            JSONObject product = new JSONObject(response);
                            System.out.println(response);
                            /*
                            productName = product.getString("productName");
                            productDescription = product.getString("productDescription");
                            type = product.getString("type");
                            price = product.getInt("price");
                            quantity = product.getInt("quantity");
                            tv_name.setText(productName);
                            tv_price.setText("$" + price);
                            tv_type.setText(type);
                            tv_description.setText(productDescription);
                            if(quantity>0) {
                                addCart.setEnabled(true);
                                addCart.setText(R.string.add_to_cart);
                            } else {
                                addCart.setEnabled(false);
                                quantityOfCart=0;
                                tv_quantityOfCart.setText(String.valueOf(quantityOfCart));
                                addCart.setText(R.string.no_stone);
                            }*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, (VolleyError error) -> {
                    error.printStackTrace();
                    //tv_name.setText("That didn't work!");
                })
                {
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<>();
                        params.put("email",String.valueOf(email));
                        params.put("code",String.valueOf(code));
                        return params;
                    }
                };
                Log.i("test:", stringRequest.toString());
                queue.add(stringRequest);


                /*try {
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
                    //Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();

                    Log.d("connectServer", "process complete");
                    inputStream.close();

                    JSONObject responseJSON = new JSONObject(result);
                    orders = responseJSON.getJSONArray("products");
                    orderDetailList[0] = "Product Name\t\tQty\tPrice\n";
                    for(int i = 1; i < orders.length(); i++){
                        JSONObject order = orders.getJSONObject(i);
                        String productName = order.optString("productName");
                        String price = order.optString("price");
                        String quantity = order.optString("quantity");
                        orderDetailList[i] = "Product Name: " + productName + "\nPrice: " + price + "\nQuantity: " + quantity;
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                 */
                if (orderDetailList != null) {
                    ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, orderDetailList);
                    lvOrderDetail.setAdapter(adapter);
                }
            }
        });

        URL url = null;
        InputStream inputStream = null;
        String result = "";

        return root;
    }
}