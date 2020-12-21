package com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.io.save_load.model.mapper.category;

import com.mraleksmay.projects.download_manager.client.gui.platform.model.plugin.model.category.BaseCategory;
import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.io.save_load.model.dto.category.BaseCategoryDto;
import com.mraleksmay.projects.download_manager.plugin.model.category.Category;
import com.mraleksmay.projects.download_manager.plugin.io.dto.category.CategoryDto;
import com.mraleksmay.projects.download_manager.plugin.io.mapper.category.CategoryMapper;

public class BaseCategoryMapper implements CategoryMapper {

    @Override
    public CategoryDto toDto(Category category) {
        return new BaseCategoryDto()
                .setCSID(category.getCSID())
                .setId(category.getId())
                .setName(category.getName())
                .setContentType(category.getContentType())
                .setOutputDir(category.getOutputDir())
                .setContentType(category.getComments());
    }

    @Override
    public Category fromDto(CategoryDto categoryDto) {
        return new BaseCategory()
                .setCSID(categoryDto.getCSID())
                .setId(categoryDto.getId())
                .setName(categoryDto.getName())
                .setContentType(categoryDto.getContentType())
                .setOutputDir(categoryDto.getOutputDir())
                .setContentType(categoryDto.getComments());
    }
}
