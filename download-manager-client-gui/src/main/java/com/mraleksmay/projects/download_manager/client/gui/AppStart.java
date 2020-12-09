package com.mraleksmay.projects.download_manager.client.gui;

import com.mraleksmay.projects.download_manager.client.gui.core.loader.CommonsLoader;
import com.mraleksmay.projects.download_manager.client.gui.core.view.ViewConfigurator;
import com.mraleksmay.projects.download_manager.client.gui.core.manager.ApplicationManager;
import com.mraleksmay.projects.download_manager.client.gui.core.view.frame.downloader.MainFrame;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

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