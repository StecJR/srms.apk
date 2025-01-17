package com.stec.srms.util;

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

    public static void errorMessage(@NonNull Context context, String message) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.toast_error, null);
        TextView textView = layout.findViewById(R.id.toastErrorText);
        textView.setText(message);

        android.widget.Toast toast = new android.widget.Toast(context);
        toast.setDuration(android.widget.Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public static void success(@NonNull Context context, String message) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.toast_error, null);
        TextView textView = layout.findViewById(R.id.toastErrorText);
        textView.setText(message);

        android.widget.Toast toast = new android.widget.Toast(context);
        toast.setDuration(android.widget.Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
