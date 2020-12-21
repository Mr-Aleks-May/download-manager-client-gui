package com.mraleksmay.projects.download_manager.plugin.io.dto.group;

import com.google.gson.annotations.Expose;
import com.mraleksmay.projects.download_manager.plugin.io.dto.category.CategoryDto;

import java.util.List;

public  class GroupDto {
    /**
     * Unique group identifier used during serialization.
     */
    @Expose
    private String gsid = "null";
    /**
     * Contains categories.
     */
    @Expose
    private List<CategoryDto> categories;


    public GroupDto() {
    }

    public GroupDto(String gsid, List<CategoryDto> categories) {
        this.gsid = gsid;
        this.categories = categories;
    }


    public String getGSID() {
        return gsid;
    }

    public GroupDto setGSID(String gsid) {
        this.gsid = gsid;
        return this;
    }

    public List<CategoryDto> getCategories() {
        return categories;
    }

    public GroupDto setCategories(List<CategoryDto> categories) {
        this.categories = categories;
        return this;
    }


    @Override
    public String toString() {
        return "BaseGroupDto{" +
                "gsid='" + gsid +
                '}';
    }
}
