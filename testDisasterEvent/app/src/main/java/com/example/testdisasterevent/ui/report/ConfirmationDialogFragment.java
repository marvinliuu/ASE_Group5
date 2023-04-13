package com.example.testdisasterevent.ui.report;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import com.example.testdisasterevent.R;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Date: 23.04.12
 * Function: create confirm dialog fragment
 * Author: Zhipeng Fu
 * Version: Week 12
 */
public class ConfirmationDialogFragment extends DialogFragment {
    private static final String ARG_NAME = "new";


    public static ConfirmationDialogFragment newInstance(String name) {
        ConfirmationDialogFragment fragment = new ConfirmationDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String name = getArguments().getString(ARG_NAME);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Alert")
                .setMessage("The " + name+" are required")
                .setPositiveButton("Back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("Button click", "message back clicked!");
                    }
                });
        return builder.create();
    }
}
