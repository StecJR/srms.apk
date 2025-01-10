package com.stec.srms.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.view.MotionEvent;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.stec.srms.R;

public class Util {
    // Password show/hide
    private static void setCompoundDrawable(EditText editText, Drawable drawable) {
        editText.setCompoundDrawablesWithIntrinsicBounds(
                editText.getCompoundDrawables()[0],
                editText.getCompoundDrawables()[1],
                drawable,
                editText.getCompoundDrawables()[3]
        );
    }
    private static boolean isTouchWithinDrawableBounds(EditText editText, MotionEvent event) {
        Drawable drawable = editText.getCompoundDrawables()[2];
        return drawable != null && event.getRawX() >= (editText.getRight() - drawable.getBounds().width());
    }
    private static void togglePasswordVisibility(EditText editText, Drawable eyeShow, Drawable eyeHide) {
        Typeface typeFace = editText.getTypeface();
        if (editText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            setCompoundDrawable(editText, eyeShow);
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            setCompoundDrawable(editText, eyeHide);
        }

        editText.setTypeface(typeFace);
        editText.setSelection(editText.getText().length());
    }
    /**
     * Makes a password field show or hide the password on icon click.
     *
     * @param editText The EditText for the password input.
     * @param context  The Context to access resources.
     */
    @SuppressLint("ClickableViewAccessibility")
    public static void makePasswordShowable(@NonNull EditText editText, @NonNull Context context) {
        Drawable eyeShow = ContextCompat.getDrawable(context, R.drawable.eye_password_show);
        Drawable eyeHide = ContextCompat.getDrawable(context, R.drawable.eye_password_hide);

        setCompoundDrawable(editText, eyeHide);
        editText.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (isTouchWithinDrawableBounds(editText, event)) {
                    togglePasswordVisibility(editText, eyeShow, eyeHide);
                    return true;
                }
            }
            return false;
        });
    }
}