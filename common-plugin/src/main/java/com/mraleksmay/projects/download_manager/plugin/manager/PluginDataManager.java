package com.mraleksmay.projects.download_manager.plugin.manager;


import com.mraleksmay.projects.download_manager.common.model.category.Category;
import com.mraleksmay.projects.download_manager.common.model.group.Group;
import com.mraleksmay.projects.download_manager.common.model.plugin.Plugin;

import java.net.URL;

/**
 * Provides access to application data for third party plugins
 */
public abstract class PluginDataManager {
    /**
     * Searches for a plugin at the specified identifier.
     *
     * @param pluginId plugin identifier
     * @return If the plugin was found, then it returns this plugin. Otherwise null.
     */
    public abstract Plugin getPluginBy(String pluginId);

    /**
     * Searches for a group at the specified plugin identifier and group identifier.
     *
     * @param pluginId plugin identifier
     * @param groupId  group identifier
     * @return If the group was found, then it returns this group. Otherwise null.
     */

    public abstract Group getGroupBy(String pluginId, String groupId);

    /**
     * Searches for a category at the specified plugin identifier, group identifier and category identifier.
     *
     * @param pluginId   plugin identifier
     * @param groupId    group identifier
     * @param categoryId category identifier
     * @return If the category was found, then it returns this category. Otherwise null.
     */
    public abstract Category getCategoryBy(String pluginId, String groupId, String categoryId);

    /**
     * Searches for a category at the specified category serialize identifier.
     *
     * @param serializableId serialize identifier
     * @return If the category was found, then it returns this category. Otherwise null.
     */
    public abstract Category findCategoryBy(String serializableId);

    /**
     * Suggests the most appropriate category to download based on: url, file name, download content type.
     *
     * @param url     url address
     * @param nameStr file name
     * @param cType   download content type
     * @return
     */
    public abstract Category tryDeterminateCategoryBy(URL url, String nameStr, String cType);
}
