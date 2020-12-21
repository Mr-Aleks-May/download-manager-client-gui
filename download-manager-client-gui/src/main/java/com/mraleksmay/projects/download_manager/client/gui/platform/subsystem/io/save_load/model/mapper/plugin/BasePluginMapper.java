package com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.io.save_load.model.mapper.plugin;

import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.io.save_load.model.mapper.group.BaseGroupMapper;
import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.io.save_load.model.dto.plugin.BasePluginDto;
import com.mraleksmay.projects.download_manager.plugin.model.plugin.Plugin;
import com.mraleksmay.projects.download_manager.plugin.io.dto.group.GroupDto;
import com.mraleksmay.projects.download_manager.plugin.io.dto.plugin.PluginDto;
import com.mraleksmay.projects.download_manager.plugin.io.mapper.plugin.PluginMapper;

import java.util.List;
import java.util.stream.Collectors;

public class BasePluginMapper implements PluginMapper {
    @Override
    public PluginDto toDto(Plugin plugin) {
        final BaseGroupMapper baseGroupMapper = new BaseGroupMapper();
        final List<GroupDto> groupsDto = plugin.getGroups().stream()
                .map(g -> baseGroupMapper.toDto(g))
                .collect(Collectors.toUnmodifiableList());

        return new BasePluginDto()
                .setPSID(plugin.getPSID())
                .setVersion(plugin.getVersion())
                .setGroups(groupsDto);
    }

    @Override
    public Plugin fromDto(PluginDto pluginDto) {
        return null;
    }
}
