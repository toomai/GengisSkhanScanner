package com.google.android.gms.samples.vision.barcodereader.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.samples.vision.barcodereader.ApiRoutes;
import com.google.android.gms.samples.vision.barcodereader.RoundedImageView;
import com.google.android.gms.samples.vision.barcodereader.camera.BarcodeCaptureActivity;
import com.google.android.gms.samples.vision.barcodereader.R;
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class AddProductActivity extends AppCompatActivity {

    static String user = null;
    static int command;
    static String product = null;
    static int quantity;
    static final int RC_BARCODE_PRODUCT = 9004;
    static String urlocal = "http://192.168.0.14:3000";
    static String url = "https://gengiskhan.herokuapp.com";
    Retrofit retrofit;
    Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        //url = urlocal;
        context = this;
        NumberPicker numberPicker = (NumberPicker) findViewById(R.id.numberPicker);
        numberPicker.setMaxValue(100);
        numberPicker.setMinValue(1);
        numberPicker.setWrapSelectorWheel(true);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                quantity = newVal;
            }
        });

        Bundle extras = getIntent().getExtras();
        user = extras.getString("user");
        command = extras.getInt("command");
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .build();
    }

    public void scan(View vi) {
        Intent intent = new Intent(this, BarcodeCaptureActivity.class);
        intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
        intent.putExtra(BarcodeCaptureActivity.UseFlash, true);
        startActivityForResult(intent, RC_BARCODE_PRODUCT);
    }

    public void finish(View vi) {
        Intent myIntent = new Intent(this, CommandViewActivity.class);
        myIntent.putExtra("user", user);
        startActivity(myIntent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_PRODUCT) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    product = barcode.displayValue;
                    addProduct();
                } else {
                    Log.d("User", "No barcode captured, intent data is null");
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void addProduct() {


        ApiRoutes service = retrofit.create(ApiRoutes.class);
        if (quantity == 0)
            quantity = 1;
        Call<ResponseBody> result = service.addProduct(user, String.valueOf(command), product, String.valueOf(quantity));
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            String resp = response.body().string();
                            JSONArray lines = new JSONObject(resp).getJSONArray("lines");
                            Log.d("Lines", lines.toString());
                            JSONObject line = lines.getJSONObject(lines.length() - 1);
                            Log.d("Line", line.toString());
                            lancerDialog(line.getString("image"), line.getString("product_id"), line.getString("name"), line.getDouble("price"), line.getInt("quantity"));
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

    public void lancerDialog(String image, String product, String name, Double price, int quantity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View custom = inflater.inflate(R.layout.dialog_product, null);


        TextView text = (TextView) custom.findViewById(R.id.product_add);
        text.setText(product);


        text = (TextView) custom.findViewById(R.id.name_add);
        text.setText(name);


        text = (TextView) custom.findViewById(R.id.price_add);
        text.setText(price + " $ x"+quantity);




        ImageView img = (ImageView) custom.findViewById(R.id.image_add);
        chargerImageAsset(context, image, img);

        builder.setView(custom);
        AlertDialog b = builder.create();

        b.show();

        b.getButton(b.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#ffa500"));

    }


    public void chargerImageAsset(Context context, String path, ImageView image) {
        try {

            RoundedImageView roundedImage;
            InputStream img = context.getAssets().open(path);
            Bitmap bm = BitmapFactory.decodeStream(img);
            roundedImage = new RoundedImageView(bm);
            image.setImageDrawable(roundedImage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
