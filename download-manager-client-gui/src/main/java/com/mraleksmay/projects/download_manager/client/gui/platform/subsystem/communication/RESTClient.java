package com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.ApplicationManager;
import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.communication.service.marketplace.dto.PluginPublishDto;
import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.communication.service.marketplace.dto.PluginRemoveDto;
import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.communication.service.marketplace.dto.PluginSearchResultDto;
import com.mraleksmay.projects.download_manager.plugin.io.dto.download.SDownloadDto;
import com.mraleksmay.projects.download_manager.common.exception.UserAlreadyExistsException;
import com.mraleksmay.projects.download_manager.common.exception.UserNotFoundException;
import com.mraleksmay.projects.download_manager.plugin.model.user.User;
import kong.unirest.HttpResponse;
import kong.unirest.HttpStatus;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class RESTClient {
    private ApplicationManager applicationManager;
    private String host = "http://localhost:7000";
    private String token;


    public RESTClient(ApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
    }


    public static class UserService {
        private static final String BASE_PATH = "/api/user";


        /**
         * Register new user.
         *
         * @param login
         * @param password
         * @return token.
         * @throws UserAlreadyExistsException
         */
        public static String signUp(String login, String password) throws UserAlreadyExistsException {
            HttpResponse<JsonNode> response = Unirest.post(User.NetworkSettings.getAddress() + BASE_PATH + "/signup/").
                    header("accept", "application/json").
                    queryString("login", login).
                    queryString("password", password).
                    asJson();

            if (response.getStatus() == HttpStatus.CONFLICT) {
                throw new UserAlreadyExistsException();
            }

            if (response.getStatus() == HttpStatus.OK) {
                String responseJson = response.getBody().toString();
                Gson gson = new GsonBuilder().create();
                Map<String, Object> responseToObj = gson.fromJson(responseJson, HashMap.class);

                String token = (String) responseToObj.get("token");
                return token;
            }

            return null;
        }

        public static String signIn(String login, String password) throws UserNotFoundException {
            HttpResponse<JsonNode> response = Unirest.post(User.NetworkSettings.getAddress() + BASE_PATH + "/signin/").
                    header("accept", "application/json").
                    queryString("login", login).
                    queryString("password", password).
                    asJson();

            System.out.println(response);


            if (response.getStatus() == HttpStatus.NOT_FOUND) {
                throw new UserNotFoundException();
            }

            if (response.getStatus() == HttpStatus.OK) {
                String responseJson = response.getBody().toString();
                Gson gson = new GsonBuilder().create();
                Map<String, Object> responseToObj = gson.fromJson(responseJson, HashMap.class);

                String token = (String) responseToObj.get("token");
                return token;
            }

            return null;
        }


        public static String removeUserAccount(String token) throws UserNotFoundException {
            HttpResponse<JsonNode> response = Unirest.delete(User.NetworkSettings.getAddress() + BASE_PATH + "/remove/").
                    header("accept", "application/json").
                    queryString("token", token).
                    asJson();

            System.out.println(response.getStatus());


            if (response.getStatus() == HttpStatus.NOT_FOUND) {
                throw new UserNotFoundException();
            }

            return response.getStatus() + "";
        }

        public static SDownloadDto getDownloadFromList(String token, String downloadDSID) throws Exception {
            HttpResponse<SDownloadDto> response = Unirest.get(User.NetworkSettings.getAddress() + BASE_PATH + "/download/get")
                    .header("accept", "application/json")
                    .header("content-type", "application/json")
                    .queryString("token", token)
                    .queryString("dsid", downloadDSID)
                    .asObject(SDownloadDto.class);

            System.out.println(response.getStatus());


            if (response.getStatus() == HttpStatus.NOT_FOUND) {
                throw new UserNotFoundException();
            }
            if (response.getStatus() == HttpStatus.OK) {
                return response.getBody();
            }


            throw new Exception();
        }

        public static List<SDownloadDto> getAllDownloadsFromUser(String token) throws Exception {
            HttpResponse<SDownloadDto[]> response = Unirest.get(User.NetworkSettings.getAddress() + BASE_PATH + "/download/get/list")
                    .header("accept", "application/json")
                    .header("content-type", "application/json")
                    .queryString("token", token)
                    .asObject(SDownloadDto[].class);

            System.out.println(response.getStatus());


            if (response.getStatus() == HttpStatus.NOT_FOUND) {
                throw new UserNotFoundException();
            }
            if (response.getStatus() == HttpStatus.OK) {
                return Arrays.asList(response.getBody());
            }


            throw new Exception();
        }

        public static String addDownloadToUser(String token, SDownloadDto downloadDto) throws UserNotFoundException {
            HttpResponse<JsonNode> response = Unirest.post(User.NetworkSettings.getAddress() + BASE_PATH + "/download/add")
                    .header("accept", "application/json")
                    .header("content-type", "application/json")
                    .queryString("token", token)
                    .body(downloadDto)
                    .asJson();

            if (response.getStatus() == HttpStatus.NOT_FOUND) {
                throw new UserNotFoundException();
            }


            return response.getStatus() + "";
        }

        public static String addDownloadListToUser(String token, List<SDownloadDto> downloadsDto) throws UserNotFoundException {
            HttpResponse<JsonNode> response = Unirest.post(User.NetworkSettings.getAddress() + BASE_PATH + "/download/add/list")
                    .header("accept", "application/json")
                    .header("content-type", "application/json")
                    .queryString("token", token)
                    .body(downloadsDto)
                    .asJson();

            if (response.getStatus() == HttpStatus.NOT_FOUND) {
                throw new UserNotFoundException();
            }


            return response.getStatus() + "";
        }

        public static String removeDownloadFromList(String token, String downloadDSID) throws Exception {
            HttpResponse<JsonNode> response = Unirest.delete(User.NetworkSettings.getAddress() + BASE_PATH + "/download/delete")
                    .header("accept", "application/json")
                    .header("content-type", "application/json")
                    .queryString("token", token)
                    .queryString("dsid", downloadDSID)
                    .asJson();

            System.out.println(response.getStatus());


            if (response.getStatus() == HttpStatus.NOT_FOUND) {
                throw new UserNotFoundException();
            }
            if (response.getStatus() == HttpStatus.OK) {
                return response.getStatus() + "";
            }


            throw new Exception();
        }


        public static String removeDownloadList(String token, List<SDownloadDto> downloadsDto) throws UserNotFoundException {
            HttpResponse<JsonNode> response = Unirest.delete(User.NetworkSettings.getAddress() + BASE_PATH + "/download/delete/list")
                    .header("accept", "application/json")
                    .header("content-type", "application/json")
                    .queryString("token", token)
                    .body(downloadsDto.stream()
                            .map((d) -> d.getDSID())
                            .collect(toList()))
                    .asJson();

            if (response.getStatus() == HttpStatus.NOT_FOUND) {
                throw new UserNotFoundException();
            }


            return response.getStatus() + "";
        }
    }

    public static class MarketplaceService {
        private static final String BASE_PATH = "/api/store/plugin";


        public static String publishPlugin(String token, PluginPublishDto pluginPublishDto) throws UserNotFoundException {
            HttpResponse<JsonNode> response = Unirest.post(User.NetworkSettings.getAddress() + BASE_PATH + "/publish")
                    .header("accept", "application/json")
                    .header("content-type", "application/json")
                    .queryString("token", token)
                    .body(pluginPublishDto)
                    .asJson();

            if (response.getStatus() == HttpStatus.NOT_FOUND) {
                throw new UserNotFoundException();
            }


            return response.getStatus() + "";
        }

        public static String removePlugin(String token, PluginRemoveDto pluginRemoveDto) throws UserNotFoundException {
            HttpResponse<JsonNode> response = Unirest.delete(User.NetworkSettings.getAddress() + BASE_PATH + "/remove")
                    .header("accept", "application/json")
                    .header("content-type", "application/json")
                    .queryString("token", token)
                    .body(pluginRemoveDto)
                    .asJson();

            if (response.getStatus() == HttpStatus.NOT_FOUND) {
                throw new UserNotFoundException();
            }


            return response.getStatus() + "";
        }

        public static List<PluginSearchResultDto> getAllPlugins(String token) throws Exception {
            HttpResponse<PluginSearchResultDto[]> response = Unirest.get(User.NetworkSettings.getAddress() + BASE_PATH + "/find/all")
                    .header("accept", "application/json")
                    .header("content-type", "application/json")
                    .queryString("token", token)
                    .asObject(PluginSearchResultDto[].class);

            if (response.getStatus() == HttpStatus.NOT_FOUND) {
                throw new UserNotFoundException();
            }

            if (response.getStatus() == HttpStatus.OK) {
                return Arrays.asList(response.getBody());
            }

            throw new Exception();
        }
    }


    public ApplicationManager getApplicationManager() {
        return applicationManager;
    }
}
