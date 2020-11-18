package com.mralexmay.projects.download_manager.model.download.category;

import com.mralexmay.projects.download_manager.core.annotation.NotNull;
import com.mralexmay.projects.download_manager.core.model.download.category.Category;
import com.mralexmay.projects.download_manager.core.model.download.local_group.LocalGroup;
import com.mralexmay.projects.download_manager.model.user.User;

import java.io.File;
import java.io.IOException;

public class CoreCategory implements Category {
    private final String id;
    private String name;
    private String contentType;
    private String extensions;
    private File outputDir;
    private String comments;
    private LocalGroup parent;


    public CoreCategory(@NotNull final String id,
                        @NotNull final String name,
                        @NotNull final String contentType,
                        @NotNull final String extensions,
                        @NotNull final File outputDir,
                        @NotNull final LocalGroup parent) throws IOException {
        this(id, name, contentType, extensions, outputDir, "", parent);
    }

    public CoreCategory(@NotNull final String id,
                        @NotNull final String name,
                        @NotNull final String contentType,
                        @NotNull final String extensions,
                        @NotNull final File outputDir,
                        @NotNull final String comments,
                        @NotNull final LocalGroup parent) throws IOException {
        this.id = id.toUpperCase().trim();
        this.name = name.trim();
        this.contentType = contentType.trim();
        this.extensions = extensions.trim();
        this.outputDir = outputDir.getCanonicalFile();
        this.comments = comments.trim();
        this.parent = parent;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(@NotNull final String name) {
        this.name = name.trim();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(@NotNull final String name) {
        this.name = name;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public void setContentType(String contentType) {
        this.contentType = contentType.trim();
    }

    @Override
    public String getExtensions() {
        return extensions;
    }

    @Override
    public void setExtensions(@NotNull final String extensions) {
        this.extensions = extensions.trim();
    }

    @Override
    public File getTmpDir() throws IOException {
        return User.getCurrentUser().getSettings().getDownloaderSettings().getTmpDir();
    }

    @Override
    public File getOutputDir() throws IOException {
        return outputDir.getCanonicalFile();
    }

    @Override
    public void setOutputDir(@NotNull final File outputDir) {
        this.outputDir = outputDir;
    }

    @Override
    public String getComments() {
        return comments;
    }

    @Override
    public void setComments(@NotNull final String comments) {
        this.comments = comments;
    }

    @Override
    public LocalGroup getParent() {
        return parent;
    }

    @Override
    public boolean isEditable() {
        return !getId().equals("ALL");
    }

    @Override
    public boolean isRemovable() {
        return !getId().equals("ALL");
    }

    @Override
    public String toString() {
        return this.name;
    }
}
