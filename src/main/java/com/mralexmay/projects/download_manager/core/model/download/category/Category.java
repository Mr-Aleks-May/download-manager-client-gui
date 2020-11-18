package com.mralexmay.projects.download_manager.core.model.download.category;

import com.mralexmay.projects.download_manager.core.annotation.NotNull;
import com.mralexmay.projects.download_manager.core.model.download.local_group.LocalGroup;

import java.io.File;
import java.io.IOException;

public interface Category {
    String getId();

    void setId(@NotNull String name);

    String getName();

    void setName(@NotNull String name);

    String getContentType();

    void setContentType(@NotNull final String contentTypeStr);

    String getExtensions();

    void setExtensions(@NotNull final String extensionsStr);

    File getTmpDir() throws IOException;

    File getOutputDir() throws IOException;

    void setOutputDir(@NotNull final File outputDir);

    String getComments();

    void setComments(@NotNull final String commentStr);

    LocalGroup getParent();

    boolean isEditable();

    boolean isRemovable();
}
