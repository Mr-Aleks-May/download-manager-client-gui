package com.mraleksmay.projects.download_manager.plugin.io.mapper.category;

import com.mraleksmay.projects.download_manager.plugin.model.category.Category;
import com.mraleksmay.projects.download_manager.plugin.io.dto.category.CategoryDto;

public interface CategoryMapper {
    public abstract CategoryDto toDto(final Category category);

    public abstract Category fromDto(CategoryDto categoryDto);
}
