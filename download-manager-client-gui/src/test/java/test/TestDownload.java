package test;

import com.mraleksmay.projects.download_manager.plugin.model.download.AuthenticationData;
import com.mraleksmay.projects.download_manager.plugin.model.download.Download;
import com.mraleksmay.projects.download_manager.plugin.model.download.DownloadFormatter;

public class TestDownload extends Download {
    private DownloadFormatter formatter = new TestFormatter();
    private TestAuthenticationData testAuthenticationData = new TestAuthenticationData();


    @Override
    public DownloadFormatter getFormatter() {
        return formatter;
    }

    @Override
    public Download setFormatter(DownloadFormatter formatter) {
        return null;
    }

    @Override
    public AuthenticationData getAuthData() {
        return testAuthenticationData;
    }

    @Override
    public Download setAuthData(String login, String password) {
        return null;
    }

    @Override
    public Download setAuthData(AuthenticationData authData) {
        return null;
    }
}
