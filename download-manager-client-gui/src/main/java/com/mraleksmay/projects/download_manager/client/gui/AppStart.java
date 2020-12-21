package com.mraleksmay.projects.download_manager.client.gui;

import com.mraleksmay.projects.download_manager.client.gui.platform.view.ViewConfigurator;
import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.ApplicationManager;
import com.mraleksmay.projects.download_manager.client.gui.platform.view.frame.downloader.MainFrame;

import java.io.IOException;

/**
 * Start application.
 */
public class AppStart {
    private static ApplicationManager applicationManager;


    public static void main(String[] args) {
        AppStart appStart = new AppStart();
        appStart.loadApp();
    }

    public void loadApp() {
        ViewConfigurator.configureView();

        MainFrame frame = new MainFrame();
        frame.init();
        applicationManager = new ApplicationManager(frame);

        try {
            applicationManager.init();

            frame.setApplicationManager(applicationManager);
            frame.showFrame();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static ApplicationManager getApplicationManager() {
        return applicationManager;
    }
}