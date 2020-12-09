package com.mraleksmay.projects.download_manager.client.gui.core.impl.plugin.common.model.download;


import com.mraleksmay.projects.download_manager.common.model.download.AuthenticationData;

import java.nio.charset.Charset;
import java.util.Base64;

public class Base64AuthenticationData extends AuthenticationData {
    public Base64AuthenticationData(String login, String password) {
        super(login, password);
    }

    public String getBase64AuthorizationData() {
        String authData = String.format("%s:%s", getLogin(), getPassword());
        return Base64.getEncoder().encodeToString(authData.getBytes(Charset.forName("UTF8")));
    }
}
