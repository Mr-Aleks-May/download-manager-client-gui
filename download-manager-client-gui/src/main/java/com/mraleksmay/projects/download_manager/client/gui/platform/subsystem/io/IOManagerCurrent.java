package com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mraleksmay.projects.download_manager.client.gui.platform.model.download.DownloadEntityInformation;
import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.ApplicationManager;
import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.downloads.DownloadsManager;
import com.mraleksmay.projects.download_manager.client.gui.platform.util.downloader.BasicNetWorker;
import com.mraleksmay.projects.download_manager.plugin.io.dto.category.CategoryDto;
import com.mraleksmay.projects.download_manager.plugin.io.dto.download.DownloadDto;
import com.mraleksmay.projects.download_manager.plugin.io.dto.group.GroupDto;
import com.mraleksmay.projects.download_manager.common.util.file.FileWorker;
import com.mraleksmay.projects.download_manager.plugin.io.dto.plugin.PluginDto;
import com.mraleksmay.projects.download_manager.plugin.model.category.Category;
import com.mraleksmay.projects.download_manager.plugin.model.download.Download;
import com.mraleksmay.projects.download_manager.plugin.model.group.Group;
import com.mraleksmay.projects.download_manager.plugin.model.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toUnmodifiableList;

public class IOManagerCurrent {
    private static final String PLUGINS_SECTION_LABEL = "--PLUGINS--";
    private static final String DOWNLOADS_SECTION_LABEL = "--DOWNLOADS--";
    private static final String ITEMS_DELIMITER = "||";
    private static final String START_SECTION_LABEL = "[";
    private static final String END_SECTION_LABEL = "]";
    private static final String START_SECTION = "START--";
    private static final String END_SECTION = "END--";
    private final ApplicationManager applicationManager;


    public IOManagerCurrent(ApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
    }


    public void save(File selectedFile) throws IOException {
        final StringBuilder save = new StringBuilder();
        final List<PluginDto> plugins = getApplicationManager().getPluginsManager().getPlugins().stream()
                .map(p -> p.getPluginMapper().toDto(p))
                .collect(toUnmodifiableList());
        final List<DownloadDto> downloads = getApplicationManager().getDownloadsManager().getDownloads().stream()
                .map((d) -> d.getDownload().getCategory().getPlugin().getDownloadMapper().toDto(d.getDownload()))
                .collect(toUnmodifiableList());

        save.append(PluginWorker.IO.save(plugins));
        save.append(DownloadWorker.IO.save(downloads));


        FileWorker.writeContentToFile(selectedFile, save);
    }


    public void restore(File selectedFile) throws IOException {
        StringBuilder content = FileWorker.readFileContent(selectedFile);
        List<PluginDto> pluginsDto = PluginWorker.IO.restore(content);
        PluginWorker.Merge.mergePlugins(pluginsDto, getApplicationManager().getPluginsManager().getPlugins());

        List<DownloadDto> downloadsDto = DownloadWorker.IO.restore(content);
        DownloadWorker.Merge.mergerDownloads(downloadsDto, getApplicationManager());
    }

    public static class PluginWorker {
        public static class IO {
            public static StringBuilder save(List<PluginDto> plugins) {
                final StringBuilder save = new StringBuilder();
                save.append(PLUGINS_SECTION_LABEL.trim() + START_SECTION + System.lineSeparator());

                int counter = 0;
                for (PluginDto plugin : plugins) {
                    save.append(counter++ + "__");
                    save.append(save(plugin));
                    save.append(ITEMS_DELIMITER + System.lineSeparator());
                }
                save.append(PLUGINS_SECTION_LABEL.trim() + END_SECTION + System.lineSeparator());

                return save;
            }

            public static StringBuilder save(PluginDto pluginDto) {
                final StringBuilder save = new StringBuilder();
                final Gson gson = new GsonBuilder().create();

                String groupsJson = gson.toJson(pluginDto.getGroups());

                save.append(pluginDto.getPSID() + "__");
                save.append(pluginDto.getVersion() + ":");
                save.append(groupsJson);

                return save;
            }

            public static List<PluginDto> restore(StringBuilder content) {
                List<PluginDto> pluginsDto = new ArrayList<>();

                String pluginsContent = content.substring(
                        content.indexOf(PLUGINS_SECTION_LABEL + START_SECTION) + (PLUGINS_SECTION_LABEL.length() + START_SECTION.length()),
                        content.indexOf(PLUGINS_SECTION_LABEL + END_SECTION)
                );

                Gson gson = new GsonBuilder().create();
                Pattern pattern = Pattern.compile("[\\d]+__([\\w-_\\s\\d]+)__([\\d.\\w]+):([\\d\\D]+)", Pattern.MULTILINE);
                for (int toIndex = 0, fromIndex = 0;
                     (toIndex = pluginsContent.indexOf(ITEMS_DELIMITER, fromIndex)) >= 0;
                     fromIndex = toIndex + ITEMS_DELIMITER.length()) {

                    Matcher matcher = pattern.matcher(pluginsContent.substring(fromIndex, toIndex));

                    if (matcher.find()) {
                        String psid = matcher.group(1);
                        String version = matcher.group(2);
                        String groupsJson = matcher.group(3);

                        List<GroupDto> groupsDto = (List<GroupDto>) gson.fromJson(groupsJson, new TypeToken<List<GroupDto>>() {
                        }.getType());
                        PluginDto pluginDto = new PluginDto()
                                .setPSID(psid)
                                .setVersion(version)
                                .setGroups(groupsDto);

                        pluginsDto.add(pluginDto);
                    }
                }

                return pluginsDto;
            }
        }

        public static class Merge {
            protected static Map<com.mraleksmay.projects.download_manager.plugin.model.plugin.Plugin, PluginDto> findExistedPlugins(List<PluginDto> pluginsDto, List<com.mraleksmay.projects.download_manager.plugin.model.plugin.Plugin> plugins) {
                Map<com.mraleksmay.projects.download_manager.plugin.model.plugin.Plugin, PluginDto> pluginsMap = new HashMap<>();

                for (PluginDto pluginDto : pluginsDto) {
                    Optional<com.mraleksmay.projects.download_manager.plugin.model.plugin.Plugin> plugin = plugins.stream()
                            .filter((p) -> pluginDto.getPSID().equals(p.getPSID()))
                            .findAny();

                    if (plugin.isPresent()) {
                        pluginsMap.putIfAbsent(plugin.get(), pluginDto);
                    }
                }

                return pluginsMap;
            }

            public static void mergePlugins(List<PluginDto> pluginsDto, List<com.mraleksmay.projects.download_manager.plugin.model.plugin.Plugin> plugins) {
                Map<Plugin, PluginDto> pluginsMap = findExistedPlugins(pluginsDto, plugins);

                for (Plugin plugin : pluginsMap.keySet()) {
                    mergerPlugin(plugin, pluginsMap.get(plugin));
                }
            }

            protected static void mergerPlugin(Plugin plugin, PluginDto pluginDto) {
                for (GroupDto groupDto : pluginDto.getGroups()) {
                    for (CategoryDto categoryDto : groupDto.getCategories()) {
                        // Find category in first group
                        Optional<Category> category = plugin.getGroups().get(0).getCategories().stream()
                                .filter((c) -> categoryDto.getCSID().equals(c.getCSID()))
                                .findAny();

                        // Add category to all groups
                        if (!category.isPresent()) {
                            Category newCategory = plugin.getCategoryMapper().fromDto(categoryDto);

                            for (Group group : plugin.getGroups()) {
                                group.add(newCategory);
                            }
                        }
                    }
                    return;
                }
            }
        }
    }

    public static class DownloadWorker {
        public static class IO {
            public static StringBuilder save(List<DownloadDto> downloadsDto) {
                StringBuilder save = new StringBuilder();
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

                save.append(DOWNLOADS_SECTION_LABEL + START_SECTION + System.lineSeparator());
                save.append(gson.toJson(downloadsDto) + System.lineSeparator());
                save.append(DOWNLOADS_SECTION_LABEL + END_SECTION);

                return save;
            }

            public static List<DownloadDto> restore(StringBuilder content) {
                String downloadsContent = content.substring(
                        content.indexOf(DOWNLOADS_SECTION_LABEL + START_SECTION) + (DOWNLOADS_SECTION_LABEL.length() + START_SECTION.length()),
                        content.indexOf(DOWNLOADS_SECTION_LABEL + END_SECTION)
                ).trim();

                Gson gson = new GsonBuilder().create();
                return gson.fromJson(downloadsContent, new TypeToken<List<DownloadDto>>() {
                }.getType());
            }
        }

        public static class Merge {
            public static void mergerDownloads(List<DownloadDto> downloadsDto, ApplicationManager applicationManager) {
                for (DownloadDto downloadDto : downloadsDto) {
                    mergerDownload(downloadDto, applicationManager);
                }
            }

            public static void mergerDownload(DownloadDto downloadDto, ApplicationManager applicationManager) {
                Optional<Plugin> plugin = applicationManager.getPluginsManager().getPlugins().stream()
                        .filter((p) -> downloadDto.getPluginPSID().equals(p.getPSID()))
                        .findAny();

                if (plugin.isPresent()) {
                    Optional<Category> category = plugin.get().getGroups().get(0).getCategories().stream()
                            .filter((c) -> downloadDto.getCategoryCSID().equals(c.getCSID()))
                            .findAny();

                    if (category.isPresent()) {
                        Download download = plugin.get().getDownloadMapper().fromDto(downloadDto, category.get());
                        applicationManager.getDownloadsManager().addIfNotExists(new DownloadEntityInformation(download, new BasicNetWorker()));
                    }
                }
            }
        }
    }


    public ApplicationManager getApplicationManager() {
        return applicationManager;
    }
}
