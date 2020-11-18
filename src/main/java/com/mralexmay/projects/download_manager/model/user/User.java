package com.mralexmay.projects.download_manager.model.user;

import com.mralexmay.projects.download_manager.settings.Settings;

public class User {
    private static User currentUser;
    private Settings settings = new Settings();


    private User() {
    }


    public synchronized static User getCurrentUser() {
        if (currentUser == null) {
            currentUser = new User();
        }

        return currentUser;
    }

    public synchronized Settings getSettings() {
        return User.getCurrentUser().settings;
    }
}
