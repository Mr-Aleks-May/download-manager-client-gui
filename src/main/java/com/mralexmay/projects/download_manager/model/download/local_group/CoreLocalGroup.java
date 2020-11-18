package com.mralexmay.projects.download_manager.model.download.local_group;

import com.mralexmay.projects.download_manager.core.annotation.NotNull;
import com.mralexmay.projects.download_manager.core.model.download.category.Category;
import com.mralexmay.projects.download_manager.core.model.download.group.Group;
import com.mralexmay.projects.download_manager.core.model.download.local_group.DownloadsFilter;
import com.mralexmay.projects.download_manager.core.model.download.meta_information.DownloadMetaInfo;
import com.mralexmay.projects.download_manager.core.model.download.local_group.LocalGroup;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CoreLocalGroup implements LocalGroup {
    private final String id;
    private String name;
    private List<Group> groups;
    private DownloadsFilter filter;


    public CoreLocalGroup(@NotNull final String id,
                          @NotNull final String name,
                          @NotNull final DownloadsFilter filter) {
        this.id = id.toUpperCase();
        this.name = name;
        this.filter = filter;
    }


    @Override
    public synchronized void add(@NotNull final Group group) {
        getGroupsList().add(group);
    }

    @Override
    public void remove(@NotNull final Category category) {
        for (Group group : getGroups()) {
            group.remove(category);
        }
    }

    @Override
    public Group getGroupBy(@NotNull String groupId) {
        groupId = groupId.toUpperCase();

        for (Group group : getGroups()) {
            if (group.getId().equals(groupId)) {
                return group;
            }
        }

        return null;
    }

    @Override
    public Category getCategoryFromGroupBy(@NotNull String groupId,
                                           @NotNull String categoryId) {
        groupId = groupId.toUpperCase();
        categoryId = categoryId.toUpperCase();

        for (Group group : getGroups()) {
            if (group.getId().equals(groupId)) {
                for (Category category : group.getCategories()) {
                    if (category.getId().equals(categoryId)) {
                        return category;
                    }
                }
            }
        }

        return null;
    }


    @Override
    public synchronized Category getCategoryByType(@NotNull final String contentType) {
        for (Group group : getGroups()) {
            if (group.getId().equals("UNFINISHED")) {
                for (Category category : group.getCategories()) {
                    Pattern pattern = Pattern.compile(category.getContentType(), Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(contentType);

                    if (matcher.find()) {
                        return category;
                    }
                }
            }
        }

        return null;
    }

    @Override
    public synchronized Category getCategoryByExtension(@NotNull final String extension) {
        for (Group group : getGroups()) {
            if (group.getId().equals("UNFINISHED")) {
                for (Category category : group.getCategories()) {
                    Pattern pattern = Pattern.compile(category.getExtensions(), Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(extension);

                    if (matcher.find()) {
                        return category;
                    }
                }
            }
        }

        return null;
    }

    @Override
    public synchronized Category tryDeterminateCategoryBy(@NotNull final URL url,
                                                          @NotNull final String nameStr,
                                                          @NotNull final String contentType) {
        Category category = null;

        category = getCategoryByType(contentType);
        if (category == null) {
            int dotIndex = nameStr.lastIndexOf(".");
            String extension = nameStr.substring(dotIndex + 1);
            category = getCategoryByExtension(extension);
        }

        return category;
    }

    @Override
    public List<DownloadMetaInfo> filterDownloads(@NotNull final List<DownloadMetaInfo> downloads) {
        return filter.getDownloadsToDisplay(downloads);
    }


    protected synchronized List<Group> getGroupsList() {
        if (groups == null) {
            groups = new ArrayList<>();
        }

        return groups;
    }

    @Override
    public synchronized List<Group> getGroups() {
        return Collections.unmodifiableList(getGroupsList());
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
    public DownloadsFilter getFilter() {
        return filter;
    }

    @Override
    public void setFilter(@NotNull final DownloadsFilter filter) {
        this.filter = filter;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass().equals(this.getClass())) {
            LocalGroup o1 = (LocalGroup) o;
            LocalGroup o2 = this;

            return o1.getId().equals(o2.getId());
        }

        return super.equals(o);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
