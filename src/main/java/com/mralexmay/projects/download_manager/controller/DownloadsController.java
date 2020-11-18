package com.mralexmay.projects.download_manager.controller;

import com.mralexmay.projects.download_manager.core.model.download.meta_information.DownloadMetaInfo;
import com.mralexmay.projects.download_manager.core.exception.DownloadAlreadyStartException;
import com.mralexmay.projects.download_manager.core.exception.ThreadAlreadyStop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DownloadsController {
    private List<DownloadMetaInfo> downloads;


    public synchronized void add(DownloadMetaInfo mDownload) {
        getDownloadsList().add(mDownload);
    }

    public synchronized void start(int index) throws DownloadAlreadyStartException {
        DownloadMetaInfo mDownload = getDownloadsList().get(index);
        mDownload.start();
    }

    public synchronized void stop(int index) throws ThreadAlreadyStop {
        DownloadMetaInfo mDownload = getDownloadsList().get(index);
        mDownload.stop();
    }

    public synchronized int indexOf(DownloadMetaInfo mDownload) {
        List<DownloadMetaInfo> mDownloads = getDownloadsList();

        for (int i = 0; i < mDownloads.size(); i++) {
            if (mDownloads.get(i).equals(mDownload)) {
                return i;
            }
        }

        return -1;
    }


    private synchronized List<DownloadMetaInfo> getDownloadsList() {
        if (downloads == null) {
            downloads = new ArrayList<>();
        }

        return downloads;
    }

    public synchronized List<DownloadMetaInfo> getDownloads() {
        return Collections.unmodifiableList(getDownloadsList());
    }

    public void remove(int index) {
        final DownloadMetaInfo mDownload = getDownloadsList().get(index);
        try {
            mDownload.stop();
        } catch (ThreadAlreadyStop threadAlreadyStop) {
        }
        getDownloadsList().remove(index);
    }
}
