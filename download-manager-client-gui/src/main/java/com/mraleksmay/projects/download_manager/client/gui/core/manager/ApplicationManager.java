package com.mraleksmay.projects.download_manager.client.gui.core.manager;


import com.mraleksmay.projects.download_manager.client.gui.core.impl.model.download.DownloadEntityInformation;
import com.mraleksmay.projects.download_manager.client.gui.core.manager.rest.RESTClient;
import com.mraleksmay.projects.download_manager.client.gui.core.view.frame.downloader.MainFrame;
import com.mraleksmay.projects.download_manager.client.gui.core.view.frame.downloader.controller.MainViewController;
import com.mraleksmay.projects.download_manager.common.model.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ApplicationManager {
    private MainFrame mainFrame;
    private DownloadsManager downloadsManager = new DownloadsManager(this);
    private PluginsManager pluginsManager = new PluginsManager(this);
    private IOManager ioManager = new IOManager(this);
    private MainViewController mainViewController;
    private Scheduler scheduler = new Scheduler(this);
    private RESTClient clientManager = new RESTClient(this);
    private PluginDataManager pluginDataManager = new PluginDataManager(this);


    public ApplicationManager(MainFrame mainFrame) {
        mainViewController = new MainViewController(this, mainFrame);
        this.mainFrame = mainFrame;
    }


    public void init() throws IOException, ClassNotFoundException {
        final File pluginsDir = new File("./plugins").getCanonicalFile();

        getPluginsManager().loadPlugins(pluginsDir);
        getPluginsManager().initPlugin();

        getMainViewController().init();
    }

    public void add(Plugin plugin) {
        getPluginsManager().add(plugin);
    }

    public void remove(Plugin plugin) {
        getPluginsManager().remove(plugin);
    }

    public void add(DownloadEntityInformation downloadEntityInfo) {
        getDownloadsManager().add(downloadEntityInfo);
        getMainViewController().add(downloadEntityInfo);
        getScheduler().add(downloadEntityInfo);
    }

    public void remove(DownloadEntityInformation downloadEntityInfo) {
        getDownloadsManager().remove(downloadEntityInfo);
        getScheduler().remove(downloadEntityInfo);
    }

    public void removeAll() {
        final List<DownloadEntityInformation> downloads = getDownloadsManager().getDownloads();

        for (DownloadEntityInformation download : downloads) {
            try {
                download.stop();
            } catch (Exception e) {
            }

            getScheduler().remove(download);
        }

        getDownloadsManager().removeAll();
    }


    public DownloadsManager getDownloadsManager() {
        return downloadsManager;
    }

    public PluginsManager getPluginsManager() {
        return pluginsManager;
    }

    public MainViewController getMainViewController() {
        return mainViewController;
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public IOManager getIOManager() {
        return ioManager;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public RESTClient getClientManager() {
        return clientManager;
    }

    public PluginDataManager getPluginDataManager() {
        return pluginDataManager;
    }
}
