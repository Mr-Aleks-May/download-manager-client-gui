package com.mraleksmay.projects.download_manager.client.gui.core.manager;

import com.google.gson.Gson;
import com.mraleksmay.projects.download_manager.client.gui.core.impl.model.download.DownloadEntityInformation;
import com.mraleksmay.projects.download_manager.client.gui.core.util.downloader.BasicNetWorker;
import com.mraleksmay.projects.download_manager.common.model.category.Category;
import com.mraleksmay.projects.download_manager.common.model.download.Download;
import com.mraleksmay.projects.download_manager.common.model.group.Group;
import com.mraleksmay.projects.download_manager.common.model.plugin.Plugin;
import com.mraleksmay.projects.download_manager.common.util.file.FileWorker;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class IOManager {
    private ApplicationManager applicationManager;

    public IOManager(ApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
    }


    public void serialize(File pathToFile) throws IOException {
        StringBuilder sb = new StringBuilder();
        com.google.gson.Gson gson = new Gson().
                newBuilder().
                excludeFieldsWithoutExposeAnnotation().
                setPrettyPrinting().
                create();

        String jsonPluginsInfo = gson.toJson(getApplicationManager().getPluginsManager().getPlugins());
        String jsonDownloads = gson.toJson(getApplicationManager().
                getDownloadsManager().
                getDownloads().
                stream().
                map(DownloadEntityInformation::getDownload).
                collect(Collectors.toList()));

        sb.append(jsonPluginsInfo);
        sb.append("\n--DOWNLOADS_INFO\n");
        sb.append(jsonDownloads);

        FileWorker.writeContentToFile(pathToFile, sb.toString());
    }

    public void deserialize(File pathToFile) throws IOException {
        StringBuilder content = FileWorker.readFileContent(pathToFile);
        String pluginJson = content.substring(0, content.indexOf("\n--DOWNLOADS_INFO\n"));
        String downloadJson = content.substring(pluginJson.length() + "\n--DOWNLOADS_INFO\n".length());

        com.google.gson.Gson gson = new Gson().
                newBuilder().
                excludeFieldsWithoutExposeAnnotation().
                setPrettyPrinting().
                create();

        List<Map<String, Object>> plugins = gson.fromJson(pluginJson, List.class);
        List<Map<String, Object>> obj2 = gson.fromJson(downloadJson, List.class);

        List<Plugin> pluginsList = restorePlugin(plugins);
        mergePlugins(pluginsList);

        List<Download> downloadsList = restoreDownloads(obj2);
        mergeDownloads(downloadsList);
    }

    private List<Plugin> restorePlugin(List<Map<String, Object>> plugins) {
        List<Plugin> pluginsList = new ArrayList<>();

        for (Map<String, Object> map : plugins) {
            try {
                String classStr = (String) map.get("classStr");
                Plugin plugin = (Plugin) Plugin.createInstance((Class<? extends Plugin>) Class.forName(classStr), "null", "null");
                plugin.deserialize(map);

                pluginsList.add(plugin);
            } catch (Exception e) {
            }
//            catch (InstantiationException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
//            } catch (InvocationTargetException e) {
//                e.printStackTrace();
//            }
        }

        return pluginsList;
    }

    void mergePlugins(List<Plugin> deserializesPlugins) {
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

    public List<Download> restoreDownloads(List<Map<String, Object>> downloads) {
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

    public void mergeDownloads(List<Download> downloadsList) {
        for (Download download : downloadsList) {
            final DownloadEntityInformation downloadEntityInfo = new DownloadEntityInformation(download, new BasicNetWorker());

            getApplicationManager().getDownloadsManager().addIfNotExists(downloadEntityInfo);
        }
    }

    public ApplicationManager getApplicationManager() {
        return applicationManager;
    }


    class Root {
        List<Plugin> pluginList = new ArrayList<>();

        public void add(Plugin plugin) {
            pluginList.add(plugin);
        }
    }
}
