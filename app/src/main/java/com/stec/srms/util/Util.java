package com.stec.srms.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.stec.srms.R;

public class Util {
    private static Drawable eyeShow, eyeHide;

    public static int stringToInt(String value, int defaultValue) {
        if (value == null || value.trim().isBlank()) return defaultValue;
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static int dpToPx(@NonNull Context context, int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.getResources().getDisplayMetrics()
        );
    }

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
        return drawable != null && event.getRawX() >= (editText.getRight() - drawable.getBounds().width() - 50);
    }
    private static void togglePasswordVisibility(EditText editText) {
        Typeface typeFace = editText.getTypeface();
        int cursorPosition = editText.getSelectionStart();
        if (editText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            setCompoundDrawable(editText, eyeShow);
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            setCompoundDrawable(editText, eyeHide);
        }
        editText.setSelection(cursorPosition);
        editText.setTypeface(typeFace);
    }
    @SuppressLint("ClickableViewAccessibility")
    public static void addPasswordVisibilityToggler(@NonNull EditText editText, @NonNull Context context) {
        if (eyeShow == null || eyeHide == null) {
            eyeShow = ContextCompat.getDrawable(context, R.drawable.eye_password_show);
            eyeHide = ContextCompat.getDrawable(context, R.drawable.eye_password_hide);
        }
        setCompoundDrawable(editText, eyeHide);
        editText.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (isTouchWithinDrawableBounds(editText, event)) {
                    togglePasswordVisibility(editText);
                    return true;
                }
            }
            return false;
        });
    }
}