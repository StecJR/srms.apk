package com.stec.srms.util;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.stec.srms.R;

public class Toast {
    public static void credentialError(Context context) {
        android.widget.Toast.makeText(context, "Wrong credentials", android.widget.Toast.LENGTH_LONG).show();
    }


    public static void errorMessage(Context context, String message) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.toast_error, null);
        TextView textView = layout.findViewById(R.id.toastErrorText);
        textView.setText(message);

        android.widget.Toast toast = new android.widget.Toast(context);
        toast.setDuration(android.widget.Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
    /**
     * Custom toast to show success message.
     *
     * @param context  The Context to access resources.
     * @param message  The message to show.
     */
    public static void success(Context context, String message) {
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
