package com.mralexmay.projects.download_manager.model.download.group;

import com.mralexmay.projects.download_manager.core.annotation.NotNull;
import com.mralexmay.projects.download_manager.core.model.download.category.Category;
import com.mralexmay.projects.download_manager.core.model.download.group.Group;
import com.mralexmay.projects.download_manager.core.model.download.local_group.LocalGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CoreGroup implements Group {
    private final String id;
    private String name;
    private List<Category> categories;
    private LocalGroup parent;


    public CoreGroup(@NotNull final String id,
                     @NotNull final String name,
                     @NotNull final LocalGroup parent) {
        this.id = id.toUpperCase();
        this.name = name;
        this.parent = parent;
    }

    @Override
    public synchronized void add(@NotNull final Category category) {
        getCategoriesList().add(category);
    }

    @Override
    public void remove(@NotNull final Category category) {
        getCategoriesList().remove(category);
    }


    private synchronized List<Category> getCategoriesList() {
        if (categories == null) {
            this.categories = new ArrayList<>();
        }

        return categories;
    }

    @Override
    public synchronized List<Category> getCategories() {
        return Collections.unmodifiableList(getCategoriesList());
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public LocalGroup getParent() {
        return parent;
    }

    @Override
    public String toString() {
        return name;
    }
}
