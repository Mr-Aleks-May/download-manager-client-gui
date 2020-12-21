package com.mraleksmay.projects.download_manager.plugin.model.download;


import com.mraleksmay.projects.download_manager.common.annotation.NotNull;
import com.mraleksmay.projects.download_manager.common.annotation.Serialize;

public abstract class AuthenticationData {
    @Serialize
    @NotNull
    private String login;
    @Serialize
    @NotNull
    private String password;


    // Constructors
    public AuthenticationData() {
    }

    public AuthenticationData(@NotNull String login,
                              @NotNull String password) {
        this.login = login;
        this.password = password;
    }


    // Getters and Setters
    public String getLogin() {
        return login;
    }

    public AuthenticationData setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public AuthenticationData setPassword(String password) {
        this.password = password;
        return this;
    }


    @Override
    public String toString() {
        return "AuthenticationData{" +
                "login='" + login + '\'' +
                '}';
    }
}
