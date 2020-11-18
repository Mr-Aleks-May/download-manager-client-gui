package com.mralexmay.projects.download_manager.core.model.download.local_group;

import com.mralexmay.projects.download_manager.core.model.download.meta_information.DownloadMetaInfo;
import com.mralexmay.projects.download_manager.core.annotation.NotNull;

import java.util.List;

public interface DownloadsFilter {
    List<DownloadMetaInfo> getDownloadsToDisplay(@NotNull final List<DownloadMetaInfo> downloads);
}
