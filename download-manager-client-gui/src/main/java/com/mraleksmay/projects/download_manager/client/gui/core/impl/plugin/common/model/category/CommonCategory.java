package com.mraleksmay.projects.download_manager.client.gui.core.impl.plugin.common.model.category;


import com.mraleksmay.projects.download_manager.common.model.category.Category;
import com.mraleksmay.projects.download_manager.common.model.plugin.Plugin;
import com.mraleksmay.projects.download_manager.common.model.user.User;

import java.io.File;
import java.io.IOException;

public class CommonCategory extends Category {

    public CommonCategory() {
    }

    public CommonCategory(String id, String name, String contentType, String extensions, File outputDir, Plugin parent) throws IOException {
        super(id, name, contentType, extensions, outputDir, parent);
        setSerializableId("PLUGIN-COMMON-CATEGORY-COMMON-" + id);
    }

    public CommonCategory(String id, String name, String contentType, String extensions, File outputDir, String comments, Plugin parent) throws IOException {
        super(id, name, contentType, extensions, outputDir, comments, parent);
        setSerializableId("PLUGIN-COMMON-CATEGORY-COMMON-" + id);
    }


    @Override
    public File getTmpDir() throws IOException {
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
