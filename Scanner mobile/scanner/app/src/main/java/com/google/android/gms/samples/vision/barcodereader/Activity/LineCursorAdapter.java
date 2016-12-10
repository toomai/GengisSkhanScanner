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

import com.google.android.gms.samples.vision.barcodereader.Line;
import com.google.android.gms.samples.vision.barcodereader.R;
import com.google.android.gms.samples.vision.barcodereader.RoundedImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class LineCursorAdapter extends ArrayAdapter<Line> {

    public LineCursorAdapter(Context context, ArrayList<Line> c) {
        super(context, 0, c);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.line, parent, false);

        Line sou = getItem(position);

        TextView text = (TextView) convertView.findViewById(R.id.produit_name);
        text.setText(sou.getNom());

        text = (TextView) convertView.findViewById(R.id.prix);
        text.setText(sou.getPrice() + " $ x " + sou.getQuantity());

        ImageView img = (ImageView) convertView.findViewById(R.id.image);
        chargerImageAsset(getContext(), sou.getImage(), img);

        return convertView;
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
