package com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.io.save_load.model.mapper.group;

import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.io.save_load.model.mapper.category.BaseCategoryMapper;
import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.io.save_load.model.dto.group.BaseGroupDto;
import com.mraleksmay.projects.download_manager.plugin.model.group.Group;
import com.mraleksmay.projects.download_manager.plugin.io.dto.category.CategoryDto;
import com.mraleksmay.projects.download_manager.plugin.io.dto.group.GroupDto;
import com.mraleksmay.projects.download_manager.plugin.io.mapper.group.GroupMapper;

import java.util.List;
import java.util.stream.Collectors;

public class BaseGroupMapper implements GroupMapper {
    @Override
    public GroupDto toDto(Group group) {
        final BaseCategoryMapper baseCategoryMapper = new BaseCategoryMapper();
        final List<CategoryDto> categoriesList = group.getCategories().stream()
                .map(c -> baseCategoryMapper.toDto(c))
                .collect(Collectors.toUnmodifiableList());

        return new BaseGroupDto()
                .setGSID(group.getGSID())
                .setCategories(categoriesList);
    }

    @Override
    public Group fromDto(GroupDto groupDto) {
        return null;
    }
}
