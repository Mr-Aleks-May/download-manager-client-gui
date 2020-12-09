package com.mraleksmay.projects.download_manager.client.gui.core.loader;

public class ApplicationClassLoader extends ClassLoader {
    public ApplicationClassLoader() {
    }

    public ApplicationClassLoader(ClassLoader parent) {
        super(parent);
    }

    public ApplicationClassLoader(String name, ClassLoader parent) {
        super(name, parent);
    }
}
