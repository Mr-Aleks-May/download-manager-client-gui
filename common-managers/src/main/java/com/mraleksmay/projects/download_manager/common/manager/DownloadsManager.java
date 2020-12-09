package com.mraleksmay.projects.download_manager.common.manager;


import com.mraleksmay.projects.download_manager.common.exception.DownloadAlreadyStartException;
import com.mraleksmay.projects.download_manager.common.exception.DownloadAlreadyStopException;
import com.mraleksmay.projects.download_manager.plugin.model.download.DownloadEntityInformation;

import java.util.List;

public interface DownloadsManager {
    void add(DownloadEntityInformation downloadEntityInformation);

    void addIfNotExists(DownloadEntityInformation downloadEntity);

    void remove(DownloadEntityInformation download);

    void remove(int index);

    void start(int index) throws DownloadAlreadyStartException;

    void stop(int index) throws DownloadAlreadyStopException;

    int indexOf(DownloadEntityInformation mDownload);

    List<DownloadEntityInformation> getDownloads();

    void removeAll();
}
