package com.mraleksmay.projects.download_manager.plugin.model;

import java.io.IOException;

/**
 * This interface indicates that the class can be serialized and deserialized.
 */
public interface Restorable {
    /**
     * Serializes an object.
     *
     * @return json
     */
    String save();

    /**
     * Restores the object to its original state.
     *
     * @param json
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws IOException
     */
    void restore(String json) throws ClassNotFoundException, Exception;
}
