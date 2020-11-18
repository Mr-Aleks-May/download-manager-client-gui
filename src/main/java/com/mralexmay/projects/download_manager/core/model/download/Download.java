package com.mralexmay.projects.download_manager.core.model.download;

import com.mralexmay.projects.download_manager.core.model.download.auth.AuthData;
import com.mralexmay.projects.download_manager.core.model.download.category.Category;
import com.mralexmay.projects.download_manager.core.annotation.NotNull;
import com.mralexmay.projects.download_manager.model.download.CorePreDownloadInfo;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class Download {
    public enum Status {
        CREATED,
        PAUSED,
        UPDATING,
        DOWNLOADING,
        DOWNLOADED,
        FINISHING,
        STOP,
        ERROR
    }

    private static long globalId = 0;
    private final long id = globalId++;
    private String fileName;
    private long fullSize;
    private long currentSize;
    private File tmpDir;
    private File outputDir;
    private URL url;
    private AuthData authData;
    private volatile Status status = Status.CREATED;
    private Category category;


    public Download(@NotNull final CorePreDownloadInfo pDownload) throws IOException {
        this(pDownload.getFileName(), pDownload.getUrl(), pDownload.getOutputDir(), pDownload.getAuthData(), pDownload.getCategory(), pDownload.getFullSize());
    }

    public Download(@NotNull final String fileName,
                    @NotNull final URL url,
                    @NotNull final File outputDir,
                    AuthData authData,
                    @NotNull final Category category,
                    long fullSize) throws IOException {
        this.fileName = fileName.trim();
        this.url = url;
        this.tmpDir = category.getTmpDir().getCanonicalFile();
        this.outputDir = outputDir.getCanonicalFile();
        this.authData = authData;
        this.category = category;
        this.fullSize = fullSize;
    }


    public void addBytes(int downloadedBytes) {
        currentSize += downloadedBytes;
    }


    public static void setGlobalId(long globalId) {
        Download.globalId = globalId;
    }

    public static long getGlobalId() {
        return globalId;
    }

    public long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public long getFullSize() {
        return fullSize;
    }

    public void setFullSize(long fullSize) {
        this.fullSize = fullSize;
    }

    public long getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(long currentSize) {
        this.currentSize = currentSize;
    }

    public File getTmpDir() {
        return tmpDir;
    }

    public void setTmpDir(@NotNull final File tmpDir) {
        this.tmpDir = tmpDir;
    }

    public File getOutputDir() throws IOException {
        return outputDir.getCanonicalFile();
    }

    public void setOutputDir(@NotNull final File outputDir) throws IOException {
        this.outputDir = outputDir.getCanonicalFile();
    }

    public File getFullTempPathToFile() throws IOException {
        return new File(getTmpDir() + "/" + fileName).getCanonicalFile();
    }

    public File getFullPathToFile() throws IOException {
        return new File(getOutputDir() + "/" + fileName).getCanonicalFile();
    }

    public URL getUrl() throws MalformedURLException {
        return new URL(url.toString());
    }

    public float getProgress() {
        float progress = ((float) currentSize / (float) fullSize) * 100f;

        if (Float.isNaN(progress))
            return 0f;
        else
            return Math.abs(progress);
    }

    public AuthData getAuthData() {
        return authData;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(@NotNull final Status status) {
        this.status = status;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(@NotNull final Category category) {
        this.category = category;
    }
}
