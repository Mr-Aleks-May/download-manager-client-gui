package com.mraleksmay.projects.download_manager.plugin.io.dto.download;

import com.mraleksmay.projects.download_manager.plugin.io.dto.category.SCategoryDto;

public class SDownloadDto {
    /**
     * Download unique identifier.
     */
    private String dsid = "null";
    /**
     * Download file name.
     */
    private String fileName;
    /**
     * File extension.
     */
    private String extension;
    /**
     * A folder for temporary storage of data during download.
     */
    private String tmpDir;
    /**
     * The downloaded file will be moved to this directory.
     */
    private String outputDir;
    /**
     * Download url.
     */
    private String url;
    /**
     * Download category.
     */
    private SCategoryDto categoryDto;
    /**
     * Time when the download was added.
     */
    private long creationTime;


    public SDownloadDto() {
    }


    public String getDSID() {
        return dsid;
    }

    public SDownloadDto setDSID(String DSID) {
        this.dsid = DSID;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public SDownloadDto setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getExtension() {
        return extension;
    }

    public SDownloadDto setExtension(String extension) {
        this.extension = extension;
        return this;
    }

    public String getTmpDir() {
        return tmpDir;
    }

    public SDownloadDto setTmpDir(String tmpDirStr) {
        this.tmpDir = tmpDirStr;
        return this;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public SDownloadDto setOutputDir(String outputDirStr) {
        this.outputDir = outputDirStr;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public SDownloadDto setUrl(String url) {
        this.url = url;
        return this;
    }

    public SCategoryDto getCategoryDto() {
        return categoryDto;
    }

    public SDownloadDto setCategoryDto(SCategoryDto category) {
        this.categoryDto = category;
        return this;
    }

    public Long getCreationTime() {
        return creationTime;
    }

    public SDownloadDto setCreationTime(Long creationTime) {
        this.creationTime = creationTime;
        return this;
    }


    @Override
    public String toString() {
        return this.fileName;
    }
}
