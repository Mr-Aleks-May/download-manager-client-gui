package com.mraleksmay.projects.download_manager.client.gui.core.view.frame.downloader.component.tree.model;



import com.mraleksmay.projects.download_manager.common.model.category.Category;
import com.mraleksmay.projects.download_manager.common.model.group.Group;

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
