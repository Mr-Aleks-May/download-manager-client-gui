package com.mraleksmay.projects.download_manager.client.gui.platform.view.dialog.store.component;

import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.ApplicationManager;

public class PluginMarketplaceController {
    private ApplicationManager applicationManager;


    public PluginMarketplaceController(ApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
    }


    public ApplicationManager getApplicationManager() {
        return applicationManager;
    }
}
