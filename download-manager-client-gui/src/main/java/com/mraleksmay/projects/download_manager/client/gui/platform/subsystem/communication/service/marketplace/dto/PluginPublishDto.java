package com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.communication.service.marketplace.dto;

import java.util.HashSet;
import java.util.Set;

public class PluginPublishDto {

    /**
     * Plugin unique identifier.
     */
    private String pluginPSID;
    /**
     * Plugin name.
     */
    private String name;
    /**
     * Plugin version.
     */
    private String version;
    /**
     * Plugin description.
     */
    private String description;
    /**
     * Plugin source.
     */
    private Byte[] content;
    /**
     * Plugin tags.
     */
    private Set<String> tags;


    public PluginPublishDto() {
        tags = new HashSet<>();
    }


    public String getPluginPSID() {
        return pluginPSID;
    }

    public PluginPublishDto setPluginPSID(String pluginPSID) {
        this.pluginPSID = pluginPSID;
        return this;
    }

    public String getName() {
        return name;
    }

    public PluginPublishDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public PluginPublishDto setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public PluginPublishDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public Byte[] getContent() {
        return content;
    }

    public PluginPublishDto setContent(Byte[] content) {
        this.content = content;
        return this;
    }

    public Set<String> getTags() {
        return tags;
    }

    public PluginPublishDto setTags(Set<String> tags) {
        this.tags = tags;
        return this;
    }

    @Override
    public String toString() {
        return "{" + "pluginPSID='" + pluginPSID + ", name='" + name + '}';
    }
}
