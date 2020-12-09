package com.mraleksmay.projects.download_manager.common.model.user;

import com.mraleksmay.projects.download_manager.common.model.user.settings.Settings;

public class User {
    private static User currentUser;
    public String token;
    private Settings settings = new Settings();


    private static void createUser() {
        currentUser = new User();
    }

    public static User getCurrentUser() {
        if (currentUser == null) {
            createUser();
        }

        return currentUser;
    }

    public Settings getSettings() {
        return settings;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
