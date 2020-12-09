package com.mraleksmay.projects.download_manager.common.model.group;


import com.google.gson.annotations.Expose;
import com.mraleksmay.projects.download_manager.common.annotation.NotNull;
import com.mraleksmay.projects.download_manager.common.model.Restoreable;
import com.mraleksmay.projects.download_manager.common.model.category.Category;
import com.mraleksmay.projects.download_manager.common.model.plugin.Plugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * The group the download belongs to.
 */
public abstract class Group implements Restoreable {
    /**
     * Unique group identifier used during serialization.
     */
    @Expose
    private String serializableId = "null";
    /**
     * Group identifier.
     */
    @Expose
    private String id;
    /**
     * Group name.
     */
    @Expose
    private String name;
    /**
     * Contains categories.
     */
    @Expose
    private List<Category> categories;
    /**
     * The plugin the group belongs to.
     */
    private Plugin parent;
    /**
     * The class of a specific implementation of this group. Used when deserializing.
     */
    @Expose
    private String classStr;


    {
        setClassStr(this.getClass());
    }


    // Constructors
    public Group() {
    }

    public Group(@NotNull final String id,
                 @NotNull final String name,
                 @NotNull final Plugin parent) {
        this.id = id.toUpperCase();
        this.name = name;
        this.parent = parent;
    }


    // Methods
    public synchronized void add(@NotNull final Category category) {
        getCategoriesList().add(category);
    }

    public synchronized void remove(@NotNull final Category category) {
        getCategoriesList().remove(category);
    }

    @Override
    public String serialize() {
        return null;
    }

    @Override
    public void deserialize(Object gobj) throws ClassNotFoundException, IllegalAccessException, InstantiationException, IOException {
        Group group = this;
        Map<String, Object> gmap = (Map<String, Object>) gobj;

        group.setSerializableId((String) gmap.get("serializableId"));
        group.setId((String) gmap.get("id"));
        group.setName((String) gmap.get("name"));
    }
    

    // Getters and Setters
    private synchronized List<Category> getCategoriesList() {
        if (categories == null) {
            this.categories = new ArrayList<>();
        }

        return categories;
    }

    public synchronized List<Category> getCategories() {
        return Collections.unmodifiableList(getCategoriesList());
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public String getSerializableId() {
        return serializableId;
    }

    public void setSerializableId(String serializableId) {
        this.serializableId = serializableId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Plugin getParent() {
        return parent;
    }

    public void setParent(Plugin parent) {
        this.parent = parent;
    }

    protected Class<? extends Group> getClazz() throws ClassNotFoundException {
        return (Class<? extends Group>) Class.forName(classStr);
    }

    public String getClassStr() {
        return classStr;
    }

    protected void setClassStr(Class<? extends Group> clazz) {
        this.classStr = clazz.getCanonicalName();
    }

    @Override
    public String toString() {
        return name;
    }
}
