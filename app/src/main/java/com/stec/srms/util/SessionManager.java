package com.stec.srms.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.stec.srms.database.Database;
import com.stec.srms.model.FacultySession;
import com.stec.srms.model.GuardianSession;
import com.stec.srms.model.StudentSession;

import java.util.Date;
import java.util.Objects;

public class SessionManager {
    private static SharedPreferences sharedPreference;
    private static SessionManager sessionManager;
    Context context;

    private SessionManager(Context context) {
        this.context = context;
        if (sharedPreference == null) {
            try {
                sharedPreference = EncryptedSharedPreferences.create(
                        context,
                        "encrypted_session_pref",
                        new MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
            } catch (Exception e) {
                Log.e("SessionCreationError", String.valueOf(e));
                e.printStackTrace();
            }
        }
    }
    public static synchronized SessionManager getInstance(Context context) {
        if (sessionManager == null) {
            sessionManager = new SessionManager(context);
        }
        return sessionManager;
    }

    public void createStudentSession(int deptId, int studentId, int expiryDay) {
        sharedPreference.edit()
                .putInt("sessionUserDeptId", deptId)
                .putInt("sessionUserId", studentId)
                .putString("sessionUserType", "student")
                .putLong("sessionExpiryDate", new Date().getTime() + (expiryDay * 86400000L) - 1)
                .apply();
    }
    public void createFacultySession(int facultyId, int expiryDay) {
        sharedPreference.edit()
                .putInt("sessionUserId", facultyId)
                .putString("sessionUserType", "faculty")
                .putLong("sessionExpiryDate", new Date().getTime() + (expiryDay * 86400000L) - 1)
                .apply();
    }
    public void createGuardianSession(int guardianId, int deptId, int studentId, int expiryDay) {
        sharedPreference.edit()
                .putInt("sessionUserId", guardianId)
                .putInt("sessionSubUserDeptId", deptId)
                .putInt("sessionSubUserId", studentId)
                .putString("sessionUserType", "guardian")
                .putLong("sessionExpiryDate", new Date().getTime() + (expiryDay * 86400000L) - 1)
                .apply();
    }
    public void createAdminSession(int expiryDay) {
        sharedPreference.edit()
                .putString("sessionUserType", "admin")
                .putLong("sessionExpiryDate", new Date().getTime() + (expiryDay * 86400000L) - 1)
                .apply();
    }

    public String getAccountType() {
        return sharedPreference.getString("sessionUserType", "");
    }
    public int getAccountId() {
        String type = getAccountType();
        if (type.isEmpty()) return -1;
        Integer id = Database.getInstance(context).getAccountType(type).accountId;
        if (id == null) return -1;
        return (int) id;
    }
    public String validSession() {
        long expiryDate = sharedPreference.getLong("sessionExpiryDate", 0);
        String accountType = getAccountType();
        if (expiryDate == 0 || expiryDate < new Date().getTime()) {
            switch (accountType) {
                case "student":
                    deleteStudentSession();
                    break;
                case "faculty":
                    deleteFacultySession();
                    break;
                case "guardian":
                    deleteGuardianSession();
                    break;
                case "admin":
                    deleteAdminSession();
                    break;
            }
            return null;
        }
        return accountType;
    }
    public StudentSession getStudentSession() {
        int deptId = sharedPreference.getInt("sessionUserDeptId", -1);
        int studentId = sharedPreference.getInt("sessionUserId", 0);
        if (deptId == -1 || studentId == 0) return null;
        return new StudentSession(deptId, studentId);
    }
    public FacultySession getFacultySession() {
        int facultyId = sharedPreference.getInt("sessionUserId", 0);
        if (facultyId == 0) return null;
        return new FacultySession(facultyId);
    }
    public GuardianSession getGuardianSession() {
        int guardianId = sharedPreference.getInt("sessionUserId", 0);
        int deptId = sharedPreference.getInt("sessionSubUserDeptId", -1);
        int studentId = sharedPreference.getInt("sessionSubUserId", 0);
        if (guardianId == 0 || studentId == 0 || deptId == -1) return null;
        return new GuardianSession(guardianId, deptId, studentId);
    }

    public void deleteStudentSession() {
        sharedPreference.edit()
                .remove("sessionUserDeptId")
                .remove("sessionUserId")
                .remove("sessionUserType")
                .remove("sessionExpiryDate")
                .apply();
    }
    public void deleteFacultySession() {
        sharedPreference.edit()
                .remove("sessionUserId")
                .remove("sessionUserType")
                .remove("sessionExpiryDate")
                .apply();
    }
    public void deleteGuardianSession() {
        sharedPreference.edit()
                .remove("sessionUserId")
                .remove("sessionSubUserDeptId")
                .remove("sessionSubUserId")
                .remove("sessionUserType")
                .remove("sessionExpiryDate")
                .apply();
    }
    public void deleteAdminSession() {
        sharedPreference.edit()
                .remove("sessionUserType")
                .remove("sessionExpiryDate")
                .apply();
    }

    public boolean isFirstTime() {
        return sharedPreference.getBoolean("isFirstTime", true);
    }
    public void turnOffFirstTime() {
        sharedPreference.edit()
                .putBoolean("isFirstTime", false)
                .apply();
    }
}
