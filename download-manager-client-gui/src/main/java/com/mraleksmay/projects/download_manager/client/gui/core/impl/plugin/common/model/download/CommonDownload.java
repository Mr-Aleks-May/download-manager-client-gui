package com.mraleksmay.projects.download_manager.client.gui.core.impl.plugin.common.model.download;

import com.mraleksmay.projects.download_manager.common.annotation.NotNull;
import com.mraleksmay.projects.download_manager.common.model.category.Category;
import com.mraleksmay.projects.download_manager.common.model.download.AuthenticationData;
import com.mraleksmay.projects.download_manager.common.model.download.Download;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class CommonDownload extends Download {
    public CommonDownload() {
    }

    public CommonDownload(Download download) throws IOException {
        super(download);
    }

    public CommonDownload(String fileName,
                          URL url,
                          File outputDir,
                          @NotNull final String extension,
                          AuthenticationData authData,
                          Category category,
                          long fullSize) throws IOException {
        super(fileName, url, outputDir, extension, authData, category, fullSize);
    }
}
