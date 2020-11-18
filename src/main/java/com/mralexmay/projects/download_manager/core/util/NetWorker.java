package com.mralexmay.projects.download_manager.core.util;

import com.mralexmay.projects.download_manager.core.model.download.Download;
import com.mralexmay.projects.download_manager.core.annotation.NotNull;

public interface NetWorker {

    void downloadOnDiskSync(@NotNull final Download download);
}
