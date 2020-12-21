package com.mraleksmay.projects.download_manager.plugin.io.dto.category;

import com.google.gson.annotations.Expose;

import java.io.File;
import java.io.IOException;

public class CategoryDto {
    /**
     * Unique category identifier used during (dese|se)rialization.
     */
    @Expose
    private String csid = "null";
    @Expose
    private String id;
    @Expose
    private String name;
    @Expose
    private String contentType;
    @Expose
    private String extensions;
    @Expose
    private String outputDir;
    @Expose
    private String comments;


    public CategoryDto() {
    }

    public CategoryDto(String csid) {
        this.csid = csid;
    }


    public String getCSID() {
        return csid;
    }

    public CategoryDto setCSID(String csid) {
        this.csid = csid;
        return this;
    }

    public String getId() {
        return id;
    }

    public CategoryDto setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public CategoryDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getContentType() {
        return contentType;
    }

    public CategoryDto setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public String getExtensions() {
        return extensions;
    }

    public CategoryDto setExtensions(String extensions) {
        this.extensions = extensions;
        return this;
    }

    public File getOutputDir() {
        try {
            return new File(outputDir).getCanonicalFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CategoryDto setOutputDir(File outputDir) {
        try {
            this.outputDir = outputDir.getCanonicalPath();
            return this;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getComments() {
        return comments;
    }

    public CategoryDto setComments(String comments) {
        this.comments = comments;
        return this;
    }


    @Override
    public String toString() {
        return "BaseCategoryDto{" +
                "csid='" + csid + '\'' +
                '}';
    }
}
