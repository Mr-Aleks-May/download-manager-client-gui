package com.mralexmay.projects.download_manager.model.download.meta;

import com.mralexmay.projects.download_manager.core.model.download.Download;
import com.mralexmay.projects.download_manager.core.model.download.formatter.DownloadOutputFormatter;
import com.mralexmay.projects.download_manager.core.model.download.meta_information.DownloadMetaInfo;
import com.mralexmay.projects.download_manager.core.util.NetWorker;
import com.mralexmay.projects.download_manager.core.annotation.NotNull;
import com.mralexmay.projects.download_manager.core.exception.DownloadAlreadyStartException;
import com.mralexmay.projects.download_manager.core.exception.ThreadAlreadyStop;
import com.mralexmay.projects.download_manager.model.download.formatter.CommonDownloadOutputFormatter;

public class CoreDownloadMetaInfo implements DownloadMetaInfo {
    private final Download download;
    private final NetWorker worker;
    private final DownloadOutputFormatter formatter;
    private Thread dThread;


    public CoreDownloadMetaInfo(@NotNull final Download download,
                                @NotNull final NetWorker worker) {
        this.download = download;
        this.worker = worker;
        this.formatter = new CommonDownloadOutputFormatter(download);
    }


    @Override
    public synchronized void start() throws DownloadAlreadyStartException {
        if (dThread != null && dThread.isAlive()) {
            throw new DownloadAlreadyStartException();
        }

        dThread = new Thread(() -> {
            worker.downloadOnDiskSync(download);
        });
        dThread.start();
    }

    @Override
    public synchronized void stop() throws ThreadAlreadyStop {
        if (dThread == null || !dThread.isAlive()) {
            dThread = null;
            throw new ThreadAlreadyStop();
        }

        download.setStatus(Download.Status.STOP);
    }


    @Override
    public Download getDownload() {
        return download;
    }

    @Override
    public NetWorker getWorker() {
        return worker;
    }

    @Override
    public DownloadOutputFormatter getOutputFormatter() {
        return formatter;
    }
}
