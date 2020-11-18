package com.mralexmay.projects.download_manager.model.download;

import com.mralexmay.projects.download_manager.core.model.download.Download;
import com.mralexmay.projects.download_manager.core.model.download.auth.AuthData;
import com.mralexmay.projects.download_manager.core.model.download.category.Category;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class CommonDownload extends Download {

    public CommonDownload(CorePreDownloadInfo pDownload) throws IOException {
        super(pDownload);
    }

    public CommonDownload(String fileName, URL url, File outputDir, AuthData authData, Category category, long fullSize) throws IOException {
        super(fileName, url, outputDir, authData, category, fullSize);
    }
}
