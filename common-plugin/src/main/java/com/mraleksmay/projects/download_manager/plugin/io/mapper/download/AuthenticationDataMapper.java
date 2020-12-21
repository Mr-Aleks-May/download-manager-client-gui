package com.mraleksmay.projects.download_manager.plugin.io.mapper.download;

import com.mraleksmay.projects.download_manager.plugin.model.download.AuthenticationData;
import com.mraleksmay.projects.download_manager.plugin.io.dto.download.AuthenticationDataDto;

public interface AuthenticationDataMapper {

    public AuthenticationDataDto toDto(AuthenticationData authData);

    public AuthenticationData fromDto(AuthenticationDataDto authDataDto);
}
