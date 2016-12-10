package com.google.android.gms.samples.vision.barcodereader.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.samples.vision.barcodereader.Command;
import com.google.android.gms.samples.vision.barcodereader.Line;
import com.google.android.gms.samples.vision.barcodereader.R;
import com.google.android.gms.samples.vision.barcodereader.RoundedImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class CommandCursorAdapter extends ArrayAdapter<Command> {

    public CommandCursorAdapter(Context context, ArrayList<Command> c) {
        super(context, 0, c);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.command_item, parent, false);

        Command sou = getItem(position);

        TextView text = (TextView) convertView.findViewById(R.id.command_info);
        text.setText("Id : "+sou.getId() + " Date :  " + sou.getDate()+" .");

        text = (TextView) convertView.findViewById(R.id.command_infos);
        text.setText("Total : "+ sou.getPrice() + " $");

        return convertView;
    }


}
