package com.mralexmay.projects.download_manager.view.dialogs.download.controller;

import com.mralexmay.projects.download_manager.controller.DownloadsController;
import com.mralexmay.projects.download_manager.core.exception.UpdateThreadAlreadyRunningException;
import com.mralexmay.projects.download_manager.core.model.download.Download;
import com.mralexmay.projects.download_manager.core.model.download.meta_information.DownloadMetaInfo;
import com.mralexmay.projects.download_manager.view.component.table.controller.TableController;
import com.mralexmay.projects.download_manager.view.component.tree.controller.TreeController;
import com.mralexmay.projects.download_manager.controller.LocalGroupsController;
import com.mralexmay.projects.download_manager.core.model.download.local_group.LocalGroup;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UIUpdater {
    private Thread backgroundWorker;
    private final DownloadsController dController;
    private final TableController tController;
    private final TreeController trController;
    private final LocalGroupsController gController;
    private volatile boolean isAlive = true;
    private boolean isUserSearchActive = false;
    private String userSearchQuery;


    public UIUpdater(DownloadsController dController, TableController tController, TreeController trController, LocalGroupsController gController) {
        this.dController = dController;
        this.tController = tController;
        this.trController = trController;
        this.gController = gController;

        this.trController.addCategoryChangeListener((e) -> categoryChange(e));
    }


    public synchronized void startUpdate(final int millis) throws UpdateThreadAlreadyRunningException {
        if (this.backgroundWorker != null && this.backgroundWorker.isAlive()) {
            throw new UpdateThreadAlreadyRunningException();
        }

        this.isAlive = true;
        this.backgroundWorker = new Thread(() -> {
            try {
                while (isAlive) {
                    SwingUtilities.invokeLater(() -> {
                        updateUI();
                    });
                    Thread.sleep(millis);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });
        this.backgroundWorker.start();
    }

    public synchronized void stopUpdate() {
        if (this.backgroundWorker != null && this.backgroundWorker.isAlive()) {
            isAlive = false;
            // this.backgroundWorker.interrupt();
        }
    }

    public synchronized void updateUI() {
        final List<DownloadMetaInfo> downloads = getDownloadsController().getDownloads();
        final List<DownloadMetaInfo> showInTable = getDownloadsForShowInTable(downloads);

        getTableController().updateTable(showInTable);
        getTreeController().updateTree(downloads);
    }

    private List<DownloadMetaInfo> getDownloadsForShowInTable(List<DownloadMetaInfo> allDownloads) {
        List<DownloadMetaInfo> showInTable = null;

        if (!isUserSearchActive) {
            final LocalGroup activeLocalGroup = getLocalGroupsController().getActiveLocalGroup();
            showInTable = activeLocalGroup.filterDownloads(allDownloads);
        } else {
            showInTable = new ArrayList<>();

            Pattern pattern = Pattern.compile(getUserSearchQuery().trim(), Pattern.CASE_INSENSITIVE);
            for (DownloadMetaInfo mDownload : getDownloadsController().getDownloads()) {
                Download download = mDownload.getDownload();
                Matcher matcher = pattern.matcher(download.getFileName().trim());

                if (matcher.find()) {
                    showInTable.add(mDownload);
                }
            }
        }

        return showInTable;
    }


    private void categoryChange(ActionEvent e) {
        this.setUserSearchActive(false);
    }


    public boolean isAlive() {
        return isAlive;
    }

    public boolean isUserSearchActive() {
        return isUserSearchActive;
    }

    public void setUserSearchActive(boolean userSearchActive) {
        isUserSearchActive = userSearchActive;
    }

    public void setUserSearchQuery(String userSearchQuery) {
        this.userSearchQuery = userSearchQuery;
    }

    public String getUserSearchQuery() {
        return userSearchQuery;
    }

    protected DownloadsController getDownloadsController() {
        return dController;
    }

    protected LocalGroupsController getLocalGroupsController() {
        return gController;
    }

    protected TableController getTableController() {
        return tController;
    }

    protected TreeController getTreeController() {
        return trController;
    }
}
