package com.mralexmay.projects.download_manager.core.model.download.meta_information;

import com.mralexmay.projects.download_manager.core.model.download.Download;
import com.mralexmay.projects.download_manager.core.model.download.formatter.DownloadOutputFormatter;
import com.mralexmay.projects.download_manager.core.util.NetWorker;
import com.mralexmay.projects.download_manager.core.exception.DownloadAlreadyStartException;
import com.mralexmay.projects.download_manager.core.exception.ThreadAlreadyStop;

public interface DownloadMetaInfo {
    void start() throws DownloadAlreadyStartException;

    void stop() throws ThreadAlreadyStop;

    Download getDownload();

    NetWorker getWorker();

    DownloadOutputFormatter getOutputFormatter();
}
