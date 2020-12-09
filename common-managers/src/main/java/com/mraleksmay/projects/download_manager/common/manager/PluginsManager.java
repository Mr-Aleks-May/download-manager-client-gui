package com.mraleksmay.projects.download_manager.common.manager;


import com.mraleksmay.projects.download_manager.plugin.model.category.Category;
import com.mraleksmay.projects.download_manager.plugin.model.group.Group;
import com.mraleksmay.projects.download_manager.plugin.model.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public interface PluginsManager {
    void loadPlugins(File pluginsDir) throws IOException, ClassNotFoundException;

    void initPlugin() throws IOException;

    void add(Plugin plugin);

    void remove(Plugin plugin);

    int indexOf(Plugin plugin);

    Plugin getActivePlugin();

    Group getActiveGroup();

    Category getActiveCategory();

    Plugin getPluginBy(String pluginId);

    Group getGroupBy(String pluginId, String groupId);

    Category getCategoryBy(String pluginId, String groupId, String categoryId);

    Category findCategoryBy(String serializableId);

    Category tryDeterminateCategoryBy(URL url, String nameStr, String cType);

    List<Plugin> getPlugins();

    String getCurrentSelectedPluginId();

    void setCurrentSelectedPluginId(String currentSelectedPluginId);

    String getCurrentSelectedGroupId();

    void setCurrentSelectedGroupId(String currentSelectedGroupId);

    String getCurrentSelectedCategoryId();

    void setCurrentSelectedCategoryId(String currentSelectedCategoryId);

    ApplicationManager getApplicationManager();
}
