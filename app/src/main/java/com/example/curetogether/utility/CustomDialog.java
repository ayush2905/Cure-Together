package com.example.curetogether.utility;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;

import com.example.curetogether.R;

public class CustomDialog {
    private Context context;
    private AlertDialog dialog;

    public CustomDialog(Context context) {
        this.context = context;
        dialog = new AlertDialog.Builder(context)
                .setView(LayoutInflater.from(context).inflate(R.layout.view_loading_dialog, null, false))
                .setCancelable(false)
                .create();
    }

    public void start() {
        dialog.show();
    }

    public void stop() {
        dialog.dismiss();
    }
}
