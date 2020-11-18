package com.mralexmay.projects.download_manager.model.download.formatter;

import com.mralexmay.projects.download_manager.core.annotation.NotNull;
import com.mralexmay.projects.download_manager.core.model.download.Download;
import com.mralexmay.projects.download_manager.core.model.download.formatter.DownloadOutputFormatter;

public class CommonDownloadOutputFormatter extends DownloadOutputFormatter {
    public CommonDownloadOutputFormatter(@NotNull Download download) {
        super(download);
    }
}
