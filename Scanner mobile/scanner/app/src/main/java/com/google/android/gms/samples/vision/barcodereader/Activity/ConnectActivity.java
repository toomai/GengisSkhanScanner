package com.google.android.gms.samples.vision.barcodereader.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.samples.vision.barcodereader.ApiRoutes;
import com.google.android.gms.samples.vision.barcodereader.camera.BarcodeCaptureActivity;
import com.google.android.gms.samples.vision.barcodereader.R;
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;


public class ConnectActivity extends AppCompatActivity {

    String user = null;
    private static final int RC_BARCODE_CONNECT = 9002;
    static String urlocal = "http://192.168.0.14:3000";
    static String url = "https://gengiskhan.herokuapp.com";
    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        //url = urlocal;
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .build();
    }

    public void connexion(View vi) {
        Intent intent = new Intent(this, BarcodeCaptureActivity.class);
        intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
        intent.putExtra(BarcodeCaptureActivity.UseFlash, true);
        startActivityForResult(intent, RC_BARCODE_CONNECT);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CONNECT) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    user = barcode.displayValue;
                    doRetrofitRequest();
                } else {
                    Log.d("User", "No barcode captured, intent data is null");
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void lancerDialog(String login, String name) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent myIntent = new Intent(ConnectActivity.this, CommandActivity.class);
                        myIntent.putExtra("user", user);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        LayoutInflater inflater = this.getLayoutInflater();
        View custom = inflater.inflate(R.layout.dialog_user, null);

        TextView text = (TextView) custom.findViewById(R.id.login);
        text.setText(login);

        text = (TextView) custom.findViewById(R.id.name);
        text.setText(name);

        builder.setView(custom);
        AlertDialog b = builder.create();

        b.show();
        b.getButton(b.BUTTON_POSITIVE).setTextColor(Color.parseColor("#ffa500"));
        b.getButton(b.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#ffa500"));

    }

    public void doRetrofitRequest() {
        ApiRoutes service = retrofit.create(ApiRoutes.class);
        Call<ResponseBody> result = service.getUser(user);
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            String resp = response.body().string();
                            Log.d("response", resp);
                            JSONObject user = new JSONObject(resp);
                            lancerDialog(user.getString("user_id"), user.getString("name"));
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
}
