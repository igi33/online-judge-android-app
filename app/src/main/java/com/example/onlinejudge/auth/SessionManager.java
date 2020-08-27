package com.example.onlinejudge.auth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.onlinejudge.models.User;

import javax.inject.Inject;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;
    // Sharedpref file name
    private static final String PREF_NAME = "AndroidHivePref";
    // All Shared Preferences Keys
    private static final String KEY_LOGGED_IN = "userloggedin";
    private static final String KEY_ID = "userid";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_TOKEN = "usertoken";

    @SuppressLint("CommitPrefEdits")
    @Inject
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(User user) {
        editor.putBoolean(KEY_LOGGED_IN, true);
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_TOKEN, user.getToken());
        editor.commit();
    }

    /**
     * Get stored session data
     */
    public User getUserDetails() {
        User user = new User();
        user.setId(pref.getInt(KEY_ID, 0));
        user.setUsername(pref.getString(KEY_USERNAME, null));
        user.setToken(pref.getString(KEY_TOKEN, null));
        return user;
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
    }

    /**
     * Quick check for login
     **/
    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_LOGGED_IN, false);
    }
}
