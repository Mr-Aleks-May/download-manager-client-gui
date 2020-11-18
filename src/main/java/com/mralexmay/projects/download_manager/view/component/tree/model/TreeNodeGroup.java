package com.mralexmay.projects.download_manager.view.component.tree.model;

import com.mralexmay.projects.download_manager.core.model.download.category.Category;
import com.mralexmay.projects.download_manager.core.model.download.group.Group;

import java.util.ArrayList;
import java.util.List;

public class TreeNodeGroup {
    private final Group group;
    private final List<TreeNodeCategory> tnCategories;


    public TreeNodeGroup(Group group) {
        this.group = group;
        tnCategories = new ArrayList<>();
    }


    public void resetDCInCategories() {
        for (final TreeNodeCategory treeNodeCategory : getTreeNodesCategories()) {
            treeNodeCategory.resetChildesCount();
        }
    }


    public Group getGroup() {
        return group;
    }

    public List<TreeNodeCategory> getTreeNodesCategories() {
        return tnCategories;
    }

    public void updateTreeNodesCategories() {
        final List<TreeNodeCategory> tnCategories = getTreeNodesCategories();

        tnCategories.clear();
        for (Category category : group.getCategories()) {
            tnCategories.add(new TreeNodeCategory(category));
        }
    }

    @Override
    public String toString() {
        return this.group.getName();
    }
}
