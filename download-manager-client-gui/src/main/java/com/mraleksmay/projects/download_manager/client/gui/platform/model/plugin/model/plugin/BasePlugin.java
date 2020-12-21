package com.mraleksmay.projects.download_manager.client.gui.platform.model.plugin.model.plugin;

import com.mraleksmay.projects.download_manager.client.gui.platform.model.plugin.model.category.BaseCategory;
import com.mraleksmay.projects.download_manager.client.gui.platform.model.plugin.model.group.BaseGroup;
import com.mraleksmay.projects.download_manager.client.gui.platform.model.plugin.view.dialog.AddCommonDownloadDialog;
import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.communication.service.user.mapper.BaseSCategoryMapper;
import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.communication.service.user.mapper.BaseSDownloadMapper;
import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.io.save_load.model.mapper.category.BaseCategoryMapper;
import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.io.save_load.model.mapper.download.BaseDownloadMapper;
import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.io.save_load.model.mapper.group.BaseGroupMapper;
import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.io.save_load.model.mapper.plugin.BasePluginMapper;
import com.mraleksmay.projects.download_manager.plugin.io.mapper.category.CategoryMapper;
import com.mraleksmay.projects.download_manager.plugin.io.mapper.category.SCategoryMapper;
import com.mraleksmay.projects.download_manager.plugin.io.mapper.download.DownloadMapper;
import com.mraleksmay.projects.download_manager.plugin.io.mapper.download.SDownloadMapper;
import com.mraleksmay.projects.download_manager.plugin.io.mapper.group.GroupMapper;
import com.mraleksmay.projects.download_manager.plugin.io.mapper.plugin.PluginMapper;
import com.mraleksmay.projects.download_manager.plugin.model.category.Category;
import com.mraleksmay.projects.download_manager.plugin.model.group.Group;
import com.mraleksmay.projects.download_manager.plugin.model.plugin.Plugin;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;


public class BasePlugin extends Plugin {

    {
        setName("Base name");
        setVersion("1.0");
        setAuthor("alexmy");

        setDefPluginClass(BasePlugin.class);
        setDefGroupClass(BaseGroup.class);
        setDefCategoryClass(BaseCategory.class);

        add("[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}");
        setPriority(-9999);
    }


    public BasePlugin() {
        this("CORE", "CORE");
    }

    private BasePlugin(String id, String name) {
        super(id, name);
        setPSID("PLUGIN-COMMON-" + id);
    }


    @Override
    public Plugin init() throws IOException {
        final Plugin plugin = this;
        plugin.setUserCanDownloadLater(true);

        final Group all = new BaseGroup("ALL", "All", plugin);
        final Group unfinished = new BaseGroup("UNFINISHED", "Unfinished", plugin);
        final Group completed = new BaseGroup("COMPLETED", "Completed", plugin);
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
        category = new BaseCategory(id, name, contentType, extensions, outputDir, plugin);
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
        category = new BaseCategory(id, name, contentType, extensions, outputDir, plugin);
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
        category = new BaseCategory(id, name, contentType, extensions, outputDir, plugin);
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
        category = new BaseCategory(id, name, contentType, extensions, outputDir, plugin);
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
        category = new BaseCategory(id, name, contentType, extensions, outputDir, plugin);
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
        category = new BaseCategory(id, name, contentType, extensions, outputDir, plugin);
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
        category = new BaseCategory(id, name, contentType, extensions, outputDir, plugin);
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
        category = new BaseCategory(id, name, contentType, extensions, outputDir, plugin);
        category.setEditable(false);
        category.setRemovable(false);
        categories.add(category);


        for (Category categ0ry : categories) {
            for (Group group : plugin.getGroups()) {
                group.add(categ0ry);
            }
        }

        return this;
    }

    @Override
    public boolean canDeserialize(String psid, String version) {
        return false;
    }

    @Override
    public boolean canDeserializeIgnoreVersion(String psid) {
        return false;
    }

    @Override
    public JDialog getAddDownloadDialogWindow(Class<?>[] constructorArgsType, Object[] constructorParams) throws Exception {
        final Class<? extends JDialog> dialogClass = this.getAddDownloadDialogWindowClass();
        final Constructor<? extends JDialog> dialogConstructor = dialogClass.getDeclaredConstructor(constructorArgsType);
        final JDialog dialog = dialogConstructor.newInstance(constructorParams);

        return dialog;
    }

    @Override
    public Class<? extends JDialog> getAddDownloadDialogWindowClass() {
        return AddCommonDownloadDialog.class;
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

    @Override
    public Class<? extends Plugin> getPluginClass() {
        return this.getClass();
    }

    @Override
    public PluginMapper getPluginMapper() {
        return new BasePluginMapper();
    }

    @Override
    public GroupMapper getGroupMapper() {
        return new BaseGroupMapper();
    }

    @Override
    public CategoryMapper getCategoryMapper() {
        return new BaseCategoryMapper();
    }

    @Override
    public DownloadMapper getDownloadMapper() {
        return new BaseDownloadMapper();
    }

    @Override
    public SDownloadMapper getSDownloadMapper() {
        return new BaseSDownloadMapper(new BaseSCategoryMapper());
    }

    @Override
    public SCategoryMapper getSCategoryMapper() {
        return new BaseSCategoryMapper();
    }

    @Override
    public Group getDefGroup() {
        return new BaseGroup();
    }

    @Override
    public Category getDefCategory() {
        return new BaseCategory();
    }
}
