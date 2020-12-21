package com.mraleksmay.projects.download_manager.plugin.io.dto.plugin;

import com.google.gson.annotations.Expose;
import com.mraleksmay.projects.download_manager.plugin.io.dto.group.GroupDto;

import java.util.List;

public  class PluginDto {
    @Expose
    private String psid = "null";
    @Expose
    private String version;
    @Expose
    private List<GroupDto> groups;


    public PluginDto() {
    }


    public PluginDto(String psid, List<GroupDto> groups) {
        this.psid = psid;
        this.groups = groups;
    }

    public String getPSID() {
        return psid;
    }

    public PluginDto setPSID(String psid) {
        this.psid = psid;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public PluginDto setVersion(String version) {
        this.version = version;
        return this;
    }

    public List<GroupDto> getGroups() {
        return groups;
    }

    public PluginDto setGroups(List<GroupDto> groups) {
        this.groups = groups;
        return this;
    }


    @Override
    public String toString() {
        return "BasePluginDto{" +
                "psid='" + psid + '\'' +
                '}';
    }
}
