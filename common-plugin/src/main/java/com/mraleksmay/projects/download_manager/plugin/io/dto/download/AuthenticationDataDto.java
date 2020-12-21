package com.mraleksmay.projects.download_manager.plugin.io.dto.download;

import com.google.gson.annotations.Expose;
import com.mraleksmay.projects.download_manager.common.annotation.NotNull;

public  class AuthenticationDataDto {
    @Expose
    @NotNull
    private String login;
    @Expose
    @NotNull
    private String password;


    public AuthenticationDataDto() {
    }

    public AuthenticationDataDto(String login, String password) {
        this.login = login;
        this.password = password;
    }


    public String getLogin() {
        return login;
    }

    public AuthenticationDataDto setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public AuthenticationDataDto setPassword(String password) {
        this.password = password;
        return this;
    }


    @Override
    public String toString() {
        return "BaseCommonAuthenticationDataDto{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
