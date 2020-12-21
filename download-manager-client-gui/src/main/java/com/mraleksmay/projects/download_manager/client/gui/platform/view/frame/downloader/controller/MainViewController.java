package com.mraleksmay.projects.download_manager.client.gui.platform.view.frame.downloader.controller;

import com.mraleksmay.projects.download_manager.client.gui.platform.model.download.DownloadEntityInformation;
import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.ApplicationManager;
import com.mraleksmay.projects.download_manager.client.gui.platform.view.frame.downloader.MainFrame;
import com.mraleksmay.projects.download_manager.client.gui.platform.view.frame.downloader.component.table.controller.TableController;
import com.mraleksmay.projects.download_manager.client.gui.platform.view.frame.downloader.component.tree.controller.TreeController;
import com.mraleksmay.projects.download_manager.common.exception.ThreadAlreadyStartException;
import com.mraleksmay.projects.download_manager.common.view.UIWorker;

import javax.swing.*;

public class MainViewController {
    private ApplicationManager applicationManager;
    private MainFrame mainFrame;
    private TableController tableController;
    private TreeController treeController;
    private UIWorker uiUpdater;

    public MainViewController(ApplicationManager applicationManager, MainFrame mainFrame) {
        this.applicationManager = applicationManager;
        this.mainFrame = mainFrame;
    }

    public void init() {
        tableController = new TableController(getMainFrame().getTable(), getApplicationManager());
        tableController.initTable();
        treeController = new TreeController(getMainFrame().getTree(), getApplicationManager());
        treeController.initTree();

        uiUpdater = new MainFrameUIWorker(() -> {
            SwingUtilities.invokeLater(() -> {
                getTableController().updateTable();
                getTreeController().updateTree();
            });
        }, 500);

        try {
            uiUpdater.start();
        } catch (ThreadAlreadyStartException threadAlreadyStartException) {
            threadAlreadyStartException.printStackTrace();
        }
    }

    public void add(DownloadEntityInformation downloadEntityInfo) {
        getTableController().updateTable();
        getTreeController().updateTree();
    }

    public ApplicationManager getApplicationManager() {
        return applicationManager;
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public TableController getTableController() {
        return tableController;
    }

    public TreeController getTreeController() {
        return treeController;
    }
}
