package com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.communication.service.user.mapper;

import com.mraleksmay.projects.download_manager.client.gui.platform.model.plugin.model.category.BaseCategory;
import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.communication.service.user.dto.BaseSCategoryDto;
import com.mraleksmay.projects.download_manager.plugin.io.dto.category.SCategoryDto;
import com.mraleksmay.projects.download_manager.plugin.io.mapper.category.SCategoryMapper;
import com.mraleksmay.projects.download_manager.plugin.model.category.Category;
import com.mraleksmay.projects.download_manager.plugin.model.plugin.Plugin;

public class BaseSCategoryMapper implements SCategoryMapper {
    @Override
    public SCategoryDto toDto(Category category) {
        return new BaseSCategoryDto()
                .setCategoryCSID(category.getCSID())
                .setName(category.getName())
                .setPluginPSID(category.getPlugin().getPSID());
    }

    @Override
    public Category fromDto(SCategoryDto categoryDto, Plugin plugin) {
        return new BaseCategory()
                .setCSID(categoryDto.getCategoryCSID())
                .setName(categoryDto.getName())
                .setPlugin(plugin);
    }
}
