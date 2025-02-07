package com.stec.srms.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionHandler {
    public static final int REQUEST_PICK_IMAGE = 11;
    public static final int REQUEST_READ_EXTERNAL_STORAGE = 1;
    public static final int REQUEST_WRITE_EXTERNAL_STORAGE = 2;
    public static final int REQUEST_MANAGE_EXTERNAL_STORAGE = 3;

    public static boolean checkReadStoragePermission(Context context) {
        return ContextCompat.checkSelfPermission(
                context, android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestReadStoragePermission(Activity activity) {
        ActivityCompat.requestPermissions(
                activity,
                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_READ_EXTERNAL_STORAGE
        );
    }

    public static boolean checkWriteStoragePermission(Context context) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            return ContextCompat.checkSelfPermission(
                    context, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED;
        } else {
            return android.os.Environment.isExternalStorageManager();
        }
    }

    public static void requestWriteStoragePermission(Activity activity) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_EXTERNAL_STORAGE
            );
        } else {
            Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
            Uri uri = Uri.parse("package:" + activity.getPackageName());
            intent.setData(uri);
            activity.startActivityForResult(intent, REQUEST_MANAGE_EXTERNAL_STORAGE);
        }
    }
}
/*
 */
