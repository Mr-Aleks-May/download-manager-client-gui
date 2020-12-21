package com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.communication.service.marketplace.dto;

import java.util.Optional;
import java.util.Set;

public class PluginSearchResultDto {

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
     * Load user.
     */
    private String author;
    /**
     * Check sum (hash MD5).
     */
    private Long checkSum = 0L;
    /**
     * Plugin description.
     */
    private String description;
    /**
     * Tags.
     */
    private Set<String> tags;


    public PluginSearchResultDto() {
    }


    public String getPluginPSID() {
        return pluginPSID;
    }

    public PluginSearchResultDto setPluginPSID(String pluginPSID) {
        this.pluginPSID = pluginPSID;
        return this;
    }

    public String getName() {
        return name;
    }

    public PluginSearchResultDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public PluginSearchResultDto setVersion(String version) {
        this.version = version;
        return this;
    }

    public Optional<String> getAuthor() {
        return Optional.of(author);
    }

    public PluginSearchResultDto setAuthor(String author) {
        this.author = author;
        return this;
    }

    public Long getCheckSum() {
        return checkSum;
    }

    public PluginSearchResultDto setCheckSum(Long checkSum) {
        this.checkSum = checkSum;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public PluginSearchResultDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public Set<String> getTags() {
        return tags;
    }

    public PluginSearchResultDto setTags(Set<String> tags) {
        this.tags = tags;
        return this;
    }

    @Override
    public String toString() {
        return "PluginSearchResultDto{" +
                "pluginPSID='" + pluginPSID + '\'' +
                ", name='" + name + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
