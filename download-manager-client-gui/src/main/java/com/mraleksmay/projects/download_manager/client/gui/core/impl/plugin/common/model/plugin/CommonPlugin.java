package com.mraleksmay.projects.download_manager.client.gui.core.impl.plugin.common.model.plugin;

import com.mraleksmay.projects.download_manager.client.gui.core.impl.plugin.common.model.category.CommonCategory;
import com.mraleksmay.projects.download_manager.client.gui.core.impl.plugin.common.model.group.CommonGroup;
import com.mraleksmay.projects.download_manager.client.gui.core.impl.plugin.common.view.dialog.AddCommonDownloadDialog;
import com.mraleksmay.projects.download_manager.common.model.category.Category;
import com.mraleksmay.projects.download_manager.common.model.group.Group;
import com.mraleksmay.projects.download_manager.common.model.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CommonPlugin extends Plugin {
    {
        addSupportedWebSiteRegex("[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}");
        setPluginPriority(-9999);
    }


    public CommonPlugin() {
        this("CORE", "CORE");
    }

    private CommonPlugin(String id, String name) {
        super(id, name);
        setSerializableId("PLUGIN-COMMON-" + id);
    }


    @Override
    public void init() throws IOException {
        final Plugin plugin = this;
        plugin.setUserCanDownloadLater(true);
        plugin.setDownloadDialogClass(AddCommonDownloadDialog.class);

        final Group all = new CommonGroup("ALL", "All", plugin);
        final Group unfinished = new CommonGroup("UNFINISHED", "Unfinished", plugin);
        final Group completed = new CommonGroup("COMPLETED", "Completed", plugin);
        final String userHomeDir = System.getenv("HOME");

        plugin.add(all);
        plugin.add(unfinished);
        plugin.add(completed);

        final List<Category> categories = new ArrayList<>();

        String id, name, contentType, extensions;
        File tmpDir, outputDir;
        Category category;

        // CATEGORY ALL
        id = "ALL";
        name = "All";
        contentType = "all";
        extensions = "";
        tmpDir = new File("");
        outputDir = new File("");
        category = new CommonCategory(id, name, contentType, extensions, outputDir, plugin);
        category.setEditable(false);
        category.setRemovable(false);
        categories.add(category);

        // CATEGORY APPLICATION
        id = "APPLICATION";
        name = "Applications";
        contentType = "application";
        extensions = "exe";
        tmpDir = new File(userHomeDir + "/Downloader/Applications/tmp");
        outputDir = new File(userHomeDir + "/Downloader/Applications");
        category = new CommonCategory(id, name, contentType, extensions, outputDir, plugin);
        category.setEditable(true);
        category.setRemovable(true);
        categories.add(category);

        // CATEGORY COMPRESSED
        id = "COMPRESSED";
        name = "Compressed";
        contentType = "compressed";
        extensions = "zip";
        tmpDir = new File(userHomeDir + "/Downloader/Compressed/tmp");
        outputDir = new File(userHomeDir + "/Downloader/Compressed");
        category = new CommonCategory(id, name, contentType, extensions, outputDir, plugin);
        category.setEditable(true);
        category.setRemovable(true);
        categories.add(category);

        // CATEGORY DOCUMENTS
        id = "DOCUMENTS";
        name = "Documents";
        contentType = "text";
        extensions = "pdf";
        tmpDir = new File(userHomeDir + "/Downloader/Documents/tmp");
        outputDir = new File(userHomeDir + "/Downloader/Documents");
        category = new CommonCategory(id, name, contentType, extensions, outputDir, plugin);
        category.setEditable(true);
        category.setRemovable(true);
        categories.add(category);

        // CATEGORY PHOTOS
        id = "PHOTOS";
        name = "Photos";
        contentType = "image";
        extensions = "jpg";
        tmpDir = new File(userHomeDir + "/Downloader/Photos/tmp");
        outputDir = new File(userHomeDir + "/Downloader/Photos");
        category = new CommonCategory(id, name, contentType, extensions, outputDir, plugin);
        category.setEditable(true);
        category.setRemovable(true);
        categories.add(category);

        // CATEGORY VIDEO
        id = "VIDEO";
        name = "Videos";
        contentType = "video";
        extensions = "mp4";
        tmpDir = new File(userHomeDir + "/Downloader/Videos/tmp");
        outputDir = new File(userHomeDir + "/Downloader/Videos");
        category = new CommonCategory(id, name, contentType, extensions, outputDir, plugin);
        category.setEditable(true);
        category.setRemovable(true);
        categories.add(category);

        // CATEGORY MUSIC
        id = "MUSIC";
        name = "Music";
        contentType = "music";
        extensions = "mp3";
        tmpDir = new File(userHomeDir + "/Downloader/Music/tmp").getCanonicalFile();
        outputDir = new File(userHomeDir + "/Downloader/Music");
        category = new CommonCategory(id, name, contentType, extensions, outputDir, plugin);
        category.setEditable(true);
        category.setRemovable(true);
        categories.add(category);

        // CATEGORY OTHER
        id = "OTHER";
        name = "Other";
        contentType = "*";
        extensions = "*";
        tmpDir = new File(userHomeDir + "/Downloader/Other/tmp");
        outputDir = new File(userHomeDir + "/Downloader/Other");
        category = new CommonCategory(id, name, contentType, extensions, outputDir, plugin);
        category.setEditable(false);
        category.setRemovable(false);
        categories.add(category);


        for (Category categ0ry : categories) {
            for (Group group : plugin.getGroups()) {
                group.add(categ0ry);
            }
        }
    }


    @Override
    public Category getDefaultCategory() {
        for (Group group : this.getGroups()) {
            if ("ALL".equalsIgnoreCase(group.getId())) {
                for (Category category : group.getCategories()) {
                    if ("OTHER".equalsIgnoreCase(category.getId())) {
                        return category;
                    }
                }
            }
        }

        return null;
    }
}
