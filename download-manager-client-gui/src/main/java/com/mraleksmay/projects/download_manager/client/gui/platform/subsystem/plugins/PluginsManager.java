package com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.plugins;

import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.plugins.loader.PluginsLoader;
import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.ApplicationManager;
import com.mraleksmay.projects.download_manager.plugin.model.category.Category;
import com.mraleksmay.projects.download_manager.plugin.model.group.Group;
import com.mraleksmay.projects.download_manager.plugin.model.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PluginsManager {
    private List<Plugin> plugins;
    private final ApplicationManager applicationManager;
    private String currentSelectedPluginId;
    private String currentSelectedGroupId;
    private String currentSelectedCategoryId;


    public PluginsManager(ApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
    }


    public void loadPlugins(File pluginsDir) throws IOException, ClassNotFoundException {
        final PluginsLoader loader = new PluginsLoader(getApplicationManager());
        final List<Plugin> plugins = loader.loadPlugins(pluginsDir);

        for (Plugin plugin : plugins) {
            add(plugin);
        }
    }

    public void initPlugin() throws IOException {
        for (Plugin plugin : getPlugins()) {
            try {
                plugin.init();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }


    public synchronized void add(Plugin plugin) {
        getPluginsList().add(plugin);
    }

    public void remove(Plugin plugin) {
        getPluginsList().remove(plugin);
    }


    public synchronized int indexOf(Plugin plugin) {
        List<Plugin> plugins = getPluginsList();

        for (int i = 0; i < plugins.size(); i++) {
            if (plugins.get(i).equals(plugin)) {
                return i;
            }
        }

        return -1;
    }

    public Plugin getActivePlugin() {
        for (Plugin plugin : getPluginsList()) {
            if (plugin.getId().equals(currentSelectedPluginId)) {
                return plugin;
            }
        }

        return null;
    }

    public Group getActiveGroup() {
        for (Plugin plugin : getPluginsList()) {
            if (plugin.getId().equals(currentSelectedPluginId)) {
                for (Group group : plugin.getGroups()) {
                    if (group.getId().equals(currentSelectedGroupId)) {
                        return group;
                    }
                }
            }
        }

        return null;
    }

    public Category getActiveCategory() {
        for (Plugin plugin : getPluginsList()) {
            if (plugin.getId().equals(currentSelectedPluginId)) {
                for (Group group : plugin.getGroups()) {
                    if (group.getId().equals(currentSelectedGroupId)) {
                        for (Category category : group.getCategories()) {
                            if (category.getId().equals(currentSelectedCategoryId)) {
                                return category;
                            }
                        }
                    }
                }
            }
        }

        return null;
    }


    public Plugin getPluginBy(String pluginId) {
        pluginId = pluginId.toUpperCase();

        for (Plugin plugin : getPluginsList()) {
            if (plugin.getId().equals(pluginId)) {
                return plugin;
            }
        }

        return null;
    }

    public Group getGroupBy(String pluginId, String groupId) {
        pluginId = pluginId.toUpperCase();
        groupId = groupId.toUpperCase();

        for (Plugin plugin : getPluginsList()) {
            if (plugin.getId().equals(pluginId)) {
                return plugin.getGroupBy(groupId);
            }
        }

        return null;
    }

    public Category getCategoryBy(String pluginId, String groupId, String categoryId) {
        pluginId = pluginId.toUpperCase();
        groupId = groupId.toUpperCase();

        for (Plugin plugin : getPluginsList()) {
            if (plugin.getId().equals(pluginId)) {
                for (Group group : plugin.getGroups()) {
                    if (group.getId().equals(groupId)) {
                        for (Category category : group.getCategories()) {
                            if (category.getId().equals(categoryId)) {
                                return category;
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

    public Category findCategoryBy(String categoryCSID) {
        for (Plugin plugin : getPluginsList()) {
            for (Group group : plugin.getGroups()) {
                for (Category category : group.getCategories()) {
                    if (category.getCSID().equals(categoryCSID)) {
                        return category;
                    }
                }
            }
        }

        return null;
    }

    public Category tryDeterminateCategoryBy(URL url, String nameStr, String cType) {
        return getActiveCategory();
    }


    private synchronized List<Plugin> getPluginsList() {
        if (plugins == null) {
            plugins = new ArrayList<>();
        }

        return plugins;
    }

    public synchronized List<Plugin> getPlugins() {
        return Collections.unmodifiableList(getPluginsList());
    }



    public String getCurrentSelectedPluginId() {
        return currentSelectedPluginId;
    }

    public void setCurrentSelectedPluginId(String currentSelectedPluginId) {
        this.currentSelectedPluginId = currentSelectedPluginId;
    }

    public String getCurrentSelectedGroupId() {
        return currentSelectedGroupId;
    }

    public void setCurrentSelectedGroupId(String currentSelectedGroupId) {
        this.currentSelectedGroupId = currentSelectedGroupId;
    }

    public String getCurrentSelectedCategoryId() {
        return currentSelectedCategoryId;
    }

    public void setCurrentSelectedCategoryId(String currentSelectedCategoryId) {
        this.currentSelectedCategoryId = currentSelectedCategoryId;
    }

    public ApplicationManager getApplicationManager() {
        return applicationManager;
    }
}
