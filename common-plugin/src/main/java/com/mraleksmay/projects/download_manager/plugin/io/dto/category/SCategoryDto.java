package com.mraleksmay.projects.download_manager.plugin.io.dto.category;


import com.mraleksmay.projects.download_manager.common.annotation.NotNull;

public class SCategoryDto {
    /**
     * Category unique identifier.
     */
    @NotNull
    private String categoryCSID;
    /**
     * Category name.
     */
    @NotNull
    private String name;
    /**
     * Category is part of plugin with plugin_csid identifier.
     */
    @NotNull
    private String pluginPSID;


    public SCategoryDto() {
    }


    public String getCategoryCSID() {
        return categoryCSID;
    }

    public SCategoryDto setCategoryCSID(String categoryId) {
        this.categoryCSID = categoryId;
        return this;
    }

    public String getName() {
        return name;
    }

    public SCategoryDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getPluginPSID() {
        return pluginPSID;
    }

    public SCategoryDto setPluginPSID(String pluginPSID) {
        this.pluginPSID = pluginPSID;
        return this;
    }


    @Override
    public String toString() {
        return super.toString();
    }
}
