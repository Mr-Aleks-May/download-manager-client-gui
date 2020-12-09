package com.mraleksmay.projects.download_manager.common.model;

import java.io.IOException;

/**
 * This interface indicates that the class can be serialized and deserialized.
 */
public interface Restoreable {
    /**
     * Serializes an object.
     * @return json
     */
    String serialize();

    /**
     * Restores the object to its original state.
     * @param obj
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws IOException
     */
    void deserialize(Object obj) throws ClassNotFoundException, IllegalAccessException, InstantiationException, IOException;
}
