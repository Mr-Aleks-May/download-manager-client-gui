package com.mraleksmay.projects.download_manager.common.manager;

import com.mraleksmay.projects.download_manager.client.gui.commons.model.download.DownloadEntityInformation;

import java.util.List;

public interface Scheduler {
    void add(DownloadEntityInformation download);

    void remove(DownloadEntityInformation downloadEntityInfo);

    void moveUp(DownloadEntityInformation download);

    void moveDown(DownloadEntityInformation download);

    List<DownloadEntityInformation> getDownloads();

    int getQueueSize();

    void setQueueSize(int queueSize);
}
