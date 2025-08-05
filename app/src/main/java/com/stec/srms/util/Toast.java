package com.stec.srms.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;

import com.stec.srms.R;

public class Toast {
    public static final int SHORT = android.widget.Toast.LENGTH_SHORT;
    public static final int LONG = android.widget.Toast.LENGTH_LONG;

    public static void credentialError(Context context) {
        android.widget.Toast.makeText(context, "Wrong credentials", android.widget.Toast.LENGTH_LONG).show();
    }

    public static void generalError(Context context, String message) {
        android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_LONG).show();
    }

    public static void databaseError(Context context, String message) {
        android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_LONG).show();
    }

    public static void authenticationError(Context context, String message) {
        android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_LONG).show();
    }

    public static void generalWarning(Context context, String message) {
        android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_LONG).show();
    }

    public static void generalInfo(Context context, String message) {
        android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_LONG).show();
    }

    public static void databaseInfo(Context context, String message) {
        android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_LONG).show();
    }

    public static void generalSuccess(Context context, String message) {
        android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_LONG).show();
    }

    public static void errorMessage(Context context, String message, int length) {
        LayoutInflater inflater = LayoutInflater.from(context);
        @SuppressLint("InflateParams") View layout = inflater.inflate(R.layout.layout_toast, null);

        LinearLayout toastLayout = layout.findViewById(R.id.toastLayout);
        ImageView toastImage = layout.findViewById(R.id.toastImage);
        TextView toastText = layout.findViewById(R.id.toastText);

        toastLayout.setBackgroundTintList(context.getColorStateList(R.color.red_lite));
        toastImage.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.eye_password_hide));
        toastText.setTextColor(context.getColor(R.color.red_dark));
        toastText.setText(message);

        android.widget.Toast toast = android.widget.Toast.makeText(context, "", length);
        toast.setView(layout);
        toast.show();
    }
}
