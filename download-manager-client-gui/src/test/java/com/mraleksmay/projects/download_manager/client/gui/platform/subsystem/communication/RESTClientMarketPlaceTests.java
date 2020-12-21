package com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.communication;

import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.ApplicationManager;
import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.communication.service.marketplace.dto.PluginPublishDto;
import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.communication.service.marketplace.dto.PluginRemoveDto;
import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.communication.service.marketplace.dto.PluginSearchResultDto;
import com.mraleksmay.projects.download_manager.common.exception.UserAlreadyExistsException;
import com.mraleksmay.projects.download_manager.common.exception.UserNotFoundException;
import com.mraleksmay.projects.download_manager.common.util.file.FileWorker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class RESTClientMarketPlaceTests {
    /**
     * Publish plugin on server.
     *
     * @throws UserNotFoundException
     * @throws UserAlreadyExistsException
     * @throws IOException
     */
    @Test
    public void publishPluginTest() throws UserNotFoundException, UserAlreadyExistsException, IOException {
        String login = "test" + new Random().nextInt();
        String password = "test";

        File pluginDir = new File("./plugins/plugin");
        Byte[] pluginContent = FileWorker.readFileAsBytes(pluginDir);
        PluginPublishDto pluginPublishDto = new PluginPublishDto()
                .setPluginPSID("Test" + new Random().nextInt())
                .setName("Youtube Plugin")
                .setVersion("0.1")
                .setDescription("Download video from youtube for free!")
                .setTags(new HashSet<>(Arrays.asList("youtube", "download")))
                .setContent(pluginContent);

        String token = RESTClient.UserService.signUp(login, password);
        RESTClient.MarketplaceService.publishPlugin(token, pluginPublishDto);

        Assertions.assertNotEquals(null, token);
        Assertions.assertNotEquals("", token);
        Assertions.assertTrue(token.length() < 100);
        Assertions.assertTrue(token.length() > 10);
    }


    @Test
    public void removePluginTest() throws UserNotFoundException, UserAlreadyExistsException, IOException {
        String login = "test" + new Random().nextInt();
        String password = "test";

        File pluginDir = new File("./plugins/plugin");
        Byte[] pluginContent = FileWorker.readFileAsBytes(pluginDir);
        PluginPublishDto pluginPublishDto = new PluginPublishDto()
                .setPluginPSID("Test" + new Random().nextInt())
                .setName("test")
                .setVersion("0.1")
                .setDescription("test")
                .setTags(new HashSet<>(Arrays.asList("test")))
                .setContent(pluginContent);
        PluginRemoveDto pluginRemoveDto = new PluginRemoveDto()
                .setPluginPSID(pluginPublishDto.getPluginPSID())
                .setName(pluginPublishDto.getName())
                .setVersion(pluginPublishDto.getVersion());

        String token = RESTClient.UserService.signUp(login, password);
        RESTClient.MarketplaceService.publishPlugin(token, pluginPublishDto);
        RESTClient.MarketplaceService.removePlugin(token, pluginRemoveDto);


        Assertions.assertNotEquals(null, token);
        Assertions.assertNotEquals("", token);
        Assertions.assertTrue(token.length() < 100);
        Assertions.assertTrue(token.length() > 10);
    }


    @Test
    public void getAllPluginsTest() throws Exception {
        String login = "test" + new Random().nextInt();
        String password = "test";

        File pluginDir = new File("./plugins/plugin");
        Byte[] pluginContent = FileWorker.readFileAsBytes(pluginDir);
        PluginPublishDto pluginPublishDto = new PluginPublishDto()
                .setPluginPSID("Test" + new Random().nextInt())
                .setName("test")
                .setVersion("0.1")
                .setDescription("test")
                .setTags(new HashSet<>(Arrays.asList("test")))
                .setContent(pluginContent);

        String token = RESTClient.UserService.signUp(login, password);
        RESTClient.MarketplaceService.publishPlugin(token, pluginPublishDto);

        List<PluginSearchResultDto> allPlugins = RESTClient.MarketplaceService.getAllPlugins(token);


        Assertions.assertNotEquals(null, token);
        Assertions.assertNotEquals("", token);
        Assertions.assertTrue(token.length() < 100);
        Assertions.assertTrue(token.length() > 10);
    }


}
