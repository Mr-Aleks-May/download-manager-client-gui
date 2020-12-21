package com.mraleksmay.projects.download_manager.plugin.io.mapper.plugin;

import com.mraleksmay.projects.download_manager.plugin.model.plugin.Plugin;
import com.mraleksmay.projects.download_manager.plugin.io.dto.plugin.PluginDto;

public interface PluginMapper {
    public abstract PluginDto toDto(final Plugin plugin);

    public abstract Plugin fromDto(PluginDto pluginDto);
}
