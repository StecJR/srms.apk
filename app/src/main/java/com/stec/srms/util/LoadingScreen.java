package com.stec.srms.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.stec.srms.R;

import java.util.Objects;

public class LoadingScreen {
    private static Dialog loadingDialog;

    public static void start(@NonNull Context context, @NonNull String message) {
        if (loadingDialog != null && loadingDialog.isShowing()) return;

        loadingDialog = new Dialog(context);
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.setCancelable(false);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(Objects.requireNonNull(loadingDialog.getWindow()).getAttributes());
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        loadingDialog.getWindow().setAttributes(layoutParams);
        loadingDialog.getWindow().setDimAmount(0.7f);
        loadingDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView loadingText = loadingDialog.findViewById(R.id.loadingScreenText);
        loadingText.setText(message);
        loadingDialog.show();
    }

    public static void stop() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }
}
