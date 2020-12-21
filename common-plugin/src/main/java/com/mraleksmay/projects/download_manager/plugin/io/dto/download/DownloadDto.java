package com.mraleksmay.projects.download_manager.plugin.io.dto.download;

import com.google.gson.annotations.Expose;
import com.mraleksmay.projects.download_manager.common.annotation.NotNull;
import com.mraleksmay.projects.download_manager.plugin.model.download.Download;
import com.mraleksmay.projects.download_manager.plugin.io.dto.category.CategoryDto;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadDto {
    /**
     * Unique download identifier used during serialization.
     */
    @Expose
    @NotNull
    private String dsid = "null";
    /**
     * Download file name.
     */
    @Expose
    @NotNull
    private String fileName;
    /**
     * File extension.
     */
    @Expose
    @NotNull
    private String extension;
    /**
     * Total size.
     */
    @Expose
    @NotNull
    private long fullSize;
    /**
     * Now loaded bytes.
     */
    @Expose
    @NotNull
    private long currentSize;
    /**
     * A folder for temporary storage of data during download.
     */
    @Expose
    @NotNull
    private String tmpDirStr;
    /**
     * The downloaded file will be moved to this directory.
     */
    @Expose
    @NotNull
    private String outputDirStr;
    /**
     * Download url.
     */
    @Expose
    @NotNull
    private String url;
    /**
     * User authentication information.
     */
    @Expose
    @NotNull
    private AuthenticationDataDto authData;
    /**
     * Current download status.
     */
    @NotNull
    private Download.Status status = Download.Status.CREATED;
    @Expose
    private String pluginPSID;
    /**
     * Download category.
     */
    @Expose
    @NotNull
    private String categoryCSID;
    /**
     * Time when the download was added.
     */
    @Expose
    @NotNull
    private long creationTime;


    public DownloadDto() {
    }

    public DownloadDto(String dsid, String fileName, String extension, long fullSize, long currentSize, String tmpDirStr, String outputDirStr, String url, AuthenticationDataDto authData, Download.Status status, CategoryDto category, long creationTime) {
        this.dsid = dsid;
        this.fileName = fileName;
        this.extension = extension;
        this.fullSize = fullSize;
        this.currentSize = currentSize;
        this.tmpDirStr = tmpDirStr;
        this.outputDirStr = outputDirStr;
        this.url = url;
        this.authData = authData;
        this.status = status;
        this.categoryCSID = category.getCSID();
        this.creationTime = creationTime;
    }


    public String getDSID() {
        return dsid;
    }

    public DownloadDto setDSID(String dsid) {
        this.dsid = dsid;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public DownloadDto setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getExtension() {
        return extension;
    }

    public DownloadDto setExtension(String extension) {
        this.extension = extension;
        return this;
    }

    public long getFullSize() {
        return fullSize;
    }

    public DownloadDto setFullSize(long fullSize) {
        this.fullSize = fullSize;
        return this;
    }

    public long getCurrentSize() {
        return currentSize;
    }

    public DownloadDto setCurrentSize(long currentSize) {
        this.currentSize = currentSize;
        return this;
    }

    public File getTempDir() {
        return new File(tmpDirStr);
    }

    public DownloadDto setTempDir(File tempDir) {
        try {
            this.tmpDirStr = tempDir.getCanonicalPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public File getOutputDir() {
        return new File(outputDirStr);
    }

    public DownloadDto setOutputDir(File outputDir) {
        try {
            this.outputDirStr = outputDir.getCanonicalPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public URL getUrl() {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public DownloadDto setUrl(URL url) {
        this.url = url.toString();
        return this;
    }

    public AuthenticationDataDto getAuthData() {
        return authData;
    }

    public DownloadDto setAuthData(AuthenticationDataDto authData) {
        this.authData = authData;
        return this;
    }

    public Download.Status getStatus() {
        return status;
    }

    public DownloadDto setStatus(Download.Status status) {
        this.status = status;
        return this;
    }

    public String getPluginPSID() {
        return pluginPSID;
    }

    public DownloadDto setPluginPSID(String pluginPSID) {
        this.pluginPSID = pluginPSID;
        return this;
    }

    public String getCategoryCSID() {
        return categoryCSID;
    }

    public DownloadDto setCategoryCSID(CategoryDto category) {
        this.categoryCSID = category.getCSID();
        return this;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public DownloadDto setCreationTime(long creationTime) {
        this.creationTime = creationTime;
        return this;
    }

    @Override
    public String toString() {
        return "BaseDownloadMapper{" +
                "dsid='" + dsid + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
