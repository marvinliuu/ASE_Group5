package com.example.testdisasterevent.utils;

import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.example.testdisasterevent.R;

/**
 * Date: 23.04.06
 * Function: Popupwindow Relative Utils
 * Author: Siyu Liao
 * Version: Week 11
 */
public class PopupwindowUtils {

    public PopupWindow showPopwindow (View view, int height) {
        PopupWindow popupWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.MATCH_PARENT,
                height);
        // set SelectPicPopupWindow height
        popupWindow.setHeight(height);
        // get focus point
        popupWindow.setFocusable(true);
        // set background color of blank area
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // Click outside to disappear
        popupWindow.setOutsideTouchable(true);
        // Settings can be clicked
        popupWindow.setTouchable(true);
        // hidden animation
        popupWindow.setAnimationStyle(R.style.ipopwindow_anim_style);
        return popupWindow;
    }
}
