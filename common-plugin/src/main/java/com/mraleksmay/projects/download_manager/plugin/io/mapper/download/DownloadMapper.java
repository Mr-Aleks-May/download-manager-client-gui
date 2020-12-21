package com.mraleksmay.projects.download_manager.plugin.io.mapper.download;

import com.mraleksmay.projects.download_manager.plugin.model.category.Category;
import com.mraleksmay.projects.download_manager.plugin.model.download.Download;
import com.mraleksmay.projects.download_manager.plugin.io.dto.download.DownloadDto;

public interface DownloadMapper {

    public DownloadDto toDto(final Download download);

    public Download fromDto(final DownloadDto downloadDto, Category category);
}
