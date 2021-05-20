package com.example.bustransportapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Schedule extends AppCompatActivity {

    private SearchView searchView;
    private Toolbar toolbar;
    private ViewPager viewPager;
    RelativeLayout relativeLayout;
    LinearLayout linearLayout;
    com.android.volley.RequestQueue queue;

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        searchView = (SearchView) findViewById(R.id.searchView);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        //relativeLayout = (RelativeLayout) findViewById(R.id.parent_layout);
        queue = Volley.newRequestQueue(this);
        ArrayList<JSONArray> bus_list=new ArrayList<JSONArray>();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                AndroidNetworking.post("https://bus-xapi.herokuapp.com/api/busno/")
                        .addBodyParameter("bus_no", query)
                        .setTag("Bus_no")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            String bus_no = query;
                            @Override
                            public void onResponse(JSONObject response) {
                                //Log.i("Size", response.toString());
                                JSONObject li;
                                try {
                                    li = response.getJSONObject("Accepted");

                                    JSONArray list = li.getJSONArray(bus_no);
                                    int len = list.length();
                                    System.out.println("Length is" + len);
                                    String s[] = new String[len];
                                    for(int i=0; i<len; i++)
                                    {
                                        JSONArray bus = list.getJSONArray(i);
                                        String station_name = bus.getString(0);
                                        s[i] = station_name;
                                        //Log.i("Bus station name", s[i]);
                                    }

                                    ArrayAdapter adapter = new ArrayAdapter<String>(Schedule.this, R.layout.activity_listview, s);

                                    ListView listView = (ListView)findViewById(R.id.parent_layout);
                                    listView.setAdapter(adapter);
                                    listView.setOnItemClickListener(Schedule.this::ClickBusStop);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                //finish();
                                // Redirect to login activity
                                Log.i("result", "successful");
                                Toast.makeText(Schedule.this, query, Toast.LENGTH_SHORT).show();

                            }
                            @Override
                            public void onError(ANError error) {
                                Toast.makeText(Schedule.this,"bus not found",Toast.LENGTH_SHORT).show();
                            }
                        });

                // Add the request to the RequestQueue.

                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (viewPager.getCurrentItem() == 0){
                    Log.i("Query", newText);
                }
                else if (viewPager.getCurrentItem() == 1) {
                    Log.i("Query", "abc");
                }
                return false;
            }
        });
    }

    private void ClickBusStop(AdapterView<?> adapterView, View view, int i, long l) {
        //adapterView.getSelectedItem().toString();
        ListView listView = (ListView)findViewById(R.id.parent_layout);
        Log.i("Clicked on bus stop", String.valueOf(adapterView.getItemAtPosition(i)));
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}