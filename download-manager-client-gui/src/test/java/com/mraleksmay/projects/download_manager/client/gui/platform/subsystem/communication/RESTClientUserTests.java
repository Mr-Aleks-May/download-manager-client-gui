package com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.communication;

import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.ApplicationManager;
import com.mraleksmay.projects.download_manager.plugin.io.dto.category.SCategoryDto;
import com.mraleksmay.projects.download_manager.plugin.io.dto.download.SDownloadDto;
import com.mraleksmay.projects.download_manager.common.exception.UserAlreadyExistsException;
import com.mraleksmay.projects.download_manager.common.exception.UserNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RESTClientUserTests {
    public static ApplicationManager applicationManager = new ApplicationManager(null);

    /**
     * Register new user test.
     *
     * @throws UserNotFoundException
     * @throws UserAlreadyExistsException
     */
    @Test
    public void signUpTest() throws UserNotFoundException, UserAlreadyExistsException {
        String login = "test" + new Random().nextInt();
        String password = "test";

        RESTClient client = new RESTClient(applicationManager);
        String token = RESTClient.UserService.signUp(login, password);

        Assertions.assertNotEquals(null, token);
        Assertions.assertNotEquals("", token);
        Assertions.assertTrue(token.length() < 100);
        Assertions.assertTrue(token.length() > 10);
    }


    /**
     * Login test.
     *
     * @throws UserNotFoundException
     * @throws UserAlreadyExistsException
     */
    @Test
    public void signInTest() throws UserNotFoundException, UserAlreadyExistsException {
        String login = "test" + new Random().nextInt();
        String password = "test";

        String token = RESTClient.UserService.signUp(login, password);
        token = RESTClient.UserService.signIn(login, password);

        Assertions.assertNotEquals(null, token);
        Assertions.assertNotEquals("", token);
        Assertions.assertTrue(token.length() < 100);
        Assertions.assertTrue(token.length() > 10);
    }

    /**
     * Remove user account.
     *
     * @throws UserNotFoundException
     * @throws UserAlreadyExistsException
     */
    @Test
    public void removeUserAccountTest() throws UserNotFoundException, UserAlreadyExistsException {
        String login = "test" + new Random().nextInt();
        String password = "test";

        RESTClient.UserService.signUp(login, password);
        String token = RESTClient.UserService.signIn(login, password);
        String response = RESTClient.UserService.removeUserAccount(token);

        Assertions.assertNotEquals(null, token);
        Assertions.assertNotEquals("", token);
        Assertions.assertTrue(token.length() < 100);
        Assertions.assertTrue(token.length() > 10);
    }

    @Test
    public void addDownloadTest() throws UserNotFoundException, UserAlreadyExistsException {
        String login = "test" + new Random().nextInt();
        String password = "test";

        RESTClient.UserService.signUp(login, password);
        String token = RESTClient.UserService.signIn(login, password);
        RESTClient.UserService.addDownloadToUser(token, new SDownloadDto()
                .setCategoryDto(new SCategoryDto()
                        .setPluginPSID("test"))
                .setFileName("test"));

        Assertions.assertNotEquals(null, token);
        Assertions.assertNotEquals("", token);
        Assertions.assertTrue(token.length() < 100);
        Assertions.assertTrue(token.length() > 10);
    }

    @Test
    public void addDownloadListTest() throws UserNotFoundException, UserAlreadyExistsException {
        String login = "test" + new Random().nextInt();
        String password = "test";

        RESTClient.UserService.signUp(login, password);
        String token = RESTClient.UserService.signIn(login, password);


        List<SDownloadDto> downloadsDto = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            SDownloadDto downloadDto = new SDownloadDto()
                    .setDSID("TEST" + new Random().nextInt())
                    .setCategoryDto(new SCategoryDto()
                            .setPluginPSID("test" + new Random().nextInt()))
                    .setFileName("test");

            downloadsDto.add(downloadDto);
        }

        RESTClient.UserService.addDownloadListToUser(token, downloadsDto);

        Assertions.assertNotEquals(null, token);
        Assertions.assertNotEquals("", token);
        Assertions.assertTrue(token.length() < 100);
        Assertions.assertTrue(token.length() > 10);
    }

    @Test
    public void getDownloadTest() throws Exception {
        String login = "test" + new Random().nextInt();
        String password = "test";

        RESTClient.UserService.signUp(login, password);
        String token = RESTClient.UserService.signIn(login, password);

        SDownloadDto downloadDto = new SDownloadDto()
                .setDSID("TEST" + new Random().nextInt())
                .setCategoryDto(new SCategoryDto()
                        .setPluginPSID("test" + new Random().nextInt()))
                .setFileName("test");

        RESTClient.UserService.addDownloadListToUser(token, Arrays.asList(downloadDto));
        SDownloadDto download = RESTClient.UserService.getDownloadFromList(token, downloadDto.getDSID());

        Assertions.assertNotEquals(null, download);
        Assertions.assertNotEquals("", download.getDSID());
        Assertions.assertTrue(downloadDto.getDSID().equals(download.getDSID()));
        Assertions.assertTrue(token.length() > 10);
    }

    @Test
    public void getDownloadListTest() throws Exception {
        String login = "test" + new Random().nextInt();
        String password = "test";

        RESTClient.UserService.signUp(login, password);
        String token = RESTClient.UserService.signIn(login, password);

        SDownloadDto downloadDto = new SDownloadDto()
                .setDSID("TEST" + new Random().nextInt())
                .setCategoryDto(new SCategoryDto()
                        .setPluginPSID("test" + new Random().nextInt()))
                .setFileName("test");

        RESTClient.UserService.addDownloadListToUser(token, Arrays.asList(downloadDto));
        List<SDownloadDto> download = RESTClient.UserService.getAllDownloadsFromUser(token);

        Assertions.assertNotEquals(null, download);
        Assertions.assertNotEquals(false, download.size() >= 0);
        Assertions.assertTrue(download.size() >= 0);
        Assertions.assertTrue(token.length() > 10);
    }


    @Test
    public void removeDownloadTest() throws Exception {
        String login = "test" + new Random().nextInt();
        String password = "test";

        RESTClient.UserService.signUp(login, password);
        String token = RESTClient.UserService.signIn(login, password);

        SDownloadDto downloadDto = new SDownloadDto()
                .setDSID("TEST" + new Random().nextInt())
                .setCategoryDto(new SCategoryDto()
                        .setPluginPSID("test" + new Random().nextInt()))
                .setFileName("test");

        RESTClient.UserService.addDownloadToUser(token, downloadDto);
        RESTClient.UserService.removeDownloadFromList(token, downloadDto.getDSID());

        Assertions.assertTrue(token.length() > 10);
    }

    @Test
    public void removeDownloadListTest() throws Exception {
        String login = "test" + new Random().nextInt();
        String password = "test";

        RESTClient.UserService.signUp(login, password);
        String token = RESTClient.UserService.signIn(login, password);

        SDownloadDto downloadDto = new SDownloadDto()
                .setDSID("TEST" + new Random().nextInt())
                .setCategoryDto(new SCategoryDto()
                        .setPluginPSID("test" + new Random().nextInt()))
                .setFileName("test");

        RESTClient.UserService.addDownloadListToUser(token, Arrays.asList(downloadDto));
        RESTClient.UserService.removeDownloadList(token, Arrays.asList(downloadDto));

        Assertions.assertTrue(token.length() > 10);
    }
}
