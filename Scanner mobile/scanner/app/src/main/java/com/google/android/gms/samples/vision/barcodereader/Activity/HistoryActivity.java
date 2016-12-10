package com.google.android.gms.samples.vision.barcodereader.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.samples.vision.barcodereader.ApiRoutes;
import com.google.android.gms.samples.vision.barcodereader.Command;
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

public class HistoryActivity extends AppCompatActivity {

    static String resp = "", user = "";
    static String urlocal = "http://192.168.0.14:3000";
    static String url = "https://gengiskhan.herokuapp.com";
    Retrofit retrofit;

    ArrayList<Command> allCommands = new ArrayList<>();

    public static CommandCursorAdapter commandAdapter;
    ListView listView;
    Context context;
    static JSONArray commands;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.command);

        //url = urlocal;

        context = this;

        Bundle extras = getIntent().getExtras();
        resp = extras.getString("resp");
        user = extras.getString("user");

        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .build();

        listView = (ListView) findViewById(R.id.commands);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Command c = (Command) listView.getItemAtPosition(position);
                Log.d("command ", c.toString());
                Intent myIntent = new Intent(HistoryActivity.this, CommandOneViewActivity.class);
                try {
                    myIntent.putExtra("resp", getCommand(c.getId()).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(myIntent);
            }
        });

        JSONObject com = null;
        try {
            com = new JSONObject(resp);
            allCommands.clear();
            commands = com.getJSONArray("commands");
            for (int i = 0; i < commands.length(); i++) {
                JSONObject ob = commands.getJSONObject(i);
                Command c = new Command(ob.getString("command_id"), ob.getString("date"), ob.getString("price"), ob.getString("payed"));
                allCommands.add(c);
            }
            commandAdapter = new CommandCursorAdapter(context, allCommands);
            listView.setAdapter(commandAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static JSONObject getCommand(String id) throws JSONException {
        for (int i = 0; i < commands.length(); i++) {
            JSONObject ob = commands.getJSONObject(i);
            Log.d("ob ",ob.toString());
            if (ob.getString("command_id").equals(id))
                return ob;
        }
        return null;
    }
}
