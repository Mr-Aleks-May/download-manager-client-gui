package com.mralexmay.projects.download_manager.core.model.download.group;

import com.mralexmay.projects.download_manager.core.annotation.NotNull;
import com.mralexmay.projects.download_manager.core.model.download.category.Category;
import com.mralexmay.projects.download_manager.core.model.download.local_group.LocalGroup;

import java.util.List;

public interface Group {
    void add(@NotNull Category category);

    void remove(@NotNull Category category);

    List<Category> getCategories();

    String getId();

    String getName();

    LocalGroup getParent();
}
