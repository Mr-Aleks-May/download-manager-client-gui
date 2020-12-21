package com.mraleksmay.projects.download_manager.client.gui.platform.model.plugin.model.category;


import com.mraleksmay.projects.download_manager.plugin.model.category.Category;
import com.mraleksmay.projects.download_manager.plugin.model.plugin.Plugin;
import com.mraleksmay.projects.download_manager.plugin.model.user.User;

import java.io.File;
import java.io.IOException;

public class BaseCategory extends Category {
    public BaseCategory() {
    }

    public BaseCategory(String id, String name, String contentType, String extensions, File outputDir, Plugin parent) throws IOException {
        super(id, name, contentType, extensions, outputDir, parent);
        setCSID("PLUGIN-COMMON-CATEGORY-COMMON-" + id);
    }

    public BaseCategory(String id, String name, String contentType, String extensions, File outputDir, String comments, Plugin parent) throws IOException {
        super(id, name, contentType, extensions, outputDir, comments, parent);
        setCSID("PLUGIN-COMMON-CATEGORY-COMMON-" + id);
    }


    @Override
    public File getTempDir() throws IOException {
        return User.getCurrentUser().getSettings().getDownloaderSettings().getTmpDir();
    }

    @Override
    public boolean isEditable() {
        return !getId().equals("ALL");
    }

    @Override
    public boolean isRemovable() {
        return !getId().equals("ALL");
    }
}
