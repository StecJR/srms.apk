package github.stecjr.srms.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.util.Date;

import github.stecjr.srms.database.Database;
import github.stecjr.srms.model.AccountType;
import github.stecjr.srms.model.FacultySession;
import github.stecjr.srms.model.GuardianSession;
import github.stecjr.srms.model.StudentSession;

public class SessionManager {
    private static SharedPreferences sharedPreference;
    private static SessionManager sessionManager;

    private SessionManager(Context context) {
        if (sharedPreference == null) {
            try {
                sharedPreference = EncryptedSharedPreferences.create(
                        context,
                        "encrypted_session_pref",
                        new MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
            } catch (Exception e) {
                Toast.generalError(context, "Failed to create session");
                e.printStackTrace();
            }
        }
    }

    public static synchronized SessionManager getInstance(Context context) {
        if (sessionManager == null) sessionManager = new SessionManager(context);
        return sessionManager;
    }

    public boolean isFirstTime() {
        return sharedPreference.getBoolean("isFirstTime", true);
    }

    public void turnOffFirstTime() {
        sharedPreference.edit().putBoolean("isFirstTime", false).apply();
    }

    public void createStudentSession(int deptId, int studentId, int expiryDay) {
        sharedPreference.edit()
                .putInt("sessionStudentDeptId", deptId)
                .putInt("sessionStudentId", studentId)
                .putString("sessionUserType", "student")
                .putLong("sessionExpiryDate", new Date().getTime() + (expiryDay * 86400000L) - 1)
                .apply();
    }

    public void createFacultySession(int facultyId, int expiryDay) {
        sharedPreference.edit()
                .putInt("sessionFacultyId", facultyId)
                .putString("sessionUserType", "faculty")
                .putLong("sessionExpiryDate", new Date().getTime() + (expiryDay * 86400000L) - 1)
                .apply();
    }

    public void createGuardianSession(int guardianId, int deptId, int studentId, int expiryDay) {
        sharedPreference.edit()
                .putInt("sessionGuardianId", guardianId)
                .putInt("sessionStudentDeptId", deptId)
                .putInt("sessionStudentId", studentId)
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

    public String validSession() {
        long expiryDate = sharedPreference.getLong("sessionExpiryDate", 0);
        String accountType = getAccountType();
        if (expiryDate == 0 || expiryDate < new Date().getTime()) {
            if (accountType == null) return null;
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

    public int getAccountId(Context context) {
        String type = getAccountType();
        if (type == null) return -1;
        AccountType accType = Database.getInstance(context).getAccountType(type);
        if (accType == null) return -1;
        return accType.accountId;
    }

    public String getAccountType() {
        return sharedPreference.getString("sessionUserType", null);
    }

    public StudentSession getStudentSession() {
        int deptId = sharedPreference.getInt("sessionStudentDeptId", -1);
        int studentId = sharedPreference.getInt("sessionStudentId", -1);
        if (deptId == -1 || studentId == -1) return null;
        return new StudentSession(deptId, studentId);
    }

    public FacultySession getFacultySession() {
        int facultyId = sharedPreference.getInt("sessionFacultyId", -1);
        if (facultyId == -1) return null;
        return new FacultySession(facultyId);
    }

    public GuardianSession getGuardianSession() {
        int guardianId = sharedPreference.getInt("sessionGuardianId", -1);
        int studentId = sharedPreference.getInt("sessionStudentId", -1);
        int deptId = sharedPreference.getInt("sessionStudentDeptId", -1);
        if (guardianId == -1 || studentId == -1 || deptId == -1) return null;
        return new GuardianSession(guardianId, studentId, deptId);
    }

    public void deleteStudentSession() {
        sharedPreference.edit()
                .remove("sessionStudentDeptId")
                .remove("sessionStudentId")
                .remove("sessionUserType")
                .remove("sessionExpiryDate")
                .apply();
    }

    public void deleteFacultySession() {
        sharedPreference.edit()
                .remove("sessionFacultyId")
                .remove("sessionUserType")
                .remove("sessionExpiryDate")
                .apply();
    }

    public void deleteGuardianSession() {
        sharedPreference.edit()
                .remove("sessionGuardianId")
                .remove("sessionStudentDeptId")
                .remove("sessionStudentId")
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

    public void deleteAllSessions() {
        sharedPreference.edit()
                .remove("sessionStudentDeptId")
                .remove("sessionStudentId")
                .remove("sessionFacultyId")
                .remove("sessionGuardianId")
                .remove("sessionUserType")
                .remove("sessionExpiryDate")
                .apply();
    }
}
