package com.mraleksmay.projects.download_manager.client.gui.platform.model.plugin.model.download;


import com.mraleksmay.projects.download_manager.plugin.model.download.Download;
import com.mraleksmay.projects.download_manager.plugin.model.download.DownloadFormatter;

public class BaseDownloadFormatter extends DownloadFormatter {
    public BaseDownloadFormatter() {
    }

    public BaseDownloadFormatter(Download download) {
        super(download);
    }
}
