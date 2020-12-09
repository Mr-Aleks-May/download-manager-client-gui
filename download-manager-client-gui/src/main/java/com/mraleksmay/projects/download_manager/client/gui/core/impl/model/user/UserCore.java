package com.mraleksmay.projects.download_manager.client.gui.core.impl.model.user;


import com.mraleksmay.projects.download_manager.common.model.user.User;
import com.mraleksmay.projects.download_manager.common.model.user.settings.Settings;

public class UserCore extends User {


    private UserCore() {
    }


    @Override
    public synchronized Settings getSettings() {
        return getCurrentUser().getSettings();
    }

}
