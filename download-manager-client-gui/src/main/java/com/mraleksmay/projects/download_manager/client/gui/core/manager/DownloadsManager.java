package com.mraleksmay.projects.download_manager.client.gui.core.manager;


import com.mraleksmay.projects.download_manager.client.gui.core.impl.model.download.DownloadEntityInformation;
import com.mraleksmay.projects.download_manager.common.exception.DownloadAlreadyStartException;
import com.mraleksmay.projects.download_manager.common.exception.DownloadAlreadyStopException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DownloadsManager {
    private List<DownloadEntityInformation> downloads;
    private final ApplicationManager applicationManager;

    public DownloadsManager(ApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
    }


    public synchronized void add(DownloadEntityInformation downloadEntityInformation) {
        getDownloadsList().add(downloadEntityInformation);
    }


    public synchronized void addIfNotExists(DownloadEntityInformation downloadEntity) {
        Optional<DownloadEntityInformation> download = getDownloads().stream().
                filter((d) -> downloadEntity.getDownload().getSerializableId().equals(d.getDownload().getSerializableId())).
                findAny();

        if (!download.isPresent()) {
            getDownloadsList().add(downloadEntity);
        }
    }

    public void remove(DownloadEntityInformation download) {
        getDownloadsList().remove(download);
    }

    public void remove(int index) {
        final DownloadEntityInformation mDownload = getDownloadsList().get(index);
        try {
            mDownload.stop();
        } catch (DownloadAlreadyStopException threadAlreadyStop) {
        }
        getDownloadsList().remove(index);
    }

    public synchronized void start(int index) throws DownloadAlreadyStartException {
        DownloadEntityInformation mDownload = getDownloadsList().get(index);
        mDownload.start();
    }

    public synchronized void stop(int index) throws DownloadAlreadyStopException {
        DownloadEntityInformation mDownload = getDownloadsList().get(index);
        mDownload.stop();
    }

    public synchronized int indexOf(DownloadEntityInformation mDownload) {
        List<DownloadEntityInformation> mDownloads = getDownloadsList();

        for (int i = 0; i < mDownloads.size(); i++) {
            if (mDownloads.get(i).equals(mDownload)) {
                return i;
            }
        }

        return -1;
    }


    private synchronized List<DownloadEntityInformation> getDownloadsList() {
        if (downloads == null) {
            downloads = new ArrayList<>();
        }

        return downloads;
    }

    public synchronized List<DownloadEntityInformation> getDownloads() {
        return Collections.unmodifiableList(getDownloadsList());
    }

    public void removeAll() {
        getDownloadsList().clear();
    }

    public void addAll(List<DownloadEntityInformation> downloadsList) {
        getDownloadsList().addAll(downloadsList);
    }
}
