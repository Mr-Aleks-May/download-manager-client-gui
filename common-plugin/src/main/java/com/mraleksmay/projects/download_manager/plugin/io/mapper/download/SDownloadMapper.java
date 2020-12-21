package com.mraleksmay.projects.download_manager.plugin.io.mapper.download;

import com.mraleksmay.projects.download_manager.plugin.io.dto.download.SDownloadDto;
import com.mraleksmay.projects.download_manager.plugin.model.category.Category;
import com.mraleksmay.projects.download_manager.plugin.model.download.Download;

public interface SDownloadMapper {
    public SDownloadDto toDto(final Download download);

    public Download fromDto(final SDownloadDto downloadDto, Category category);
}
