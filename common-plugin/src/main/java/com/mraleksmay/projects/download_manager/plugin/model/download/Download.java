package com.mraleksmay.projects.download_manager.plugin.model.download;

import com.mraleksmay.projects.download_manager.common.annotation.NotNull;
import com.mraleksmay.projects.download_manager.common.annotation.Serialize;
import com.mraleksmay.projects.download_manager.plugin.model.category.Category;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;


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


    @Serialize
    private String dsid = "null";
    private static long globalId = 0;
    private long id;
    @Serialize
    @NotNull
    private String fileName;
    @Serialize
    @NotNull
    private String extension;
    @Serialize
    private long fullSize;
    @Serialize
    private long currentSize;
    @Serialize
    @NotNull
    private String _tmpDir;
    @Serialize
    @NotNull
    private String _outputDir;
    @Serialize
    @NotNull
    private String _url;
    private volatile Status status = Status.CREATED;
    @Serialize
    @NotNull
    private Category category;
    @Serialize
    private long creationTime;


    public Download() {
        setId(globalId++);
    }

    public Download(Download download) throws IOException {
        this(download.getId(),
                download.getFileName(),
                download.getUrl(),
                download.getOutputDir(),
                download.getExtension(),
                download.getAuthData(),
                download.getCategory(),
                download.getFullSize());
    }

    public Download(@NotNull final String fileName,
                    @NotNull final URL url,
                    @NotNull final File outputDir,
                    @NotNull final String extension,
                    AuthenticationData authData,
                    @NotNull final Category category,
                    long fullSize) throws IOException {
        this(globalId++, fileName, url, outputDir, extension, authData, category, fullSize);
    }

    private Download(long id,
                     @NotNull final String fileName,
                     @NotNull final URL url,
                     @NotNull final File outputDir,
                     @NotNull final String extension,
                     AuthenticationData authData,
                     @NotNull final Category category,
                     long fullSize) throws IOException {
        String sid = String.format("%s-%s-%s-%s",
                System.currentTimeMillis(),
                System.nanoTime(),
                new Random().nextInt(),
                new Random().nextLong());
        setDSID(sid);

        setId(id);
        setFileName(fileName.trim());
        setExtension(extension);
        setUrl(url);
        setTempDir(category.getTempDir());
        setOutputDir(outputDir);
        if (authData != null) {
            setAuthData(authData.getLogin(), authData.getPassword());
        }
        setCategory(category);
        setFullSize(fullSize);
        setCreationTime(System.currentTimeMillis());
    }


    public void addBytes(int read) {
        currentSize += read;
    }

    public boolean isCompleted() {
        return currentSize == fullSize
                && fullSize > 0;
    }


    public abstract DownloadFormatter getFormatter();

    public abstract Download setFormatter(DownloadFormatter formatter);

    public abstract AuthenticationData getAuthData();

    public abstract Download setAuthData(String login, String password);

    public abstract Download setAuthData(AuthenticationData authData);

    public float getProgress() {
        float progress = ((float) currentSize / (float) fullSize) * 100f;

        if (Float.isNaN(progress))
            return 0f;
        else
            return Math.abs(progress);
    }

    public File getFullTempPathToFile() throws IOException {
        return new File(getTempDir() + "/" + fileName + "." + getExtension()).getCanonicalFile();
    }

    public File getFullPathToFile() throws IOException {
        String fullPath = getOutputDir() + "/" + getFileName().trim() + "." + getExtension();
        return new File(fullPath).getCanonicalFile();
    }


    public String getDSID() {
        return dsid;
    }

    public Download setDSID(String dsid) {
        this.dsid = dsid;
        return this;
    }

    public static long getGlobalId() {
        return globalId;
    }

    public long getId() {
        return id;
    }

    public Download setId(long id) {
        this.id = id;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public Download setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getExtension() {
        return extension;
    }

    public Download setExtension(String extension) {
        this.extension = extension;
        return this;
    }

    public long getFullSize() {
        return fullSize;
    }

    public Download setFullSize(long fullSize) {
        this.fullSize = fullSize;
        return this;
    }

    public long getCurrentSize() {
        return currentSize;
    }

    public Download setCurrentSize(long currentSize) {
        this.currentSize = currentSize;
        return this;
    }

    public File getTempDir() throws IOException {
        return new File(_tmpDir).getCanonicalFile();
    }

    public Download setTempDir(File tempDir) throws IOException {
        this._tmpDir = tempDir.getCanonicalPath();
        return this;
    }

    public File getOutputDir() throws IOException {
        return new File(_outputDir).getCanonicalFile();
    }

    public Download setOutputDir(File outputDir) throws IOException {
        this._outputDir = outputDir.getCanonicalPath();
        return this;
    }

    public URL getUrl() throws MalformedURLException {
        return new URL(_url);
    }

    public Download setUrl(URL url) {
        this._url = url.toString();
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public Download setStatus(Status status) {
        this.status = status;
        return this;
    }

    public Category getCategory() {
        return category;
    }

    public Download setCategory(Category category) {
        this.category = category;
        return this;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public Download setCreationTime(long creationTime) {
        this.creationTime = creationTime;
        return this;
    }

    @Override
    public String toString() {
        return "Download{" +
                "dsid='" + dsid + '\'' +
                '}';
    }
}
