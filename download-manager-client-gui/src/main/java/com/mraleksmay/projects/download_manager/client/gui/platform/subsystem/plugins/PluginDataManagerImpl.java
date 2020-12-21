package com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.plugins;

import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.ApplicationManager;
import com.mraleksmay.projects.download_manager.plugin.model.category.Category;
import com.mraleksmay.projects.download_manager.plugin.model.group.Group;
import com.mraleksmay.projects.download_manager.plugin.model.plugin.Plugin;
import com.mraleksmay.projects.download_manager.plugin.manager.PluginDataManager;

import java.net.URL;

public class PluginDataManagerImpl extends PluginDataManager {
    private ApplicationManager applicationManager;


    public PluginDataManagerImpl(ApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
    }


    public Plugin getPluginBy(String pluginId) {
        return getApplicationManager().getPluginsManager().getPluginBy(pluginId);
    }

    public Group getGroupBy(String pluginId, String groupId) {
        return getApplicationManager().getPluginsManager().getGroupBy(pluginId, groupId);
    }

    public Category getCategoryBy(String pluginId, String groupId, String categoryId) {
        return getApplicationManager().getPluginsManager().getCategoryBy(pluginId, groupId, categoryId);
    }

    public Category findCategoryBy(String serializableId) {
        return getApplicationManager().getPluginsManager().findCategoryBy(serializableId);
    }

    public Category tryDeterminateCategoryBy(URL url, String nameStr, String cType) {
        return getApplicationManager().getPluginsManager().tryDeterminateCategoryBy(url, nameStr, cType);
    }


    public ApplicationManager getApplicationManager() {
        return applicationManager;
    }
}
