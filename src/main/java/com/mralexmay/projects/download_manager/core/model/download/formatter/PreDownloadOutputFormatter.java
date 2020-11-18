package com.mralexmay.projects.download_manager.core.model.download.formatter;

import com.mralexmay.projects.download_manager.core.model.download.PreDownloadInfo;
import com.mralexmay.projects.download_manager.core.annotation.NotNull;
import com.mralexmay.projects.download_manager.util.NetHelper;

import java.io.IOException;

public abstract class PreDownloadOutputFormatter {
    protected final PreDownloadInfo pDownload;

    public PreDownloadOutputFormatter(@NotNull final PreDownloadInfo pDownload) {
        this.pDownload = pDownload;
    }

    public String getName() {
        if (pDownload.getFileName() == null || pDownload.getFileName().equals("")) {
            return "undefined";
        }

        return pDownload.getFileName();
    }

    public String getFullSize() {
        if (pDownload.getFullSize() == -1) {
            return "undefined";
        }

        return NetHelper.formatSize(pDownload.getFullSize());
    }

    public String getUrl() {
        return pDownload.getUrl() + "";
    }

    public String getType() {
        if (pDownload.getContentType().indexOf("/") >= 0) {
            return pDownload.getContentType().substring(0, pDownload.getContentType().indexOf("/"));
        } else {
            return pDownload.getContentType();
        }
    }

    public String getTempDir() throws IOException {
        return pDownload.getTmpDir().getCanonicalPath();
    }

    public String getFullPathToFile() throws IOException {
        return pDownload.getFullPathToFile().getCanonicalPath();
    }
}
