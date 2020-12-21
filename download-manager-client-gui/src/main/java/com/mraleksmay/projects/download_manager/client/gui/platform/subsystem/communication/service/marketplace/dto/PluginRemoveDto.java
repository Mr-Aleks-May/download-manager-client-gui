package com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.communication.service.marketplace.dto;

public class PluginRemoveDto {

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


    public PluginRemoveDto() {
    }


    public String getPluginPSID() {
        return pluginPSID;
    }

    public PluginRemoveDto setPluginPSID(String pluginPSID) {
        this.pluginPSID = pluginPSID;
        return this;
    }

    public String getName() {
        return name;
    }

    public PluginRemoveDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public PluginRemoveDto setVersion(String version) {
        this.version = version;
        return this;
    }


    @Override
    public String toString() {
        return "{" + pluginPSID + "}";
    }
}
