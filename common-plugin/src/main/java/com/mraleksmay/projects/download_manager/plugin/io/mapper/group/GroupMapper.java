package com.mraleksmay.projects.download_manager.plugin.io.mapper.group;

import com.mraleksmay.projects.download_manager.plugin.model.group.Group;
import com.mraleksmay.projects.download_manager.plugin.io.dto.group.GroupDto;

public interface GroupMapper {
    public abstract GroupDto toDto(final Group group);

    public abstract Group fromDto(GroupDto groupDto);
}
