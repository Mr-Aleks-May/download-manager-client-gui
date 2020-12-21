package com.mraleksmay.projects.download_manager.client.gui.platform.view.frame.downloader.component.tree.controller;

import com.mraleksmay.projects.download_manager.client.gui.platform.model.download.DownloadEntityInformation;
import com.mraleksmay.projects.download_manager.client.gui.platform.model.plugin.model.plugin.BasePlugin;
import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.ApplicationManager;
import com.mraleksmay.projects.download_manager.common.annotation.NotNull;
import com.mraleksmay.projects.download_manager.client.gui.platform.view.frame.downloader.component.tree.model.TreeNodeCategory;
import com.mraleksmay.projects.download_manager.client.gui.platform.view.frame.downloader.component.tree.model.TreeNodeGroup;
import com.mraleksmay.projects.download_manager.plugin.model.category.Category;
import com.mraleksmay.projects.download_manager.plugin.model.download.Download;
import com.mraleksmay.projects.download_manager.plugin.model.group.Group;
import com.mraleksmay.projects.download_manager.plugin.model.plugin.Plugin;

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
    private JTree tree;
    private ApplicationManager applicationManager;
    private final List<TreeNodeGroup> treeNodesGroups;
    private List<ActionListener> categoryChangeListeners = new ArrayList<>();


    public TreeController(JTree tree, ApplicationManager applicationManager) {
        this.tree = tree;
        this.applicationManager = applicationManager;
        this.treeNodesGroups = new ArrayList<>();
    }


    public void initTree() {
        final JTree tree = this.tree;

        final DefaultTreeModel model = new DefaultTreeModel(new DefaultMutableTreeNode("(root)"));
        tree.setModel(model);
        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                selectionChange(mouseEvent);
            }
        });

        rebuildTree();
        setAndSelectCategoryByDefault();
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
                    getApplicationManager().getPluginsManager().setCurrentSelectedPluginId(gNode.getGroup().getParent().getId());
                    getApplicationManager().getPluginsManager().setCurrentSelectedGroupId(gNode.getGroup().getId());
                    getApplicationManager().getPluginsManager().setCurrentSelectedCategoryId(cNode.getCategory().getId());

                    for (ActionListener listener : this.categoryChangeListeners) {
                        listener.actionPerformed(null);
                    }
                }
            }

            getApplicationManager().getMainFrame().setUserSearchActive(false);
        }
    }

    public void add(Plugin plugin) {
        final DefaultTreeModel model = (DefaultTreeModel) getTree().getModel();
        final DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();

        for (Group group : plugin.getGroups()) {
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
                    getApplicationManager().getPluginsManager().setCurrentSelectedPluginId(treeNodeGroup.getGroup().getParent().getId());
                    getApplicationManager().getPluginsManager().setCurrentSelectedGroupId(treeNodeGroup.getGroup().getId());
                    getApplicationManager().getPluginsManager().setCurrentSelectedCategoryId(treeNodeCategory.getCategory().getId());

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


    public void updateTree() {
        final List<DownloadEntityInformation> allDownloads = new ArrayList<>(getApplicationManager().getDownloadsManager().getDownloads());
        final List<Plugin> plugins = getApplicationManager().getPluginsManager().getPlugins();

        for (final TreeNodeGroup group : getTreeNodesGroups()) {
            group.resetDCInCategories();
        }

        for (final DownloadEntityInformation mDownload : allDownloads) {
            final Download download = mDownload.getDownload();

            if (BasePlugin.class.equals(download.getCategory().getPlugin().getClass())) {
                displayCommonPluginDownloads(download);
            } else {
                displayOtherPluginDownloads(download);
            }
        }

        tree.updateUI();
    }

    private void displayCommonPluginDownloads(Download download) {
        for (final TreeNodeGroup treeNodeGroup : getTreeNodesGroups()) {
            final Group group = treeNodeGroup.getGroup();

            if (group.getId().equals("ALL")) {
                for (final TreeNodeCategory treeNodeCategory : treeNodeGroup.getTreeNodesCategories()) {
                    final Category category = treeNodeCategory.getCategory();

                    if (download.getCategory().equals(category) || category.getId().equals("ALL")) {
                        treeNodeCategory.increaseDC();
                    }
                }
            } else if (group.getId().equals("UNFINISHED") && download.getStatus() != Download.Status.DOWNLOADED) {
                for (final TreeNodeCategory treeNodeCategory : treeNodeGroup.getTreeNodesCategories()) {
                    final Category category = treeNodeCategory.getCategory();

                    if (download.getCategory().equals(category) || category.getId().equals("ALL")) {
                        treeNodeCategory.increaseDC();
                    }
                }
            } else if (group.getId().equals("COMPLETED") && download.getStatus() == Download.Status.DOWNLOADED) {
                for (final TreeNodeCategory treeNodeCategory : treeNodeGroup.getTreeNodesCategories()) {
                    final Category category = treeNodeCategory.getCategory();

                    if (download.getCategory().equals(category) || category.getId().equals("ALL")) {
                        treeNodeCategory.increaseDC();
                    }
                }
            }
        }
    }

    private void displayOtherPluginDownloads(Download download) {
        for (final TreeNodeGroup treeNodeGroup : getTreeNodesGroups()) {
            final Group group = treeNodeGroup.getGroup();

            if (download.getCategory().getPlugin().equals(group.getParent())) {
                for (final TreeNodeCategory treeNodeCategory : treeNodeGroup.getTreeNodesCategories()) {
                    final Category category = treeNodeCategory.getCategory();

                    if (download.getCategory().equals(category) || "ALL".equals(category.getId())) {
                        treeNodeCategory.increaseDC();
                    }
                }
            }
        }
    }

    public void rebuildTree() {
        JTree tree = getTree();
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        List<Plugin> pluginsList = getApplicationManager().getPluginsManager().getPlugins();

        root.removeAllChildren();

        for (Plugin plugin : pluginsList) {
            add(plugin);
        }

        tree.updateUI();
    }

    public JTree getTree() {
        return tree;
    }

    public ApplicationManager getApplicationManager() {
        return applicationManager;
    }

    public List<TreeNodeGroup> getTreeNodesGroups() {
        return treeNodesGroups;
    }

    public List<ActionListener> getCategoryChangeListeners() {
        return categoryChangeListeners;
    }
}
