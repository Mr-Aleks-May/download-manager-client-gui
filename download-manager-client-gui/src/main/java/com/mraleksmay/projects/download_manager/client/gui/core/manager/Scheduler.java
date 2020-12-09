package com.mraleksmay.projects.download_manager.client.gui.core.manager;


import com.mraleksmay.projects.download_manager.client.gui.core.impl.model.download.DownloadEntityInformation;
import com.mraleksmay.projects.download_manager.common.exception.DownloadAlreadyStartException;
import com.mraleksmay.projects.download_manager.common.exception.DownloadAlreadyStopException;
import com.mraleksmay.projects.download_manager.common.exception.ThreadAlreadyStartException;
import com.mraleksmay.projects.download_manager.common.exception.ThreadAlreadyStopException;
import com.mraleksmay.projects.download_manager.common.view.UIWorker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Scheduler {
    private List<DownloadEntityInformation> downloadsList;
    private ApplicationManager applicationManager;
    private int queueSize = 8;
    private ScheduleUpdater scheduleUpdater;
    private volatile boolean isActive = false;


    public Scheduler(ApplicationManager applicationManager) {
        this.applicationManager = applicationManager;

        this.scheduleUpdater = new ScheduleUpdater(() -> {
            for (int i = 0; isActive() && i < getQueueSize(); i++) {
                try {
                    getDownloads().get(i).start();
                } catch (DownloadAlreadyStartException e) {
                }
            }
        }, 1000);
    }


    public void add(DownloadEntityInformation download) {
        getDownloadsList().add(download);
    }

    public void remove(DownloadEntityInformation downloadEntityInfo) {
        getDownloadsList().remove(downloadEntityInfo);
    }

    public void moveUp(DownloadEntityInformation download) {
        int index = getDownloads().indexOf(download);

        if (index > 0) {
            DownloadEntityInformation tmp = getDownloadsList().get(index - 1);
            getDownloadsList().set(index - 1, download);
            getDownloadsList().set(index, tmp);
        }
    }

    public void moveDown(DownloadEntityInformation download) {
        int index = getDownloads().indexOf(download);

        if (index < getDownloads().size() - 1) {
            DownloadEntityInformation tmp = getDownloadsList().get(index + 1);
            getDownloadsList().set(index + 1, download);
            getDownloadsList().set(index, tmp);
        }
    }

    public void start() {
        setActive(true);
        try {
            getScheduleUpdater().start();
        } catch (ThreadAlreadyStartException threadAlreadyStartException) {
        }

        for (int i = 0; i < getQueueSize(); i++) {
            try {
                getDownloads().get(i).start();
            } catch (DownloadAlreadyStartException e) {
            }
        }
    }

    public void stop() {
        setActive(false);

        try {

            getScheduleUpdater().stop();
        } catch (ThreadAlreadyStopException threadAlreadyStopException) {
        }

        for (DownloadEntityInformation download : getDownloads()) {
            try {
                download.stop();
            } catch (DownloadAlreadyStopException e) {
            }
        }
    }


    private synchronized List<DownloadEntityInformation> getDownloadsList() {
        if (downloadsList == null) {
            downloadsList = new ArrayList<>();
        }

        return downloadsList;
    }

    public synchronized List<DownloadEntityInformation> getDownloads() {
        return Collections.unmodifiableList(getDownloadsList());
    }

    public ApplicationManager getApplicationManager() {
        return applicationManager;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public ScheduleUpdater getScheduleUpdater() {
        return scheduleUpdater;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }
}


class ScheduleUpdater extends UIWorker {
    public ScheduleUpdater(Runnable uiUpdateAction, int millis) {
        super(uiUpdateAction, millis);
    }
}