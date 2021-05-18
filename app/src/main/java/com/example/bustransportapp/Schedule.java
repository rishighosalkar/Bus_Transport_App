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
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
        relativeLayout = (RelativeLayout) findViewById(R.id.parent_layout);
        queue = Volley.newRequestQueue(this);
        ArrayList<JSONArray> bus_list=new ArrayList<JSONArray>();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                /*String url ="https://bus-xapi.herokuapp.com/api/busno/";
                Toast.makeText(Schedule.this, query,Toast.LENGTH_SHORT).show();
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                // Display the first 500 characters of the response string.
                                Log.i("Result", "successful");
                                Toast.makeText(Schedule.this,"Bus found",Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Schedule.this,"Bus not found",Toast.LENGTH_SHORT).show();
                    }
                });*/
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
                                    String s = list.getString(0);

                                    Toast.makeText(Schedule.this, s, Toast.LENGTH_SHORT).show();
                                    for(int i=1;i<=len;i++)
                                    {
                                        Log.i("Counter", "len");
                                        JSONArray bus = list.getJSONArray(i);
                                        String station_name = bus.getString(0);
                                        CardView cardView = new CardView(Schedule.this);
                                        TextView textView = new TextView(Schedule.this);
                                        cardView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                                        textView.setLayoutParams(new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                                        textView.setGravity(Gravity.CENTER_HORIZONTAL);
                                        textView.setText(station_name);
                                        //cardView.addView(textView);
                                        relativeLayout.addView(textView);
                                        Log.i("Bus stop Names", station_name);
                                    }
                                    //JSONArray station = list.getJSONArray(1).getJSONArray(0);

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
}