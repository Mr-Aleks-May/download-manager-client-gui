package com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.plugins.loader;

import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.ApplicationManager;
import com.mraleksmay.projects.download_manager.client.gui.platform.model.plugin.model.plugin.BasePlugin;
import com.mraleksmay.projects.download_manager.plugin.model.plugin.Plugin;
import com.mraleksmay.projects.download_manager.plugin.youtube.model.plugin.YoutubePlugin;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginsLoader {
    private ApplicationManager applicationManager;


    public PluginsLoader(ApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
    }


    public List<Plugin> loadPlugins(File pluginsDir) throws IOException, ClassNotFoundException {
        final List<Plugin> plugins = new ArrayList<>();
        plugins.add(new BasePlugin());
        //plugins.add(new YoutubePlugin());

        try {
            plugins.addAll(loadOther(pluginsDir));
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return plugins;
    }

    private List<Plugin> loadOther(File pluginsDir) throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final List<Plugin> plugins = new ArrayList<>();
        final ClassLoader defaultClassLoader = this.getClass().getClassLoader();

        final File[] entries = pluginsDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().matches("[\\w\\W]+\\.jar$");
            }
        });

        for (int i = 0; i < entries.length; i++) {
            final URL url = entries[i].toURI().toURL();
            final URLClassLoader pluginsLoader = new URLClassLoader("plugin" + i, new URL[]{url}, defaultClassLoader);

            try {
                final List<String> classesNames = getClassesNames(url);

                for (String className : classesNames) {
                    try {
                        pluginsLoader.loadClass(className);
                    } catch (Throwable exception) {
                    }
                }

                final Optional<String> pluginClassName = classesNames.stream().filter((name) -> name.endsWith("Plugin")).findAny();
                if (pluginClassName.isPresent()) {
                    final Class<? extends Plugin> clazz = (Class<? extends Plugin>) pluginsLoader.loadClass(pluginClassName.get());
                    final Plugin plugin = clazz.getConstructor().newInstance();

                    plugins.add(plugin);
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        return plugins;
    }

    private List<String> getClassesNames(URL path) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, IOException {
        final List<String> classes = new ArrayList<>();

        final JarFile jar = new JarFile(path.getPath());
        for (Enumeration<JarEntry> entries = jar.entries(); entries.hasMoreElements(); ) {
            final JarEntry entry = entries.nextElement();
            final String file = entry.getName();

            if (file.endsWith(".class")) {
                final String className = file.replace('/', '.').substring(0, file.length() - 6);

                if (className.startsWith("com.mraleksmay")) {
                    classes.add(className);
                }
            }
        }

        return classes;
    }

    public ApplicationManager getApplicationManager() {
        return applicationManager;
    }
}
