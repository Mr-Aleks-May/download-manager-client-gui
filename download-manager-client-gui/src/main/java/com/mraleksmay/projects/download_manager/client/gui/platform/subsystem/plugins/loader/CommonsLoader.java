package com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.plugins.loader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class CommonsLoader {

    public static void loadClasses(File commonsDir) throws IOException, IllegalAccessException {
        final ClassLoader defaultClassLoader = CommonsLoader.class.getClassLoader();
        final File[] files = commonsDir.listFiles();

        for (int i = 0; i < files.length; i++) {
            try {
                final List<String> classesNames = getClassesNames(files[i]);

                for (String className : classesNames) {
                    try {
                        defaultClassLoader.loadClass(className);
                    } catch (Throwable exception) {
                    }
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    private static List<String> getClassesNames(File fullPath) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, IOException {
        final List<String> classes = new ArrayList<>();

        final JarFile jar = new JarFile(fullPath.getCanonicalPath());
        for (Enumeration<JarEntry> entries = jar.entries(); entries.hasMoreElements(); ) {
            final JarEntry entry = entries.nextElement();
            final String file = entry.getName();

            if (file.endsWith(".class")) {
                final String className = file.replace('/', '.').substring(0, file.length() - 6);

                if (className.startsWith("com.mraleksmay.projects.download_manager")) {
                    classes.add(className);
                }
            }
        }

        return classes;
    }
}
