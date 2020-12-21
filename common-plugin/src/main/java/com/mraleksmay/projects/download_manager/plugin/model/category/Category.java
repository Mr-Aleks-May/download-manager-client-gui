package com.mraleksmay.projects.download_manager.plugin.model.category;

import com.mraleksmay.projects.download_manager.common.annotation.NotNull;
import com.mraleksmay.projects.download_manager.common.annotation.Serialize;
import com.mraleksmay.projects.download_manager.plugin.model.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public abstract class Category {
    private String csid = "null";
    private String id;
    private String name;
    private String contentType;
    private String extensions;
    private String outputDirStr;
    private String comments;
    private Plugin plugin;
    private boolean isEditable;
    private boolean isRemovable;


    // Constructors
    public Category() {
    }

    public Category(@NotNull final String id,
                    @NotNull final String name,
                    @NotNull final String contentType,
                    @NotNull final String extensions,
                    @NotNull final File outputDir,
                    @NotNull final Plugin plugin) throws IOException {
        this(id, name, contentType, extensions, outputDir, "", plugin);
    }

    public Category(@NotNull final String id,
                    @NotNull final String name,
                    @NotNull final String contentType,
                    @NotNull final String extensions,
                    @NotNull final File outputDir,
                    @NotNull final String comments,
                    @NotNull final Plugin plugin) throws IOException {
        this.csid = id.toUpperCase().trim();
        this.id = id.toUpperCase().trim();
        this.name = name.trim();
        this.contentType = contentType.trim();
        this.extensions = extensions.trim();
        this.outputDirStr = outputDir.getCanonicalPath();
        this.comments = comments.trim();
        this.plugin = plugin;
    }


    public abstract File getTempDir() throws IOException;


    public String getCSID() {
        return csid;
    }

    public Category setCSID(String csid) {
        this.csid = csid;
        return this;
    }

    public String getId() {
        return id;
    }

    public Category setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Category setName(String name) {
        this.name = name;
        return this;
    }

    public String getContentType() {
        return contentType;
    }

    public Category setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public String getExtensions() {
        return extensions;
    }

    public Category setExtensions(String extensions) {
        this.extensions = extensions;
        return this;
    }

    public File getOutputDir() {
        return new File(outputDirStr);
    }

    public Category setOutputDir(File outputDir) {
        try {
            this.outputDirStr = outputDir.getCanonicalPath();
            return this;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getComments() {
        return comments;
    }

    public Category setComments(String comments) {
        this.comments = comments;
        return this;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public Category setPlugin(Plugin plugin) {
        this.plugin = plugin;
        return this;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public Category setEditable(boolean editable) {
        isEditable = editable;
        return this;
    }

    public boolean isRemovable() {
        return isRemovable;
    }

    public Category setRemovable(boolean removable) {
        isRemovable = removable;
        return this;
    }


    @Override
    public String toString() {
        return name;
    }
}
