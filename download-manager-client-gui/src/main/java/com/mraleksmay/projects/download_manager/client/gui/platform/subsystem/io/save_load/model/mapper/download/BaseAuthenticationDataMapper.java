package com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.io.save_load.model.mapper.download;

import com.mraleksmay.projects.download_manager.client.gui.platform.model.plugin.model.download.Base64AuthenticationData;
import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.io.save_load.model.dto.download.BaseAuthenticationDataDto;
import com.mraleksmay.projects.download_manager.plugin.model.download.AuthenticationData;
import com.mraleksmay.projects.download_manager.plugin.io.dto.download.AuthenticationDataDto;
import com.mraleksmay.projects.download_manager.plugin.io.mapper.download.AuthenticationDataMapper;

public class BaseAuthenticationDataMapper implements AuthenticationDataMapper {
    @Override
    public AuthenticationDataDto toDto(AuthenticationData authData) {
        if (authData == null)
            return null;

        return new BaseAuthenticationDataDto()
                .setLogin(authData.getLogin())
                .setPassword(authData.getPassword());
    }

    @Override
    public AuthenticationData fromDto(AuthenticationDataDto authDataDto) {
        if (authDataDto == null)
            return null;

        return new Base64AuthenticationData()
                .setLogin(authDataDto.getLogin())
                .setPassword(authDataDto.getPassword());
    }
}
