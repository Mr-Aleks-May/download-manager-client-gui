package com.mralexmay.projects.download_manager.core.model.download.local_group;

import com.mralexmay.projects.download_manager.core.annotation.NotNull;
import com.mralexmay.projects.download_manager.core.model.download.category.Category;
import com.mralexmay.projects.download_manager.core.model.download.group.Group;
import com.mralexmay.projects.download_manager.core.model.download.meta_information.DownloadMetaInfo;

import java.net.URL;
import java.util.List;

public interface LocalGroup {
    void add(@NotNull Group group);

    void remove(@NotNull Category category);

    Group getGroupBy(@NotNull String groupId);

    Category getCategoryFromGroupBy(@NotNull String groupId,
                                    @NotNull String categoryId);

    Category getCategoryByType(@NotNull String contentType);

    Category getCategoryByExtension(@NotNull String extension);

    Category tryDeterminateCategoryBy(@NotNull URL url,
                                      @NotNull String nameStr,
                                      @NotNull String contentType);

    List<DownloadMetaInfo> filterDownloads(@NotNull List<DownloadMetaInfo> downloads);

    List<Group> getGroups();

    String getId();

    String getName();

    DownloadsFilter getFilter();

    void setFilter(@NotNull DownloadsFilter filter);

    @Override
    boolean equals(Object o);
}
