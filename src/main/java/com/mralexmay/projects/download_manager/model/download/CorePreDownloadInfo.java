package com.mralexmay.projects.download_manager.model.download;

import com.mralexmay.projects.download_manager.core.model.download.PreDownloadInfo;
import com.mralexmay.projects.download_manager.core.model.download.auth.AuthData;
import com.mralexmay.projects.download_manager.core.model.download.category.Category;

import java.io.IOException;
import java.net.URL;

public class CorePreDownloadInfo extends PreDownloadInfo {

    public CorePreDownloadInfo(String fileName, URL url, AuthData authData, Category category) throws IOException {
        this(fileName, url, authData, "undefined", 0, category);
    }

    public CorePreDownloadInfo(String fileName, URL url, AuthData authData, String contentType, long fullSize, Category category) throws IOException {
        super(fileName, url, authData, fullSize, contentType, category);
    }
}
