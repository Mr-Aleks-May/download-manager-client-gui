package com.mraleksmay.projects.download_manager.common.util;

import java.util.Random;

public class DownloadUtil {


    public static String suggestNameFrom(String url) {
        String name = "";
        String urlStr = url + "";
        urlStr = urlStr.trim();

        if (urlStr.charAt(url.length() - 1) == '/') {
            urlStr = urlStr.substring(0, urlStr.length() - 1);
        }

        int lIndex = urlStr.lastIndexOf('/');

        if (lIndex >= 0) {
            name = urlStr.substring(lIndex + 1);
        } else {
            name = new Random().nextInt() + "";
        }

        return name.trim();
    }


    public static String suggestNameFromName(String name) {
        return name
                .trim()
                .replaceAll("([^a-zA-Z0-9]+)", "_");
    }
}
