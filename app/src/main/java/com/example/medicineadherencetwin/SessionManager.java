package com.example.medicineadherencetwin;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "MedicineTwinPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USERNAME = "username";

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void registerUser(String username, String email, String password) {
        editor.putString(username + "_email", email);
        editor.putString(username + "_password", password);
        editor.apply();
    }

    public boolean isUserRegistered(String username) {
        return pref.contains(username + "_password");
    }

    public boolean validateCredentials(String username, String password) {
        String regPassword = pref.getString(username + "_password", null);
        return regPassword != null && regPassword.equals(password);
    }

    public void createLoginSession(String username) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USERNAME, username);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public String getUsername() {
        return pref.getString(KEY_USERNAME, "");
    }

    public void logoutUser() {
        editor.putBoolean(KEY_IS_LOGGED_IN, false);
        editor.remove(KEY_USERNAME);
        editor.apply();
    }
}
