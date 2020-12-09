package com.mraleksmay.projects.download_manager.client.gui.core.util.downloader;


import com.mraleksmay.projects.download_manager.common.annotation.NotNull;
import com.mraleksmay.projects.download_manager.common.model.download.Download;

public interface NetWorker {

    void downloadOnDisk(@NotNull final Download download);
}
