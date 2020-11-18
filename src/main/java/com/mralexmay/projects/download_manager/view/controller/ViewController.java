package com.mralexmay.projects.download_manager.view.controller;

import com.mralexmay.projects.download_manager.controller.DownloadsController;
import com.mralexmay.projects.download_manager.core.annotation.NotNull;
import com.mralexmay.projects.download_manager.core.exception.UpdateThreadAlreadyRunningException;
import com.mralexmay.projects.download_manager.core.model.download.category.Category;
import com.mralexmay.projects.download_manager.core.model.download.group.Group;
import com.mralexmay.projects.download_manager.model.download.local_group.CoreDownloadsFilter;
import com.mralexmay.projects.download_manager.model.user.User;
import com.mralexmay.projects.download_manager.view.component.table.controller.TableController;
import com.mralexmay.projects.download_manager.view.component.tree.controller.TreeController;
import com.mralexmay.projects.download_manager.view.dialogs.download.controller.UIUpdater;
import com.mralexmay.projects.download_manager.controller.LocalGroupsController;
import com.mralexmay.projects.download_manager.model.download.category.CoreCategory;
import com.mralexmay.projects.download_manager.model.download.group.CoreGroup;
import com.mralexmay.projects.download_manager.model.download.local_group.CoreLocalGroup;
import com.mralexmay.projects.download_manager.core.model.download.local_group.LocalGroup;
import com.mralexmay.projects.download_manager.view.MainFrame;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ViewController {
    private final UIUpdater uiUpdater;
    private final DownloadsController dController;
    private final LocalGroupsController gController;
    private final TableController tController;
    private final TreeController trController;


    public ViewController(@NotNull MainFrame mainFrame) {
        gController = new LocalGroupsController();
        dController = new DownloadsController();

        tController = new TableController(mainFrame.getTable(), gController);
        tController.initTable();
        trController = new TreeController(mainFrame.getTree(), gController);
        trController.initTree();

        try {
            initView();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        uiUpdater = new UIUpdater(dController, tController, trController, gController);

        try {
            uiUpdater.startUpdate(User.getCurrentUser().getSettings().getUISettings().getUpdateUIMillis());
        } catch (UpdateThreadAlreadyRunningException e) {
            e.printStackTrace();
        }
    }


    public void initView() throws IOException {
        final LocalGroup localGroups = initCoreLocalGroups();

        gController.add(localGroups);
        trController.add(localGroups);

        trController.setAndSelectCategoryByDefault();
    }

    private LocalGroup initCoreLocalGroups() throws IOException {
        final LocalGroup coreLocalGroups = new CoreLocalGroup("CORE", "CORE", new CoreDownloadsFilter(getLocalGroupsController()));
        final Group all = new CoreGroup("ALL", "All", coreLocalGroups);
        final Group unfinished = new CoreGroup("UNFINISHED", "Unfinished", coreLocalGroups);
        final Group completed = new CoreGroup("COMPLETED", "Completed", coreLocalGroups);
        final String userHomeDir = System.getenv("HOME");

        coreLocalGroups.add(all);
        coreLocalGroups.add(unfinished);
        coreLocalGroups.add(completed);

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
        category = new CoreCategory(id, name, contentType, extensions, outputDir, coreLocalGroups);
        categories.add(category);

        // CATEGORY APPLICATION
        id = "APPLICATION";
        name = "Applications";
        contentType = "application";
        extensions = "exe";
        tmpDir = new File(userHomeDir + "/Downloader/Applications/tmp");
        outputDir = new File(userHomeDir + "/Downloader/Applications");
        category = new CoreCategory(id, name, contentType, extensions, outputDir, coreLocalGroups);
        categories.add(category);

        // CATEGORY COMPRESSED
        id = "COMPRESSED";
        name = "Compressed";
        contentType = "compressed";
        extensions = "zip";
        tmpDir = new File(userHomeDir + "/Downloader/Compressed/tmp");
        outputDir = new File(userHomeDir + "/Downloader/Compressed");
        category = new CoreCategory(id, name, contentType, extensions, outputDir, coreLocalGroups);
        categories.add(category);

        // CATEGORY DOCUMENTS
        id = "DOCUMENTS";
        name = "Documents";
        contentType = "text";
        extensions = "pdf";
        tmpDir = new File(userHomeDir + "/Downloader/Documents/tmp");
        outputDir = new File(userHomeDir + "/Downloader/Documents");
        category = new CoreCategory(id, name, contentType, extensions, outputDir, coreLocalGroups);
        categories.add(category);

        // CATEGORY PHOTOS
        id = "PHOTOS";
        name = "Photos";
        contentType = "image";
        extensions = "jpg";
        tmpDir = new File(userHomeDir + "/Downloader/Photos/tmp");
        outputDir = new File(userHomeDir + "/Downloader/Photos");
        category = new CoreCategory(id, name, contentType, extensions, outputDir, coreLocalGroups);
        categories.add(category);

        // CATEGORY VIDEO
        id = "VIDEO";
        name = "Videos";
        contentType = "video";
        extensions = "mp4";
        tmpDir = new File(userHomeDir + "/Downloader/Videos/tmp");
        outputDir = new File(userHomeDir + "/Downloader/Videos");
        category = new CoreCategory(id, name, contentType, extensions, outputDir, coreLocalGroups);
        categories.add(category);

        // CATEGORY MUSIC
        id = "MUSIC";
        name = "Music";
        contentType = "music";
        extensions = "mp3";
        tmpDir = new File(userHomeDir + "/Downloader/Music/tmp").getCanonicalFile();
        outputDir = new File(userHomeDir + "/Downloader/Music");
        category = new CoreCategory(id, name, contentType, extensions, outputDir, coreLocalGroups);
        categories.add(category);

        // CATEGORY OTHER
        id = "OTHER";
        name = "Other";
        contentType = "*";
        extensions = "*";
        tmpDir = new File(userHomeDir + "/Downloader/Other/tmp");
        outputDir = new File(userHomeDir + "/Downloader/Other");
        category = new CoreCategory(id, name, contentType, extensions, outputDir, coreLocalGroups);
        categories.add(category);


        for (Category categ0ry : categories) {
            for (Group group : coreLocalGroups.getGroups()) {
                group.add(categ0ry);
            }
        }

        return coreLocalGroups;
    }


    public UIUpdater getUiUpdater() {
        return uiUpdater;
    }

    public DownloadsController getDownloadsController() {
        return dController;
    }

    public LocalGroupsController getLocalGroupsController() {
        return gController;
    }

    public TableController getTableController() {
        return tController;
    }

    public TreeController getTreeController() {
        return trController;
    }
}
