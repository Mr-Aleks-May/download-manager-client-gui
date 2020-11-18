package com.mralexmay.projects.download_manager.model.download.auth;

import com.mralexmay.projects.download_manager.core.model.download.auth.AuthData;
import com.mralexmay.projects.download_manager.core.annotation.NotNull;

import java.nio.charset.Charset;
import java.util.Base64;

public class CoreAuthData implements AuthData {
    @NotNull
    private final String login;
    @NotNull
    private final String password;

    public CoreAuthData(@NotNull String login,
                        @NotNull String password) {
        this.login = login;
        this.password = password;
    }

    @Override
    public String getAuthData() {
        String authData = String.format("%s:%s", login, password);
        return Base64.getEncoder().encodeToString(authData.getBytes(Charset.forName("UTF8")));
    }

    @Override
    public String getLogin() {
        return login;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
