package com.mralexmay.projects.download_manager.view.component.tree.controller;

import com.mralexmay.projects.download_manager.core.annotation.NotNull;
import com.mralexmay.projects.download_manager.core.model.download.Download;
import com.mralexmay.projects.download_manager.core.model.download.category.Category;
import com.mralexmay.projects.download_manager.core.model.download.group.Group;
import com.mralexmay.projects.download_manager.core.model.download.meta_information.DownloadMetaInfo;
import com.mralexmay.projects.download_manager.view.component.tree.model.TreeNodeCategory;
import com.mralexmay.projects.download_manager.view.component.tree.model.TreeNodeGroup;
import com.mralexmay.projects.download_manager.controller.LocalGroupsController;
import com.mralexmay.projects.download_manager.core.model.download.local_group.LocalGroup;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class TreeController {
    private final JTree tree;
    private final LocalGroupsController gController;
    private final List<TreeNodeGroup> treeNodeGroups;
    private List<ActionListener> categoryChangeListeners = new ArrayList<>();

    public TreeController(@NotNull JTree tree,
                          @NotNull LocalGroupsController gController) {
        this.tree = tree;
        this.gController = gController;
        this.treeNodeGroups = new ArrayList<>();
    }


    public synchronized void initTree() {
        final JTree tree = this.tree;

        final DefaultTreeModel model = new DefaultTreeModel(new DefaultMutableTreeNode("(root)"));
        tree.setModel(model);
        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                selectionChange(mouseEvent);
            }
        });
    }


    public void add(LocalGroup localGroups) {
        final DefaultTreeModel model = (DefaultTreeModel) getTree().getModel();
        final DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();

        for (Group group : localGroups.getGroups()) {
            add(group);
        }

        getTree().updateUI();
    }

    public void add(Group group) {
        final DefaultTreeModel model = (DefaultTreeModel) getTree().getModel();
        final DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();

        final TreeNodeGroup treeNodeGroup = new TreeNodeGroup(group);
        final DefaultMutableTreeNode mutableTreeNodeGroup = new DefaultMutableTreeNode(treeNodeGroup);

        treeNodeGroup.updateTreeNodesCategories();
        getTreeNodesGroups().add(treeNodeGroup);

        for (TreeNodeCategory treeNodeCategory : treeNodeGroup.getTreeNodesCategories()) {
            final DefaultMutableTreeNode mutableTreeNodeCategory = new DefaultMutableTreeNode(treeNodeCategory);
            mutableTreeNodeCategory.setAllowsChildren(false);
            mutableTreeNodeGroup.add(mutableTreeNodeCategory);
        }

        root.add(mutableTreeNodeGroup);
    }

    public void add(Category category) {

    }

    private void selectionChange(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() >= 2) {
            final TreePath sPath = getTree().getSelectionPath();
            final DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) sPath.getLastPathComponent();
            final DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) treeNode.getParent();

            if (parentNode.getUserObject().getClass() == TreeNodeGroup.class) {
                final TreeNodeGroup gNode = (TreeNodeGroup) parentNode.getUserObject();
                final TreeNodeCategory cNode = (TreeNodeCategory) treeNode.getUserObject();

                if (cNode.getClass() == TreeNodeCategory.class) {
                    getLocalGroupsController().setCurrentSelectedLocalGroupId(gNode.getGroup().getParent().getId());
                    getLocalGroupsController().setCurrentSelectedGroupId(gNode.getGroup().getId());
                    getLocalGroupsController().setCurrentSelectedCategoryId(cNode.getCategory().getId());

                    for (ActionListener listener : this.categoryChangeListeners) {
                        listener.actionPerformed(null);
                    }
                }
            }
        }
    }

    public void setAndSelectCategoryByDefault() {
        DefaultTreeModel model = (DefaultTreeModel) getTree().getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();

        for (int i = 0; i < root.getChildCount(); i++) {
            DefaultMutableTreeNode childGroup = (DefaultMutableTreeNode) root.getChildAt(i);
            TreeNodeGroup treeNodeGroup = (TreeNodeGroup) childGroup.getUserObject();

            if (!treeNodeGroup.getGroup().getParent().getId().equals("CORE")) {
                continue;
            }

            for (int j = 0; j < childGroup.getChildCount(); j++) {
                DefaultMutableTreeNode childCategory = (DefaultMutableTreeNode) childGroup.getChildAt(j);
                TreeNodeCategory treeNodeCategory = (TreeNodeCategory) childCategory.getUserObject();

                if (treeNodeGroup.getGroup().getId().equals("ALL") && treeNodeCategory.getCategory().getId().equals("ALL")) {
                    gController.setCurrentSelectedLocalGroupId(treeNodeGroup.getGroup().getParent().getId());
                    getLocalGroupsController().setCurrentSelectedGroupId(treeNodeGroup.getGroup().getId());
                    getLocalGroupsController().setCurrentSelectedCategoryId(treeNodeCategory.getCategory().getId());

                    expandPath(new TreePath(childCategory.getPath()));
                    getTree().updateUI();
                    return;
                }
            }
        }
    }

    public synchronized void expandPath(@NotNull TreePath pathToNode) {
        getTree().setSelectionPath(pathToNode);
        getTree().expandPath(pathToNode);
    }

    public synchronized void collapsePath(@NotNull TreePath pathToNode) {
        getTree().collapsePath(pathToNode);
    }


    public void updateTree(List<DownloadMetaInfo> downloads) {
        final List<DownloadMetaInfo> allDownloads = new ArrayList<>(downloads);
        final List<TreeNodeGroup> treeNodeGroups = getTreeNodesGroups();

        for (final TreeNodeGroup group : treeNodeGroups) {
            group.resetDCInCategories();
        }

        // Update group ALL
        for (final DownloadMetaInfo mDownload : allDownloads) {
            final Download download = mDownload.getDownload();

            for (final TreeNodeGroup treeNodeGroup : treeNodeGroups) {
                final Group group = treeNodeGroup.getGroup();

                if (group.getId().equals("ALL")) {
                    for (final TreeNodeCategory treeNodeCategory : treeNodeGroup.getTreeNodesCategories()) {
                        final Category category = treeNodeCategory.getCategory();

                        if (download.getCategory().equals(category) || category.getId() == "ALL") {
                            treeNodeCategory.increaseDC();
                        }
                    }
                } else if (group.getId().equals("UNFINISHED") && download.getStatus() != Download.Status.DOWNLOADED) {
                    for (final TreeNodeCategory treeNodeCategory : treeNodeGroup.getTreeNodesCategories()) {
                        final Category category = treeNodeCategory.getCategory();

                        if (download.getCategory().equals(category) || category.getId() == "ALL") {
                            treeNodeCategory.increaseDC();
                        }
                    }
                } else if (group.getId().equals("COMPLETED") && download.getStatus() == Download.Status.DOWNLOADED) {
                    for (final TreeNodeCategory treeNodeCategory : treeNodeGroup.getTreeNodesCategories()) {
                        final Category category = treeNodeCategory.getCategory();

                        if (download.getCategory().equals(category) || category.getId() == "ALL") {
                            treeNodeCategory.increaseDC();
                        }
                    }
                }
            }
        }

        tree.updateUI();
    }

    public void rebuildTree(LocalGroupsController localGroupsController) {
        JTree tree = getTree();
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();

        root.removeAllChildren();

        for (LocalGroup localGroup : localGroupsController.getLocalGroups()) {
            add(localGroup);
        }

        tree.updateUI();
    }


    public void addCategoryChangeListener(ActionListener listener) {
        this.categoryChangeListeners.add(listener);
    }


    public JTree getTree() {
        return tree;
    }

    public LocalGroupsController getLocalGroupsController() {
        return gController;
    }

    public List<TreeNodeGroup> getTreeNodesGroups() {
        return treeNodeGroups;
    }
}
