package com.example.bustransportapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Presentation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    public static TextView textView;
    Bundle extras;
    public static SharedPreferences sharedPreferences;
    public static String key = "1";
    boolean isLoggedIn = false;
    /*LocationManager locationManager;
    LocationListener locationListener;

    public void goToNext(View view){
        Intent intent = new Intent(getApplicationContext(), GeofenceActivity.class);

        startActivity(intent);
    }

    public void goToNavbar(View view){
        Intent intent = new Intent(getApplicationContext(), NavigationDrawer.class);

        startActivity(intent);
    }
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        textView = (TextView) findViewById(R.id.logout);
        //Toast.makeText(this, textView.getText(), Toast.LENGTH_SHORT).show();
        extras = getIntent().getExtras();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Intent intent = getIntent();

        if (sharedPreferences!= null) {// to avoid the NullPointerException
            isLoggedIn = sharedPreferences.getBoolean(key, false);
        }
        System.out.println(isLoggedIn);
        if(isLoggedIn)
        {
            textView.setText("Logout");
            //sharedPreferences.edit().putBoolean(key, false).commit();
            Toast.makeText(this, "You are logged in", Toast.LENGTH_SHORT).show();
        }

        else{
            textView.setText("Login");
            Toast.makeText(this, "You are logged out", Toast.LENGTH_SHORT).show();
        }

        /*locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Toast.makeText(MainActivity.this, location.toString(), Toast.LENGTH_SHORT).show();
                Log.i("Location", location.toString());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        //if android version < 23 then dont ask the user and directly update the location
        if(Build.VERSION.SDK_INT < 23){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        else {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            else{
                Log.i("Permission", "Permission Granted");
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }*/

    }

    public void ClickMenu(View view)
    {   //open drawer
        openDrawer(drawerLayout);
    }

    public void openDrawer(DrawerLayout drawerLayout) {
        //open drawer layout
        drawerLayout.openDrawer(GravityCompat.START);

    }

    public void ClickLogo(View view)
    {
        closeDrawer(drawerLayout);
    }

    public void closeDrawer(DrawerLayout drawerLayout) {
        //close drawer layout
        //check condition
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            //drawer is open
            //close drawer
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickHome(View view){
        recreate();
    }
    public void ClickDashboard(View view){
        if(textView.getText() == "Login")
            redirectActivity(this, LoginActivity.class);
    }

    public void ClickSchedule(View view)
    {
        redirectActivity(this, Schedule.class);
    }

    public void ClickAboutUs(View view){
        redirectActivity(this, NavigationDrawer.class);
    }

    public void ClickLogout(View view){
        if(textView.getText() == "Login") {
            redirectActivity(this, LoginActivity.class);
            //sharedPreferences.edit().putBoolean(key, false).commit();
            //textView.setText("Logout");
        }
        else
            logout(this);
    }

    public static void logout(Activity activity) {
        //Initialize alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //set tittle
        builder.setTitle("Logout");

        builder.setMessage("Are you sure you want to logout");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finish();
                Intent i = new Intent(activity, MainActivity.class);
                sharedPreferences.edit().putBoolean(key, false).commit();
                //i.putExtra(key, false);
                activity.startActivity(i);
                //sharedPreferences.edit().putBoolean(key, false).commit();
                //System.exit(0);
            }
        });

        //Negative No button
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();

    }

    public static void redirectActivity(Activity activity, Class aClass) {
        //Initialize intent
        Intent intent = new Intent(activity, aClass);
        //set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();

        closeDrawer(drawerLayout);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}