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

public class CommandOneViewActivity extends AppCompatActivity {


    String resp = null;

    public static LineCursorAdapter sourcesAdapter;
    public static ArrayList<Line> linesArray = new ArrayList<>();
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commande_liste);

        context = this;

        Bundle extras = getIntent().getExtras();
        resp = extras.getString("resp");

        final ListView listView = (ListView) findViewById(R.id.lines);

        JSONArray lines = null;
        try {
            lines = new JSONObject(resp).getJSONArray("lines");
            linesArray.clear();
            for (int i = 0; i < lines.length(); i++) {
                JSONObject line = lines.getJSONObject(i);
                Log.d("lien", line.toString());
                Line l = new Line(line.getString("name"), line.getString("image"), line.getString("price"), line.getString("quantity"));
                linesArray.add(l);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sourcesAdapter = new LineCursorAdapter(context, linesArray);
        listView.setAdapter(sourcesAdapter);
    }


}


