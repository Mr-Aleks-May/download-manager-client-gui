package com.mraleksmay.projects.download_manager.client.gui.core.manager.rest;

import com.google.gson.Gson;
import com.mraleksmay.projects.download_manager.client.gui.core.impl.model.download.DownloadEntityInformation;
import com.mraleksmay.projects.download_manager.client.gui.core.impl.plugin.common.model.category.CommonCategory;
import com.mraleksmay.projects.download_manager.client.gui.core.impl.plugin.common.model.download.CommonDownload;
import com.mraleksmay.projects.download_manager.client.gui.core.impl.plugin.common.model.download.CommonDownloadFormatter;
import com.mraleksmay.projects.download_manager.client.gui.core.impl.plugin.common.model.group.CommonGroup;
import com.mraleksmay.projects.download_manager.client.gui.core.impl.plugin.common.model.plugin.CommonPlugin;
import com.mraleksmay.projects.download_manager.client.gui.core.manager.ApplicationManager;
import com.mraleksmay.projects.download_manager.client.gui.core.manager.IOManager;
import com.mraleksmay.projects.download_manager.client.gui.core.util.downloader.BasicNetWorker;
import com.mraleksmay.projects.download_manager.common.exception.UserAlreadyExistsException;
import com.mraleksmay.projects.download_manager.common.exception.UserNotFoundException;
import com.mraleksmay.projects.download_manager.common.model.category.Category;
import com.mraleksmay.projects.download_manager.common.model.download.AuthenticationData;
import com.mraleksmay.projects.download_manager.common.model.download.Download;
import com.mraleksmay.projects.download_manager.common.model.download.DownloadFormatter;
import com.mraleksmay.projects.download_manager.common.model.group.Group;
import com.mraleksmay.projects.download_manager.common.model.plugin.Plugin;
import kong.unirest.HttpResponse;
import kong.unirest.HttpStatus;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RESTClient {
    private ApplicationManager applicationManager;
    private String host = "http://localhost:8080";
    private String token;


    public RESTClient(ApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
    }


    public String signUp(String login, String password) throws UserAlreadyExistsException {
        HttpResponse<JsonNode> response = Unirest.post(host + "/api/signup").
                header("accept", "application/json").
                queryString("login", login).
                queryString("password", password).
                asJson();


        if (response.getStatus() == HttpStatus.CONFLICT) {
            throw new UserAlreadyExistsException();
        }

        if (response.getStatus() == HttpStatus.OK) {
            String token = response.getBody().toString();
            return token;
        }

        return null;
    }

    public String signIn(String login, String password) throws UserNotFoundException {
        HttpResponse<JsonNode> response = Unirest.post(host + "/api/signin").
                header("accept", "application/json").
                queryString("login", login).
                queryString("password", password).
                asJson();

        System.out.println(response);


        if (response.getStatus() == HttpStatus.NOT_FOUND) {
            throw new UserNotFoundException();
        }

        if (response.getStatus() == HttpStatus.OK) {
            String token = response.getBody().toString();
            return token;
        }

        return null;
    }


    public void sync(String token) {
        final List<Download> newDownloadsList = getApplicationManager().
                getDownloadsManager().
                getDownloads().
                stream().
                map(DownloadEntityInformation::getDownload).
                collect(Collectors.toList());
        // Получаем спиоск загрузок с сервера
        final List<DownloadEntityInformation> oldDownloadsList = getDownloads(token);
        // Делаем копию локального списка загрузок
//        final List<Download> newDownloadsList = getApplicationManager().
//                getDownloadsManager().
//                getDownloads().
//                stream().
//                map(DownloadEntityInformation::getDownload).
//                collect(Collectors.toList());

        uploadDownloads(token, newDownloadsList);


    }

    public List<DownloadEntityInformation> getDownloads(String token) {
        final HttpResponse<JsonNode> response = Unirest.get(host + "/api/download/get_all/").
                header("accept", "application/json").
                queryString("token", token).
                asJson();


        if (response.getStatus() == HttpStatus.OK) {
            final String downloadsJson = response.getBody().toString();
            final Gson gson = new Gson().newBuilder().excludeFieldsWithoutExposeAnnotation().create();
            final List<Map<String, Object>> downloadsInfos = gson.fromJson(downloadsJson, List.class);

            final List<DownloadEntityInformation> downloadsList = new ArrayList<>();

            for (Map<String, Object> downloadInfo : downloadsInfos) {
                try {
                    Download download = deserialize(downloadInfo);
                    downloadsList.add(new DownloadEntityInformation(download, new BasicNetWorker()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return downloadsList;
        }

        return null;
    }

    public void uploadDownloads(String token, List<Download> downloads) {
        final Gson gson = new Gson().newBuilder().excludeFieldsWithoutExposeAnnotation().create();
        final String downloadsJson = gson.toJson(downloads);

        final HttpResponse<JsonNode> response = Unirest.post(host + "/api/download/add_all/").
                header("accept", "application/json").
                queryString("token", token).
                queryString("downloads", downloadsJson).
                asJson();


        if (response.getStatus() == HttpStatus.OK) {
        }
    }

    public Download deserialize(Map<String, Object> dmap) throws Exception {
        final String serializableId = (String) dmap.getOrDefault("serializableId", "null");
        final String fileName = (String) dmap.getOrDefault("fileName", "null");
        final String url = (String) dmap.getOrDefault("url", "null");
        final String outputDirStr = (String) dmap.getOrDefault("outputDirStr", "null");
        final String tmpDirStr = (String) dmap.getOrDefault("tmpDirStr", "null");
        final String classStr = (String) dmap.getOrDefault("classStr", "null");
        final double addTime = (Double) dmap.getOrDefault("addTime", 0);
        final String formatterClassStr = (String) dmap.getOrDefault("formatterClassStr", "null");

        final Map<String, Object> cmap = (Map<String, Object>) dmap.getOrDefault("category", new HashMap());
        final String categoryId = (String) cmap.getOrDefault("serializableId", "null");
        Category category = getApplicationManager().getPluginsManager().findCategoryBy(categoryId);

        Download download = (Download) Class.forName(classStr).newInstance();
        DownloadFormatter formatter = (DownloadFormatter) Class.forName(formatterClassStr).newInstance();

        download.setCategory(category);
        download.setSerializableId(serializableId);
        download.setFileName(fileName);
        download.setUrl(new URL(url));
        download.setOutputDir(new File(outputDirStr));
        download.setTmpDir(new File(tmpDirStr));
        download.setTime((long) addTime);
        download.setFormatter(new CommonDownloadFormatter());

        return download;
    }


    public static void main(String[] args) throws IOException {
        test1();
    }

    private static void test1() throws IOException {
        RESTClient cm = new RESTClient(null);
        List<Download> downloadsList = new ArrayList<>();


        String name;
        final URL url;
        AuthenticationData authData = null;
        final Category category;
        String urlStr = "https://learnjava.co.in/hibernate-list-mapping/";

        final Plugin plugin = new CommonPlugin();
        final Group all = new CommonGroup("ALL", "All", plugin);
        plugin.add(all);
        // CATEGORY ALL
        String id = "ALL";
        name = "All";
        String contentType = "all";
        String extensions = "";
        File tmpDir = new File("");
        File outputDir = new File("");
        category = new CommonCategory(id, name, contentType, extensions, outputDir, plugin);
        category.setEditable(false);
        category.setRemovable(false);
        all.add(category);

        try {
            name = Download.suggestNameFrom(urlStr);
            if (name.length() > 100) name = name.substring(0, 100);
            url = new URL(urlStr);

            final Download download = new CommonDownload(name, url, category.getOutputDir(), "file", authData, category, 0);
            download.setFormatter(new CommonDownloadFormatter());
            final DownloadEntityInformation downloadEntityInformation = new DownloadEntityInformation(download, new BasicNetWorker());

            downloadsList.add(download);
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            String token = cm.signUp("test", "test");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String token = cm.signIn("test", "test");
            cm.uploadDownloads(token, downloadsList);
            cm.getDownloads(token);

            int t = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ApplicationManager getApplicationManager() {
        return applicationManager;
    }
}
