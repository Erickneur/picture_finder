package com.uhnux.picturefinder.util;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.uhnux.picturefinder.R;

public class ProgressDialog {
    private Context context;
    private LayoutInflater layoutInflater;

    private Dialog progressDialog;

    public ProgressDialog(Context context, LayoutInflater layoutInflater) {
        this.context = context;
        this.layoutInflater = layoutInflater;
        init();
    }

    private void init() {
        @SuppressLint("InflateParams") View view = layoutInflater.inflate(R.layout.custom_progress_dialog, null);

        progressDialog = new Dialog(context);
        progressDialog.setContentView(view);
        progressDialog.setCancelable(false);
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    public void show(boolean show) {
        if (show) progressDialog.show();
        else progressDialog.dismiss();
    }
}