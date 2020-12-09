package com.mraleksmay.projects.download_manager.common.manager;


import com.mraleksmay.projects.download_manager.plugin.model.category.Category;
import com.mraleksmay.projects.download_manager.plugin.model.download.Download;
import com.mraleksmay.projects.download_manager.plugin.model.group.Group;
import com.mraleksmay.projects.download_manager.plugin.model.plugin.Plugin;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IOManager {
    default List<Plugin> restorePlugin(List<Map<String, Object>> plugins) {
        List<Plugin> pluginsList = new ArrayList<>();

        for (Map<String, Object> map : plugins) {
            try {
                String classStr = (String) map.get("classStr");
                Plugin plugin = (Plugin) Plugin.createInstance((Class<? extends Plugin>) Class.forName(classStr), "null", "null");
                plugin.deserialize(map);

                pluginsList.add(plugin);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return pluginsList;
    }

    default void mergePlugins(List<Plugin> deserializesPlugins) {
        for (Plugin deserializesPlugin : deserializesPlugins) {
            Optional<Plugin> plugin = getApplicationManager().
                    getPluginsManager().
                    getPlugins().
                    stream().
                    filter((p) -> p.getSerializableId().equals(deserializesPlugin.getSerializableId())).
                    findAny();

            List<Group> deserializesGroups = deserializesPlugin.getGroups();
            if (plugin.isPresent()) {
                for (Group deserializesGroup : deserializesGroups) {
                    Optional<Group> group = plugin.get().getGroups().
                            stream().
                            filter((g) -> g.getSerializableId().equals(deserializesGroup.getSerializableId())).
                            findAny();

                    List<Category> deserializesCategories = deserializesGroup.getCategories();
                    if (group.isPresent()) {
                        for (Category deserializesCategory : deserializesCategories) {
                            Optional<Category> category = group.get().getCategories().
                                    stream().
                                    filter((c) -> c.getSerializableId().equals(deserializesCategory.getSerializableId())).
                                    findAny();

                            if (!category.isPresent()) {
                                deserializesCategory.setPlugin(plugin.get());
                                group.get().add(deserializesCategory);
                            }
                        }
                    } else {
                        deserializesGroup.setParent(plugin.get());
                        plugin.get().add(deserializesGroup);
                    }
                }
            } else {
                getApplicationManager().getPluginsManager().add(deserializesPlugin);
            }
        }
    }

    default List<Download> restoreDownloads(List<Map<String, Object>> downloads) {
        List<Download> downloadsList = new ArrayList<>();

        for (Map<String, Object> map : downloads) {
            try {
                String classStr = (String) map.get("classStr");
                Download download = (Download) Class.forName(classStr).newInstance();
                download.deserialize(map);

                Map<String, Object> cmap = (Map<String, Object>) map.get("category");
                String categorySId = (String) cmap.get("serializableId");
                Category category = getApplicationManager().getPluginsManager().findCategoryBy(categorySId);
                download.setCategory(category);


                downloadsList.add(download);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return downloadsList;
    }

    void mergeDownloads(List<Download> downloadsList);

    ApplicationManager getApplicationManager();
}
