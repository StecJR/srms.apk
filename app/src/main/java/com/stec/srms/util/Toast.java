package com.stec.srms.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.stec.srms.R;

public class Toast {
    public static void credentialError(@NonNull Context context) {
        android.widget.Toast.makeText(context, "Wrong credentials", android.widget.Toast.LENGTH_LONG).show();
    }
    public static void generalError(@NonNull Context context, String message) {
        android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_LONG).show();
    }
    public static void databaseError(@NonNull Context context, String message) {
        android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_LONG).show();
    }
    public static void generalWarning(@NonNull Context context, String message) {
        android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_LONG).show();
    }
    public static void generalInfo(@NonNull Context context, String message) {
        android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_LONG).show();
    }
    public static void databaseInfo(@NonNull Context context, String message) {
        android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_LONG).show();
    }
    public static void generalSuccess(@NonNull Context context, String message) {
        android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_LONG).show();
    }

    public static void errorMessage(@NonNull Context context, String message) {
        LayoutInflater inflater = LayoutInflater.from(context);
        @SuppressLint("InflateParams")
        View layout = inflater.inflate(R.layout.toast_error, null);

        TextView textView = layout.findViewById(R.id.toastErrorText);
        textView.setText(message);

        android.widget.Toast toast = new android.widget.Toast(context);
        toast.setDuration(android.widget.Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
