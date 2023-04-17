package com.example.testdisasterevent.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testdisasterevent.R;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class IconSettingUtils {
    /**
     * Date: 23.04.17
     * Function: create disaster icon on relativelayout
     * Author: Siyu Liao
     * Version: Week 13
     */
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

    /**
     * Date: 23.04.17
     * Function: create report event icon on relativelayout
     * Author: Siyu Liao
     * Version: Week 13
     */
    public ImageView createReportEvenIconOnWindow (String title, Context context) {
        // Create and add an ImageView to the RelativeLayout - disaster logo
        ImageView imageView = new ImageView(context);
        if (title.equals("fire")) {
            imageView.setImageResource(R.drawable.fire_event_image);
        } else if (title.equals("water")) {
            imageView.setImageResource(R.drawable.water_event_image);
        } else {
            imageView.setImageResource(R.drawable.other_event_image);
        }
        return imageView;
    }

    /**
     * Date: 23.04.17
     * Function: create disaster icon on map
     * Author: Siyu Liao
     * Version: Week 13
     */
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

    /**
     * Date: 23.04.17
     * Function: set disaster icon resource
     * Author: Siyu Liao
     * Version: Week 13
     */
    public void setDisIconResource (String title, ImageView disaster_logo) {
        if (title.equals("fire")) {
            disaster_logo.setImageResource(R.drawable.fire_logo);
        } else if (title.equals("water")) {
            disaster_logo.setImageResource(R.drawable.water_logo);
        } else {
            disaster_logo.setImageResource(R.drawable.other_logo);
        }
    }

    /**
     * Date: 23.04.17
     * Function: set officer task title
     * Author: Siyu Liao
     * Version: Week 13
     */
    public void setTaskTitle (String title, TextView txt_show_task) {
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

    /**
     * Date: 23.04.17
     * Function: set report title
     * Author: Siyu Liao
     * Version: Week 13
     */
    public void setReportTitle (String title, TextView txt_show) {
        if (title.equals("Fire")) {
            txt_show.setText(title);
            txt_show.setTextColor(Color.RED);
        } else if (title.equals("Water")) {
            txt_show.setText(title);
            txt_show.setTextColor(Color.BLUE);
        } else {
            txt_show.setText(title);
            txt_show.setTextColor(Color.RED);
        }
    }

    /**
     * Date: 23.04.17
     * Function: set ori / des icon on map
     * Author: Siyu Liao
     * Version: Week 13
     */
    public Bitmap setOriDesIcon (Boolean ori, Resources resources) {
        // Create and add an ImageView to the RelativeLayout - disaster logo
        Bitmap bitmap;
        if (ori) {
            bitmap = BitmapFactory.decodeResource(resources, R.drawable.originpoint);
        } else {
            bitmap = BitmapFactory.decodeResource(resources, R.drawable.destination);
        }
        return bitmap;
    }

}
