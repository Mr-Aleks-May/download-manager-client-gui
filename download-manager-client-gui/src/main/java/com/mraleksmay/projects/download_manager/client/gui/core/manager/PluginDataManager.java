package com.mraleksmay.projects.download_manager.client.gui.core.manager;

import com.mraleksmay.projects.download_manager.common.model.category.Category;
import com.mraleksmay.projects.download_manager.common.model.group.Group;
import com.mraleksmay.projects.download_manager.common.model.plugin.Plugin;

import java.net.URL;

public class PluginDataManager extends com.mraleksmay.projects.download_manager.plugin.manager.PluginDataManager {
    private ApplicationManager applicationManager;


    public PluginDataManager(ApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
    }


    @Override
    public Plugin getPluginBy(String pluginId) {
        return getApplicationManager().getPluginsManager().getPluginBy(pluginId);
    }

    @Override
    public Group getGroupBy(String pluginId, String groupId) {
        return getApplicationManager().getPluginsManager().getGroupBy(pluginId, groupId);
    }

    @Override
    public Category getCategoryBy(String pluginId, String groupId, String categoryId) {
        return getApplicationManager().getPluginsManager().getCategoryBy(pluginId, groupId, categoryId);
    }

    @Override
    public Category findCategoryBy(String serializableId) {
        return getApplicationManager().getPluginsManager().findCategoryBy(serializableId);
    }

    @Override
    public Category tryDeterminateCategoryBy(URL url, String nameStr, String cType) {
        return getApplicationManager().getPluginsManager().tryDeterminateCategoryBy(url, nameStr, cType);
    }


    public ApplicationManager getApplicationManager() {
        return applicationManager;
    }
}
