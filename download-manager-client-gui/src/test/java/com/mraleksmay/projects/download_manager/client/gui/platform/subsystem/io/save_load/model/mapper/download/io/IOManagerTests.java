package com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.io.save_load.model.mapper.download.io;

import com.mraleksmay.projects.download_manager.client.gui.platform.model.plugin.model.plugin.BasePlugin;
import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.ApplicationManager;
import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.io.IOManagerCurrent;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class IOManagerTests {

    @Test
    public void serializationTest() throws Exception {
        ApplicationManager applicationManager = getApplicationManager();

        IOManagerCurrent ioManager = new IOManagerCurrent(applicationManager);
        ioManager.save(new File("./test.out"));
    }

    @Test
    public void deserializationTest() throws Exception {
//        List<PluginContext> plugins = new ArrayList<>();
//        for (int i = 0; i < 2; i++) {
//            PluginContext plugin = new BasePluginContext();
//            plugin.init();
//            plugins.add(plugin);
//        }
//
//        IOManagerN2.restore(plugins);
    }


    private ApplicationManager getApplicationManager() {
        try {
            ApplicationManager applicationManager = new ApplicationManager(null);
            BasePlugin plugin = new BasePlugin();
            plugin.init();
            applicationManager.getPluginsManager().add(plugin);

            return applicationManager;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
