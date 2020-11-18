package com.mralexmay.projects.download_manager.view.dialogs.download;

import com.mralexmay.projects.download_manager.core.annotation.NotNull;
import com.mralexmay.projects.download_manager.core.exception.RetrieveInformationThreadAlreadyRunning;
import com.mralexmay.projects.download_manager.model.download.CorePreDownloadInfo;
import com.mralexmay.projects.download_manager.core.model.download.category.Category;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateUI {
    private final AddCDownloadDialog addCDownloadDialog;
    private Thread uiUpdater;


    public UpdateUI(AddCDownloadDialog addCDownloadDialog, @NotNull final CorePreDownloadInfo pDownload) {
        this.addCDownloadDialog = addCDownloadDialog;
        uiUpdater = new Thread(() -> {
            try {
                SwingUtilities.invokeAndWait(() -> {
                    try {
                        setValuesFrom(pDownload);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                retrieveInformation(pDownload);

                SwingUtilities.invokeLater(() -> {
                    displayNewInformationInUI(pDownload);
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }


    private synchronized void setValuesFrom(@NotNull final CorePreDownloadInfo pDownload) throws IOException {
        getDialog().getJTF_name().setText(pDownload.getFileName());
        getDialog().getJTF_url().setText(pDownload.getFormatter().getUrl() + "");
        getDialog().getJTF_fullPath().setText(pDownload.getFormatter().getFullPathToFile());
    }

    public synchronized void retrieveInformation(@NotNull final CorePreDownloadInfo download) {
        final URL url = getDialog().getPreDownloadInfo().getUrl();
        HttpURLConnection conn = null;

        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
            conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.123 Safari/537.36");
            conn.setInstanceFollowRedirects(true);

            final int cLength = conn.getContentLength();
            final String cType = conn.getContentType().trim();
            final String nameStr = CorePreDownloadInfo.getNameFrom(conn.getURL() + "");
            final Category category = getDialog().getLocalGroupsController().getLocalGroupBy("CORE").tryDeterminateCategoryBy(url, nameStr, cType);

            if (!getDialog().isUserChangeFileName()) {
                getDialog().getPreDownloadInfo().setFileName(nameStr);
                getDialog().getPreDownloadInfo().setOutputDir(category.getOutputDir());
            }
            getDialog().getPreDownloadInfo().setContentType(cType);
            getDialog().getPreDownloadInfo().setFullSize(cLength);
            if (!getDialog().isUserSelectCategory())
                getDialog().getPreDownloadInfo().setCategory(category);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn == null) {
                conn.disconnect();
            }
        }
    }

    private synchronized void displayNewInformationInUI(@NotNull final CorePreDownloadInfo pDownload) {
        getDialog().getJTF_name().setText(pDownload.getFormatter().getName());
        getDialog().getJLBL_size().setText(pDownload.getFormatter().getFullSize());
        getDialog().getJLBL_type().setText(pDownload.getFormatter().getType());
        getDialog().getJCB_category().setSelectedItem(pDownload.getCategory());
    }

    public void start() throws RetrieveInformationThreadAlreadyRunning {
        if (this.uiUpdater.isAlive()) {
            throw new RetrieveInformationThreadAlreadyRunning();
        }

        uiUpdater.start();
    }

    public void stop() {
        uiUpdater.stop();
    }

    public AddCDownloadDialog getDialog() {
        return addCDownloadDialog;
    }
}
