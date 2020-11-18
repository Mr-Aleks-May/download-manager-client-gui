package com.mralexmay.projects.download_manager.core.model.download;

import com.mralexmay.projects.download_manager.core.model.download.category.Category;
import com.mralexmay.projects.download_manager.core.annotation.NotNull;
import com.mralexmay.projects.download_manager.core.model.download.auth.AuthData;
import com.mralexmay.projects.download_manager.model.download.formatter.CorePreDownloadOutputFormatter;
import com.mralexmay.projects.download_manager.core.model.download.formatter.PreDownloadOutputFormatter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

public abstract class PreDownloadInfo {
    @NotNull
    protected final URL url;
    protected final AuthData authData;
    @NotNull
    protected final PreDownloadOutputFormatter poFormatter;
    @NotNull
    protected String fileName;
    protected long fullSize;
    protected String contentType;
    @NotNull
    protected File outputDir;
    @NotNull
    protected Category category;

    public PreDownloadInfo(String fileName, URL url, AuthData authData, long fullSize, String contentType, Category category) throws IOException {
        this.fileName = fileName.trim();
        this.url = url;
        this.authData = authData;
        this.fullSize = fullSize;
        this.contentType = contentType.trim();
        this.outputDir = category.getOutputDir().getCanonicalFile();
        this.category = category;
        this.poFormatter = new CorePreDownloadOutputFormatter(this);
    }

    public static String getNameFrom(@NotNull final String url) {
        String name = "";
        String urlStr = url + "";

        int lIndex = urlStr.lastIndexOf('/');

        if (lIndex >= 0) {
            name = urlStr.substring(lIndex + 1);
        } else {
            name = new Random().nextInt() + "";
        }

        return name.trim();
    }

    public synchronized String getFileName() {
        if (fileName == null || fileName.equals("")) {
            return "undefined";
        }

        return fileName;
    }

    public synchronized void setFileName(String fileName) {
        if (!fileName.equals(""))
            this.fileName = fileName.trim();
    }

    public synchronized URL getUrl() {
        return url;
    }

    public synchronized AuthData getAuthData() {
        return authData;
    }

    public synchronized long getFullSize() {
        return fullSize;
    }

    public synchronized void setFullSize(long fullSize) {
        this.fullSize = fullSize;
    }

    public synchronized String getContentType() {
        return contentType;
    }

    public synchronized void setContentType(String contentType) {
        if (contentType != null) this.contentType = contentType;
    }

    public synchronized PreDownloadOutputFormatter getFormatter() {
        return poFormatter;
    }

    public synchronized File getTmpDir() throws IOException {
        return category.getTmpDir().getCanonicalFile();
    }

    public File getOutputDir() throws IOException {
        return outputDir.getCanonicalFile();
    }

    public void setOutputDir(File outputDir) {
        this.outputDir = outputDir;
    }

    public File getFullPathToFile() throws IOException {
        return new File(outputDir.getCanonicalPath() + "/" + fileName).getCanonicalFile();
    }

    public synchronized Category getCategory() {
        return category;
    }

    public synchronized void setCategory(Category category) {
        if (category != null) this.category = category;
    }
}
