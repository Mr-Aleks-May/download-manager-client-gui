package com.mraleksmay.projects.download_manager.client.gui.core.impl.model.download;

import com.google.gson.annotations.Expose;
import com.mraleksmay.projects.download_manager.client.gui.core.util.downloader.NetWorker;
import com.mraleksmay.projects.download_manager.common.annotation.NotNull;
import com.mraleksmay.projects.download_manager.common.exception.DownloadAlreadyStartException;
import com.mraleksmay.projects.download_manager.common.exception.DownloadAlreadyStopException;
import com.mraleksmay.projects.download_manager.common.model.download.Download;

import java.io.Serializable;

public class DownloadEntityInformation implements Serializable {
    @Expose
    protected final Download download;
    protected NetWorker worker;
    private Thread dThread;

    public DownloadEntityInformation(@NotNull final Download download, @NotNull final NetWorker worker) {
        this.download = download;
        this.worker = worker;
    }

    public synchronized void start() throws DownloadAlreadyStartException {
        if (dThread != null && dThread.isAlive()) {
            throw new DownloadAlreadyStartException();
        }

        dThread = new Thread(() -> {
            worker.downloadOnDisk(download);
        });
        dThread.start();
    }

    public synchronized void stop() throws DownloadAlreadyStopException {
        if (dThread == null || !dThread.isAlive()) {
            dThread = null;
            throw new DownloadAlreadyStopException();
        }

        download.setStatus(Download.Status.STOP);
    }

    public Download getDownload() {
        return download;
    }

    public NetWorker getWorker() {
        return worker;
    }
}
