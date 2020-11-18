package com.mralexmay.projects.download_manager.model.download.local_group;

import com.mralexmay.projects.download_manager.core.model.download.Download;
import com.mralexmay.projects.download_manager.core.model.download.local_group.DownloadsFilter;
import com.mralexmay.projects.download_manager.core.model.download.meta_information.DownloadMetaInfo;
import com.mralexmay.projects.download_manager.core.annotation.NotNull;
import com.mralexmay.projects.download_manager.controller.LocalGroupsController;

import java.util.ArrayList;
import java.util.List;

public class CoreDownloadsFilter implements DownloadsFilter {
    private final LocalGroupsController gController;


    public CoreDownloadsFilter(@NotNull LocalGroupsController gController) {
        this.gController = gController;
    }


    @Override
    public synchronized List<DownloadMetaInfo> getDownloadsToDisplay(@NotNull final List<DownloadMetaInfo> downloads) {
        List<DownloadMetaInfo> mDownloads = new ArrayList<>();

        for (DownloadMetaInfo mDownload : downloads) {
            final Download download = mDownload.getDownload();

            if (getLocalGroupsController().getCurrentSelectedGroupId().equals("ALL")) {
                if (download.getCategory().getId().equals(getLocalGroupsController().getCurrentSelectedCategoryId())) {
                    mDownloads.add(mDownload);
                } else if (getLocalGroupsController().getCurrentSelectedCategoryId().equals("ALL")) {
                    mDownloads.add(mDownload);
                }
            } else if (getLocalGroupsController().getCurrentSelectedGroupId().equals("UNFINISHED")) {
                if (download.getStatus() != Download.Status.DOWNLOADED) {
                    if (download.getCategory().getId().equals(getLocalGroupsController().getCurrentSelectedCategoryId())) {
                        mDownloads.add(mDownload);
                    } else if (getLocalGroupsController().getCurrentSelectedCategoryId().equals("ALL")) {
                        mDownloads.add(mDownload);
                    }
                }
            } else if (getLocalGroupsController().getCurrentSelectedGroupId().equals("COMPLETED")) {
                if (download.getStatus() == Download.Status.DOWNLOADED) {
                    if (download.getCategory().getId().equals(getLocalGroupsController().getCurrentSelectedCategoryId())) {
                        mDownloads.add(mDownload);
                    } else if (getLocalGroupsController().getCurrentSelectedCategoryId().equals("ALL")) {
                        mDownloads.add(mDownload);
                    }
                }
            }
        }

        return mDownloads;
    }

    public LocalGroupsController getLocalGroupsController() {
        return gController;
    }
}
