package com.example.mobileshopping.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobileshopping.Product;
import com.example.mobileshopping.R;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class HomeFragment extends Fragment {

    HomeViewModel homeViewModel;
    Button btnSearch;
    EditText edSearch;
    ListView lvProducts;
    String[] productList;
    HttpURLConnection con;
    JSONObject jObj;
    JSONArray products;
    int numOfDisplayedProducts;
    int[] displayedProductId;
    Spinner spin;
    String[] productType = {"All Product", "Chassis", "CPU", "Display Card", "Internal Optical Drives", "Internal HDD", "SSD", "Motherboard", "Power Supply", "RAM", "RAID Card", "Sound Card"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });

        btnSearch = root.findViewById(R.id.btnSearch);
        edSearch = root.findViewById(R.id.edSearch);
        lvProducts = root.findViewById(R.id.lvProducts);
        spin = root.findViewById(R.id.spinner);
        ArrayAdapter<String> adap = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, productType);
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adap);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /*Toast.makeText(parent.getContext(),
                        "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
                        Toast.LENGTH_SHORT).show();*/
                SharedPreferences sharedPref = getActivity().getSharedPreferences("appData", Context.MODE_PRIVATE);
                String str = sharedPref.getString("jsonProductList", "null");
                if (str != "null") {
                    try {
                        productList = null;
                        jObj = new JSONObject(str);
                        products = jObj.getJSONArray("products");
                        numOfDisplayedProducts = getNumOfSuitableItem(products, "type", parent.getItemAtPosition(position).toString());
                        if (numOfDisplayedProducts != 0) {
                            productList = new String[numOfDisplayedProducts];
                            displayedProductId = new int[numOfDisplayedProducts];
                            numOfDisplayedProducts = 0;
                            for (int i = 0; i < products.length(); i++) {
                                JSONObject product = products.getJSONObject(i);
                                if (parent.getItemAtPosition(position).toString().equals("All Product")) {
                                    displayedProductId[numOfDisplayedProducts] = product.getInt("productID");
                                    productList[numOfDisplayedProducts++] = product.getString("productName") + "\nHK$" + product.getString("price");
                                } else {
                                    if (product.getString("type").equals(parent.getItemAtPosition(position).toString())) {
                                        displayedProductId[numOfDisplayedProducts] = product.getInt("productID");
                                        productList[numOfDisplayedProducts++] = product.getString("productName") + "\nHK$" + product.getString("price");
                                    }
                                }
                            }
                        }
                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                    }
                    if (productList != null) {
                        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, productList);
                        lvProducts.setAdapter(adapter);
                        lvProducts.setOnItemClickListener(onClickListView);
                    } else {
                        lvProducts.setAdapter(null);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        //get products
        InputStream inputStream = null;
        String result = "";
        URL url = null;
        try {
            //specific ip address
            url = new URL("http://192.168.1.11/webServer/COMP4342-Mobile-Computing/getProducts.php");
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            Log.d("connectServer", "process start");

            //not async
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
            f.get(2, TimeUnit.SECONDS); //if the connection cannot completes with in 2 seconds, a TimeroutException will be throw.

            inputStream = con.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
            Log.d("connectServer", "process complete");
            inputStream.close();

            //process JSON
            jObj = new JSONObject(result);
            products = jObj.getJSONArray("products");
            numOfDisplayedProducts = getNumOfSuitableItem(products, "promotion", "");
            if (numOfDisplayedProducts != 0) {
                productList = new String[numOfDisplayedProducts];
                displayedProductId = new int[numOfDisplayedProducts];
                numOfDisplayedProducts = 0;
                for (int i = 0; i < products.length(); i++) {
                    JSONObject product = products.getJSONObject(i);
                    if (product.getInt("promotion") == 1) {
                        displayedProductId[numOfDisplayedProducts] = product.getInt("productID");
                        productList[numOfDisplayedProducts++] = product.getString("productName") + "\nHK$" + product.getString("price");

                    }
                }
                //save product list
                //SharedPreferences sharedPref = getActivity().getSharedPreferences("appData", Context.MODE_PRIVATE);
                SharedPreferences.Editor prefEditor = getActivity().getSharedPreferences("appData", Context.MODE_PRIVATE).edit();
                prefEditor.putString("jsonProductList", result);
                prefEditor.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
            String msg = e.getMessage();
            System.out.println("error: " + msg);

            //get products from localstorage
            SharedPreferences sharedPref = getActivity().getSharedPreferences("appData", Context.MODE_PRIVATE);
            String str = sharedPref.getString("jsonProductList", "null");
            if (str != "null") {
                try {
                    jObj = new JSONObject(str);
                    products = jObj.getJSONArray("products");
                    numOfDisplayedProducts = getNumOfSuitableItem(products, "promotion", "");
                    if (numOfDisplayedProducts != 0) {
                        productList = new String[numOfDisplayedProducts];
                        displayedProductId = new int[numOfDisplayedProducts];
                        numOfDisplayedProducts = 0;
                        for (int i = 0; i < products.length(); i++) {
                            JSONObject product = products.getJSONObject(i);
                            if (product.getInt("promotion") == 1) {
                                displayedProductId[numOfDisplayedProducts] = product.getInt("productID");
                                productList[numOfDisplayedProducts++] = product.getString("productName") + "\nHK$" + product.getString("price");
                            }
                        }
                    }
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            }
        }
        if (productList != null) {
            ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, productList);
            lvProducts.setAdapter(adapter);
            lvProducts.setOnItemClickListener(onClickListView);
        }
        return root;
    }

    private AdapterView.OnItemClickListener onClickListView = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            System.out.println(displayedProductId[(int) id]);
        }
    };

    private int getNumOfSuitableItem(JSONArray products, String key, String type) throws JSONException {
        int num = 0;
        for (int i = 0; i < products.length(); i++) {
            JSONObject product = products.getJSONObject(i);
            switch (key) {
                case "promotion":
                    if (product.getInt(key) == 1)
                        num++;
                    break;
                case "type":
                    if (type.equals("All Product")) {
                        num++;
                    } else {
                        if (product.getString(key).equals(type))
                            num++;
                    }
                    break;
            }
        }
        return num;
    }

}

