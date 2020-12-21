package com.mraleksmay.projects.download_manager.client.gui.platform.model.user;


import com.mraleksmay.projects.download_manager.plugin.model.user.User;
import com.mraleksmay.projects.download_manager.plugin.model.user.settings.Settings;

public class Customer extends User {


    private Customer() {
    }


    @Override
    public synchronized Settings getSettings() {
        return getCurrentUser().getSettings();
    }

}
