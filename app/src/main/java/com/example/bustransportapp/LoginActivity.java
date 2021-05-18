package com.example.bustransportapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public String isLoggedIn = "false";
    private TextView textViewSignup, textViewResetPassword;
    private EditText editTextEmail, editTextPassword;
    private Button buttonSignin;
    private ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.abc_fade_in,R.anim.abc_fade_out);
        setContentView(R.layout.activity_login);

        AndroidNetworking.initialize(getApplicationContext());


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        textViewSignup = (TextView) findViewById(R.id.textViewSignup);
        textViewResetPassword = (TextView) findViewById(R.id.textViewResetPassword);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonSignin = (Button) findViewById(R.id.buttonSignin);

        progressDialog = new ProgressDialog(this);

        // Set Listeners
        textViewSignup.setOnClickListener(this);
        textViewResetPassword.setOnClickListener(this);
        buttonSignin.setOnClickListener(this);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // If email is empty, return
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
            return;
        }

        // If email is empty, return
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Logging Please Wait...");
        progressDialog.show();

        AndroidNetworking.post("https://bestbus-api.herokuapp.com/api/login/")
                .addBodyParameter("email", email)
                .addBodyParameter("password", password)
                .setTag("User Logged in")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        finish();
                        // Redirect to login activity
                        String key = "1";
                        Intent i = new Intent(getBaseContext(), MainActivity.class);
                        sharedPreferences.edit().putBoolean(key, true).commit();
                        //i.putExtra(key, true);
                        startActivity(i);
                        //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(LoginActivity.this,"Authentication error....",Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void onClick(View view) {
        if (view == buttonSignin){
            userLogin();
        }
        else if (view == textViewSignup){
            finish();
            startActivity(new Intent(this, RegisterActivity.class ));
        }
    }
}