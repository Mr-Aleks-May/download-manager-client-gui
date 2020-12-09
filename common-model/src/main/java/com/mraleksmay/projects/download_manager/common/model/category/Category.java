package com.mraleksmay.projects.download_manager.common.model.category;

import com.google.gson.annotations.Expose;
import com.mraleksmay.projects.download_manager.common.annotation.NotNull;
import com.mraleksmay.projects.download_manager.common.model.Restoreable;
import com.mraleksmay.projects.download_manager.common.model.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

/**
 * Download category class.
 */
public abstract class Category implements Serializable, Restoreable {

    /**
     * Unique category identifier used during serialization.
     */
    @Expose
    private String serializableId = "null";
    /**
     * Unique category identifier.
     */
    @Expose
    private String id;
    /**
     * Category name.
     */
    @Expose
    private String name;
    /**
     * Category content type.
     */
    @Expose
    private String contentType;
    /**
     * File extensions specific to this category.
     */
    @Expose
    private String extensions;
    /**
     * Save download directory.
     */
    @Expose
    private String outputDirStr;
    /**
     * Category comments.
     */
    @Expose
    private String comments;
    /**
     * Plugin this category is a part of
     */
    private Plugin plugin;
    /**
     * The class of a specific implementation of this category. Used when deserializing.
     */
    @Expose
    private String classStr;
    /**
     * Is user can edit this category.
     */
    private boolean isEditable;
    /**
     * Whether the user can delete this category.
     */
    private boolean isRemovable;


    {
        setClazz(this.getClass());
    }


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
        this.id = id.toUpperCase().trim();
        this.name = name.trim();
        this.contentType = contentType.trim();
        this.extensions = extensions.trim();
        this.outputDirStr = outputDir.getCanonicalPath();
        this.comments = comments.trim();
        this.plugin = plugin;
    }


    @Override
    public String serialize() {
        return null;
    }

    @Override
    public void deserialize(Object cobj) throws IOException {
        Category category = this;
        Map<String, Object> cmap = (Map<String, Object>) cobj;

        category.setSerializableId((String) cmap.get("serializableId"));
        category.setId((String) cmap.get("id"));
        category.setName((String) cmap.get("name"));
        category.setContentType((String) cmap.get("contentType"));
        category.setExtensions((String) cmap.get("extensions"));
        category.setOutputDir(new File((String) cmap.get("outputDirStr")));
        category.setComments((String) cmap.get("comments"));
    }


    // Getters and Setters
    public String getSerializableId() {
        return serializableId;
    }

    public void setSerializableId(String serializableId) {
        this.serializableId = serializableId;
    }

    public String getId() {
        return id;
    }

    public void setId(@NotNull String id) {
        this.id = id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name.trim();
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType.trim();
    }

    public String getExtensions() {
        return extensions;
    }

    public void setExtensions(@NotNull String extensions) {
        this.extensions = extensions.trim();
    }

    public abstract File getTmpDir() throws IOException;

    public File getOutputDir() throws IOException {
        return new File(outputDirStr).getCanonicalFile();
    }

    public void setOutputDir(@NotNull final File outputDir) throws IOException {
        this.outputDirStr = outputDir.getCanonicalPath();
    }

    public String getComments() {
        return comments;
    }

    public void setComments(@NotNull final String comments) {
        this.comments = comments;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }

    protected Class<? extends Category> getClazz() throws ClassNotFoundException {
        return (Class<? extends Category>) Class.forName(classStr);
    }

    protected void setClazz(Class<? extends Category> clazz) {
        this.classStr = clazz.getCanonicalName();
    }


    public String getClassStr() {
        return classStr;
    }

    public void setClassStr(String classStr) {
        this.classStr = classStr;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public boolean isRemovable() {
        return isRemovable;
    }

    public void setRemovable(boolean removable) {
        isRemovable = removable;
    }

    public void setEditable(boolean editable) {
        this.isEditable = editable;
    }


    @Override
    public String toString() {
        return this.name;
    }
}
