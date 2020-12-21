package com.mraleksmay.projects.download_manager.plugin.io.mapper.category;

import com.mraleksmay.projects.download_manager.plugin.io.dto.category.SCategoryDto;
import com.mraleksmay.projects.download_manager.plugin.model.category.Category;
import com.mraleksmay.projects.download_manager.plugin.model.plugin.Plugin;

public interface SCategoryMapper {
    public abstract SCategoryDto toDto(final Category category);

    public abstract Category fromDto(SCategoryDto categoryDto, Plugin plugin);
}
