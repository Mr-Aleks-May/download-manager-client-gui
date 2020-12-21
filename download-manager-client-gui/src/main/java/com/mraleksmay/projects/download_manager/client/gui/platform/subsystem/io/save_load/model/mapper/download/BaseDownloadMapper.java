package com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.io.save_load.model.mapper.download;

import com.mraleksmay.projects.download_manager.client.gui.platform.model.plugin.model.download.BaseDownload;
import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.io.save_load.model.mapper.category.BaseCategoryMapper;
import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.io.save_load.model.dto.download.BaseDownloadDto;
import com.mraleksmay.projects.download_manager.plugin.model.category.Category;
import com.mraleksmay.projects.download_manager.plugin.model.download.AuthenticationData;
import com.mraleksmay.projects.download_manager.plugin.model.download.Download;
import com.mraleksmay.projects.download_manager.plugin.io.dto.category.CategoryDto;
import com.mraleksmay.projects.download_manager.plugin.io.dto.download.AuthenticationDataDto;
import com.mraleksmay.projects.download_manager.plugin.io.dto.download.DownloadDto;
import com.mraleksmay.projects.download_manager.plugin.io.mapper.download.DownloadMapper;

import java.io.IOException;

public class BaseDownloadMapper implements DownloadMapper {
    @Override
    public DownloadDto toDto(Download download) {
        final CategoryDto categoryDto = new BaseCategoryMapper().toDto(download.getCategory());
        final AuthenticationDataDto authDataDto = new BaseAuthenticationDataMapper().toDto(download.getAuthData());

        try {
            return new BaseDownloadDto()
                    .setDSID(download.getDSID())
                    .setFileName(download.getFileName())
                    .setUrl(download.getUrl())
                    .setOutputDir(download.getOutputDir())
                    .setTempDir(download.getTempDir())
                    .setExtension(download.getExtension())
                    .setPluginPSID(download.getCategory().getPlugin().getPSID())
                    .setCategoryCSID(categoryDto)
                    .setAuthData(authDataDto)
                    .setFullSize(download.getFullSize())
                    .setCurrentSize(download.getCurrentSize())
                    .setCreationTime(download.getCreationTime());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Download fromDto(DownloadDto downloadDto, Category category) {
        final AuthenticationData authData = new BaseAuthenticationDataMapper().fromDto(downloadDto.getAuthData());

        try {
            return new BaseDownload()
                    .setDSID(downloadDto.getDSID())
                    .setFileName(downloadDto.getFileName())
                    .setUrl(downloadDto.getUrl())
                    .setOutputDir(downloadDto.getOutputDir())
                    .setTempDir(downloadDto.getTempDir())
                    .setExtension(downloadDto.getExtension())
                    .setCategory(category)
                    .setAuthData(authData)
                    .setFullSize(downloadDto.getFullSize())
                    .setCurrentSize(downloadDto.getCurrentSize())
                    .setCreationTime(downloadDto.getCreationTime());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
