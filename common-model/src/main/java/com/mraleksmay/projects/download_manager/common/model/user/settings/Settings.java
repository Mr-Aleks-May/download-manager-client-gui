package com.mraleksmay.projects.download_manager.common.model.user.settings;

import java.io.File;
import java.io.IOException;

public class Settings {
    private DownloaderSettings downloaderSettings = new DownloaderSettings();
    private UISettings uiSettings = new UISettings();


    public void loadSettings(File pathToFile) {

    }

    public DownloaderSettings getDownloaderSettings() {
        return downloaderSettings;
    }

    public UISettings getUISettings() {
        return uiSettings;
    }


    public class DownloaderSettings {
        private File tmpDir;


        public File getTmpDir() throws IOException {
            if (tmpDir == null) {
                tmpDir = new File(System.getenv("HOME") + "/Downloader/tmp").getCanonicalFile();

                if (!tmpDir.exists()) {
                    tmpDir.mkdirs();
                }
            }

            return tmpDir;
        }
    }


    public class UISettings {
        private int updateUIMillis = 500;

        public int getUpdateUIMillis() {
            return updateUIMillis;
        }
    }
}
