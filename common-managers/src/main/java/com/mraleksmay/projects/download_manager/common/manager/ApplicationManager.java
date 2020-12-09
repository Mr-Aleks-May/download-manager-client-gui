package com.mraleksmay.projects.download_manager.common.manager;

import com.mraleksmay.projects.download_manager.plugin.model.download.DownloadEntityInformation;
import com.sun.source.util.Plugin;

import java.io.IOException;

public interface ApplicationManager {
    void init() throws IOException, ClassNotFoundException;

    void add(Plugin plugin);

    void remove(Plugin plugin);

    void add(DownloadEntityInformation downloadEntityInfo);

    void remove(DownloadEntityInformation downloadEntityInfo);

    void removeAll();

    DownloadsManager getDownloadsManager();

    PluginsManager getPluginsManager();

    IOManager getIOManager();

    Scheduler getScheduler();

    RESTClient getClientManager();
}
