package com.mraleksmay.projects.download_manager.common.model.download;


import com.google.gson.annotations.Expose;
import com.mraleksmay.projects.download_manager.common.annotation.NotNull;

/**
 * Contains authentication user data.
 */
public abstract class AuthenticationData {
    /**
     * User login.
     */
    @Expose
    @NotNull
    private String login;
    /**
     * User password.
     */
    @Expose
    @NotNull
    private String password;
    /**
     * The class of a specific implementation of this AuthenticationData.
     * Used when deserializing.
     */
    @Expose
    private String classStr;


    {
        setClassStr(this.getClass());
    }

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

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClassStr() {
        return classStr;
    }

    public void setClassStr(Class<? extends AuthenticationData> clazz) {
        this.classStr = clazz.getCanonicalName();
    }
}
