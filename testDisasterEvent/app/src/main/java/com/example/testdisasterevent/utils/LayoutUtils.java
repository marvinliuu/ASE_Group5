package com.example.testdisasterevent.utils;

import android.content.Context;
import android.widget.RelativeLayout;

public class LayoutUtils {
    /**
     * Date: 23.04.05
     * Following Function: create Single RelativeLayout For Popup window
     * Version: Week 11
     */
    public RelativeLayout createSingleRelativeLayout(int i, Context context) {
        // create single RelativeLayout
        RelativeLayout relativeLayout = new RelativeLayout(context);
        RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        relativeLayout.setId(i);
        relativeParams.addRule(RelativeLayout.BELOW, relativeLayout.getId());
        relativeParams.setMargins(0, 0, 0, 16); // set a top margin of 16dp
        relativeLayout.setLayoutParams(relativeParams);
        relativeLayout.setId(i);
        return relativeLayout;
    }
}
