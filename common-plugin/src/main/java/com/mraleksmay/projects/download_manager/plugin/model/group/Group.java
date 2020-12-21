package com.mraleksmay.projects.download_manager.plugin.model.group;

import com.mraleksmay.projects.download_manager.common.annotation.NotNull;
import com.mraleksmay.projects.download_manager.common.annotation.Serialize;
import com.mraleksmay.projects.download_manager.plugin.model.plugin.Plugin;
import com.mraleksmay.projects.download_manager.plugin.model.category.Category;

import java.util.ArrayList;
import java.util.List;

public abstract class Group {
    @Serialize
    private String gsid;
    private String id;
    @Serialize
    private String name;
    @Serialize
    private List<Category> categories = new ArrayList<>();
    private Plugin parent;


    // Constructors
    public Group() {
    }

    public Group(@NotNull final String id,
                 @NotNull final String name,
                 @NotNull final Plugin parent) {
        this.gsid = id.toUpperCase();
        this.id = id.toUpperCase();
        this.name = name;
        this.parent = parent;
    }

    public boolean add(Category category) {
        return categories.add(category);
    }

    public synchronized void remove(@NotNull final Category category) {
        categories.remove(category);
    }


    public String getGSID() {
        return gsid;
    }

    public Group setGSID(String gsid) {
        this.gsid = gsid;
        return this;
    }

    public String getId() {
        return id;
    }

    public Group setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Group setName(String name) {
        this.name = name;
        return this;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public Group setCategories(List<Category> categories) {
        this.categories = categories;
        return this;
    }

    public Plugin getParent() {
        return parent;
    }

    public Group setParent(Plugin parent) {
        this.parent = parent;
        return this;
    }


    @Override
    public String toString() {
        return "Group{" +
                "gsid='" + gsid + '\'' +
                '}';
    }
}
