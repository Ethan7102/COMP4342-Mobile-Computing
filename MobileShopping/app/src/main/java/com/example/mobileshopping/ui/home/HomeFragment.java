package com.example.mobileshopping.ui.home;

import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mobileshopping.R;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    Button btnSearch;
    EditText edSearch;
    ListView lvProducts;
    String[] productList;

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

        //get products
        InputStream inputStream = null;
        String result = "";
        URL url = null;

        try {
            //specific ip address
            url = new URL("http://192.168.1.11/webServer/COMP4342-Mobile-Computing/getProducts.php");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            Log.d("doInBackground", "process start");

            //not async
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            con.connect();

            inputStream = con.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
            Log.d("doInBackground", "process complete");
            inputStream.close();

            //process JSON
            JSONObject jObj = new JSONObject(result);
            JSONArray products = jObj.getJSONArray("products");
            productList=new String[products.length()];
                for (int i = 0; i < products.length(); i++) {
                    JSONObject product = products.getJSONObject(i);
                    productList[i] = product.getString("brand") + "\n" + product.getString("productName") + "\nHK$" + product.getString("price");
                }
            ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,productList);
            lvProducts.setAdapter(adapter);
            lvProducts.setOnItemClickListener(onClickListView);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = e.getMessage();
            System.out.println("error: "+msg);
        }
        return root;
    }

    private AdapterView.OnItemClickListener onClickListView = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    };
}

