package com.mralexmay.projects.download_manager.controller;

import com.mralexmay.projects.download_manager.core.model.download.category.Category;
import com.mralexmay.projects.download_manager.core.model.download.group.Group;
import com.mralexmay.projects.download_manager.core.model.download.local_group.LocalGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocalGroupsController {
    private List<LocalGroup> localGroups;
    private String currentSelectedLocalGroupId;
    private String currentSelectedGroupId;
    private String currentSelectedCategoryId;


    public void add(LocalGroup localGroups) {
        getLocalGroupsList().add(localGroups);
    }


    public LocalGroup getActiveLocalGroup() {
        for (LocalGroup localGroup : getLocalGroupsList()) {
            if (localGroup.getId().equals(currentSelectedLocalGroupId)) {
                return localGroup;
            }
        }

        return null;
    }

    public Group getActiveGroup() {
        for (LocalGroup localGroup : getLocalGroupsList()) {
            if (localGroup.getId().equals(currentSelectedLocalGroupId)) {
                for (Group group : localGroup.getGroups()) {
                    if (group.getId().equals(currentSelectedGroupId)) {
                        return group;
                    }
                }
            }
        }

        return null;
    }

    public Category getActiveCategory() {
        for (LocalGroup localGroup : getLocalGroupsList()) {
            if (localGroup.getId().equals(currentSelectedLocalGroupId)) {
                for (Group group : localGroup.getGroups()) {
                    if (group.getId().equals(currentSelectedGroupId)) {
                        for (Category category : group.getCategories()) {
                            if (category.getId().equals(currentSelectedCategoryId)) {
                                return category;
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

    public LocalGroup getLocalGroupBy(String localGroupId) {
        localGroupId = localGroupId.toUpperCase();

        for (LocalGroup localGroup : getLocalGroupsList()) {
            if (localGroup.getId().equals(localGroupId)) {
                return localGroup;
            }
        }

        return null;
    }

    public Group getGroupBy(String localGroupId, String groupId) {
        localGroupId = localGroupId.toUpperCase();
        groupId = groupId.toUpperCase();

        for (LocalGroup localGroup : getLocalGroupsList()) {
            if (localGroup.getId().equals(localGroupId)) {
                return localGroup.getGroupBy(groupId);
            }
        }

        return null;
    }

    public Category getCategoryBy(String localGroupId, String groupId, String categoryId) {
        localGroupId = localGroupId.toUpperCase();
        groupId = groupId.toUpperCase();

        for (LocalGroup localGroup : getLocalGroupsList()) {
            if (localGroup.getId().equals(localGroupId)) {
                return localGroup.getCategoryFromGroupBy(groupId, categoryId);
            }
        }

        return null;
    }


    private synchronized List<LocalGroup> getLocalGroupsList() {
        if (localGroups == null) {
            localGroups = new ArrayList<>();
        }

        return localGroups;
    }

    public List<LocalGroup> getLocalGroups() {
        return Collections.unmodifiableList(getLocalGroupsList());
    }

    public String getCurrentSelectedLocalGroupId() {
        return currentSelectedLocalGroupId;
    }

    public void setCurrentSelectedLocalGroupId(String currentSelectedLocalGroupId) {
        this.currentSelectedLocalGroupId = currentSelectedLocalGroupId.toUpperCase();
    }

    public String getCurrentSelectedGroupId() {
        return currentSelectedGroupId;
    }

    public void setCurrentSelectedGroupId(String currentSelectedGroupId) {
        this.currentSelectedGroupId = currentSelectedGroupId.toUpperCase();
    }

    public String getCurrentSelectedCategoryId() {
        return currentSelectedCategoryId;
    }

    public void setCurrentSelectedCategoryId(String currentSelectedCategoryId) {
        this.currentSelectedCategoryId = currentSelectedCategoryId.toUpperCase();
    }
}
