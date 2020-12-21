package com.mraleksmay.projects.download_manager.plugin.model.user;

import com.mraleksmay.projects.download_manager.plugin.model.user.settings.Settings;

public class User {
    private static User currentUser;
    public String token;
    private Settings settings = new Settings();


    private static void createUser() {
        currentUser = new User();
    }

    public static User getCurrentUser() {
        if (currentUser == null) {
            createUser();
        }

        return currentUser;
    }

    public Settings getSettings() {
        return settings;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public static class NetworkSettings {
        private static String host = "http://localhost";
        private static int port = 7000;


        public static String getHost() {
            return host;
        }

        public static void setHost(String host) {
            NetworkSettings.host = host;
        }

        public static int getPort() {
            return port;
        }

        public static void setPort(int port) {
            NetworkSettings.port = port;
        }

        public static String getAddress() {
            return String.format("%s:%s", host, port);
        }
    }
}
