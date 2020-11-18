package com.mralexmay.projects.download_manager.model.download.formatter;

import com.mralexmay.projects.download_manager.core.annotation.NotNull;
import com.mralexmay.projects.download_manager.core.model.download.PreDownloadInfo;
import com.mralexmay.projects.download_manager.core.model.download.formatter.PreDownloadOutputFormatter;

public class CorePreDownloadOutputFormatter extends PreDownloadOutputFormatter {


    public CorePreDownloadOutputFormatter(@NotNull final PreDownloadInfo pDownload) {
        super(pDownload);
    }


}
