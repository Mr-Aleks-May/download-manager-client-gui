package com.mraleksmay.projects.download_manager.common.view;


import com.mraleksmay.projects.download_manager.plugin.model.download.Download;

/**
 * Allows you to set and get the download with which we will work in the dialogue.
 */
public interface DownloadDialog {
    /**
     * Set basic information about download.
     *
     * @param downloadInformation
     */
    void setDownloadInformation(Download downloadInformation);

    /**
     * Get download information after user modification.
     *
     * @return
     */
    Download getDownload();
}
