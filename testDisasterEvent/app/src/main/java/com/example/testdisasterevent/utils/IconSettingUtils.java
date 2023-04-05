package com.example.testdisasterevent.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testdisasterevent.R;

public class IconSettingUtils {
    public ImageView createDisIconOnWindow (String title, Context context) {
        // Create and add an ImageView to the RelativeLayout - disaster logo
        ImageView imageView = new ImageView(context);
        if (title.equals("fire")) {
            imageView.setImageResource(R.drawable.fire_logo);
        } else if (title.equals("water")) {
            imageView.setImageResource(R.drawable.water_logo);
        } else {
            imageView.setImageResource(R.drawable.other_logo);
        }
        return imageView;
    }

    public Bitmap createDisIconOnMap (String title, Resources resources) {
        // Create and add an ImageView to the RelativeLayout - disaster logo
        Bitmap bitmap;
        if (title.equals("fire")) {
            bitmap = BitmapFactory.decodeResource(resources, R.drawable.fire_logo);
        } else if (title.equals("water")) {
            bitmap = BitmapFactory.decodeResource(resources, R.drawable.water_logo);
        } else {
            bitmap = BitmapFactory.decodeResource(resources, R.drawable.other_logo);
        }
        return bitmap;
    }

    public void setDisIconResource (String title, ImageView disaster_logo) {
        if (title.equals("fire")) {
            disaster_logo.setImageResource(R.drawable.fire_logo);
        } else if (title.equals("water")) {
            disaster_logo.setImageResource(R.drawable.water_logo);
        } else {
            disaster_logo.setImageResource(R.drawable.other_logo);
        }
    }

    public void setDisTitle (String title, TextView txt_show_task) {
        if (title.equals("fire")) {
            txt_show_task.setText("Task");
            txt_show_task.setTextColor(Color.RED);
        } else if (title.equals("water")) {
            txt_show_task.setText("Task");
            txt_show_task.setTextColor(Color.BLUE);
        } else {
            txt_show_task.setText("Task");
            txt_show_task.setTextColor(Color.BLACK);
        }
    }
}
