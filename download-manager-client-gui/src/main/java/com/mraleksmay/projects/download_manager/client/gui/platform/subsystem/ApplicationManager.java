package com.mraleksmay.projects.download_manager.client.gui.platform.subsystem;


import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.downloads.DownloadsManager;
import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.io.IOManagerCurrent;
import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.plugins.PluginDataManagerImpl;
import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.plugins.PluginsManager;
import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.downloads.Scheduler;
import com.mraleksmay.projects.download_manager.client.gui.platform.model.download.DownloadEntityInformation;
import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.communication.RESTClient;
import com.mraleksmay.projects.download_manager.client.gui.platform.view.frame.downloader.MainFrame;
import com.mraleksmay.projects.download_manager.client.gui.platform.view.frame.downloader.controller.MainViewController;
import com.mraleksmay.projects.download_manager.plugin.model.plugin.Plugin;
import com.mraleksmay.projects.download_manager.plugin.manager.PluginDataManager;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ApplicationManager {
    private MainFrame mainFrame;
    private DownloadsManager downloadsManager = new DownloadsManager(this);
    private PluginsManager pluginsManager = new PluginsManager(this);
    private IOManagerCurrent ioManager = new IOManagerCurrent(this);
    private MainViewController mainViewController;
    private Scheduler scheduler = new Scheduler(this);
    private RESTClient clientManager = new RESTClient(this);
    private PluginDataManagerImpl pluginDataManager = new PluginDataManagerImpl(this);


    public ApplicationManager(MainFrame mainFrame) {
        mainViewController = new MainViewController(this, mainFrame);
        this.mainFrame = mainFrame;
    }

    public ApplicationManager(MainFrame mainFrame,
                              DownloadsManager downloadsManager,
                              PluginsManager pluginsManager,
                              IOManagerCurrent ioManager,
                              MainViewController mainViewController,
                              Scheduler scheduler,
                              RESTClient clientManager,
                              PluginDataManagerImpl pluginDataManager) {
        this.mainFrame = mainFrame;
        this.downloadsManager = downloadsManager;
        this.pluginsManager = pluginsManager;
        this.ioManager = ioManager;
        this.mainViewController = mainViewController;
        this.scheduler = scheduler;
        this.clientManager = clientManager;
        this.pluginDataManager = pluginDataManager;
    }


    public void init() throws IOException, ClassNotFoundException {
        final File pluginsDir = new File("./plugins").getCanonicalFile();

        getPluginsManager().loadPlugins(pluginsDir);
        getPluginsManager().initPlugin();

        getMainViewController().init();
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

    public IOManagerCurrent getIOManager() {
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
