package com.google.android.gms.samples.vision.barcodereader.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.samples.vision.barcodereader.ApiRoutes;
import com.google.android.gms.samples.vision.barcodereader.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class CommandActivity extends AppCompatActivity {

    String user = null;
    int command;
    private static final int RC_BARCODE_COMMAND = 9003;
    static String urlocal = "http://192.168.0.14:3000";
    static String url = "https://gengiskhan.herokuapp.com";
    Retrofit retrofit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command);
        //url = urlocal;
        Bundle extras = getIntent().getExtras();
        user = extras.getString("user");
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .build();
    }

    public void startCommand(View vi) {
        ApiRoutes service = retrofit.create(ApiRoutes.class);
        Call<ResponseBody> result = service.newCommand(user);
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            String resp = response.body().string();
                            JSONObject com = new JSONObject(resp);
                            JSONArray commands = com.getJSONArray("commands");
                            com = commands.getJSONObject(commands.length() - 1);
                            command = com.getInt("command_id");
                            Intent myIntent = new Intent(CommandActivity.this, AddProductActivity.class);
                            myIntent.putExtra("command", command);
                            myIntent.putExtra("user", user);
                            startActivity(myIntent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }

        });
    }


    public void history(View view) {
        ApiRoutes service = retrofit.create(ApiRoutes.class);
        Call<ResponseBody> result = service.getUser(user);
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            String resp = response.body().string();
                            Intent myIntent = new Intent(CommandActivity.this, HistoryActivity.class);
                            myIntent.putExtra("resp", resp);
                            myIntent.putExtra("user", user);
                            startActivity(myIntent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }

        });
    }
}
