package com.google.android.gms.samples.vision.barcodereader.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.google.android.gms.samples.vision.barcodereader.ApiRoutes;
import com.google.android.gms.samples.vision.barcodereader.Line;
import com.google.android.gms.samples.vision.barcodereader.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class CommandViewActivity extends AppCompatActivity {


    String user = null, select = null;

    static String urlocal = "http://192.168.0.14:3000";
    static String url = "https://gengiskhan.herokuapp.com";
    Retrofit retrofit;

    public static LineCursorAdapter sourcesAdapter;
    public static ArrayList<Line> linesArray = new ArrayList<>();
    ListView listView;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commande_liste);

        context = this;

        Bundle extras = getIntent().getExtras();
        user = extras.getString("user");
        select = extras.getString("select");

        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .build();

        listView = (ListView) findViewById(R.id.lines);



        ApiRoutes service = retrofit.create(ApiRoutes.class);
        Call<ResponseBody> result = service.getCommand(user);
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            String resp = response.body().string();
                            JSONArray lines = new JSONObject(resp).getJSONArray("lines");
                            linesArray.clear();
                            for (int i = 0; i < lines.length(); i++) {
                                JSONObject line = lines.getJSONObject(i);
                                Log.d("lien", line.toString());
                                Line l = new Line(line.getString("name"), line.getString("image"), line.getString("price"), line.getString("quantity"));
                                linesArray.add(l);
                            }
                            sourcesAdapter = new LineCursorAdapter(context, linesArray);
                            listView.setAdapter(sourcesAdapter);
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
