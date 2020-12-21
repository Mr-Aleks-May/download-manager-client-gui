package com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.communication.service.user.mapper;


import com.mraleksmay.projects.download_manager.client.gui.platform.model.plugin.model.download.BaseDownload;
import com.mraleksmay.projects.download_manager.plugin.io.dto.download.SDownloadDto;
import com.mraleksmay.projects.download_manager.plugin.io.mapper.download.SDownloadMapper;
import com.mraleksmay.projects.download_manager.plugin.model.category.Category;
import com.mraleksmay.projects.download_manager.plugin.model.download.Download;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class BaseSDownloadMapper implements SDownloadMapper {
    private final BaseSCategoryMapper categoryMapper;


    public BaseSDownloadMapper(BaseSCategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }


    @Override
    public SDownloadDto toDto(Download download) {
        try {
            return new SDownloadDto()
                    .setDSID(download.getDSID())
                    .setFileName(download.getFileName())
                    .setExtension(download.getExtension())
                    .setTmpDir(download.getTempDir().getCanonicalPath())
                    .setOutputDir(download.getOutputDir().getCanonicalPath())
                    .setUrl(download.getUrl().toString())
                    .setCategoryDto(categoryMapper.toDto(download.getCategory()))
                    .setCreationTime(download.getCreationTime());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Download fromDto(SDownloadDto downloadDto, Category category) {
        try {
            return new BaseDownload()
                    .setDSID(downloadDto.getDSID())
                    .setFileName(downloadDto.getFileName())
                    .setExtension(downloadDto.getExtension())
                    .setTempDir(new File(downloadDto.getTmpDir()))
                    .setOutputDir(new File(downloadDto.getOutputDir()))
                    .setUrl(new URL(downloadDto.getUrl()))
                    .setCategory(category)
                    .setCreationTime(downloadDto.getCreationTime());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
