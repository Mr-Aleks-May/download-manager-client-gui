package com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.communication;

import com.mraleksmay.projects.download_manager.client.gui.platform.model.download.DownloadEntityInformation;
import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.ApplicationManager;
import com.mraleksmay.projects.download_manager.plugin.io.dto.download.SDownloadDto;
import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.communication.service.user.mapper.BaseSCategoryMapper;
import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.communication.service.user.mapper.BaseSDownloadMapper;
import com.mraleksmay.projects.download_manager.client.gui.platform.util.downloader.BasicNetWorker;
import com.mraleksmay.projects.download_manager.common.exception.UserNotFoundException;
import com.mraleksmay.projects.download_manager.plugin.model.category.Category;
import com.mraleksmay.projects.download_manager.plugin.model.download.Download;
import com.mraleksmay.projects.download_manager.plugin.model.plugin.Plugin;
import com.mraleksmay.projects.download_manager.plugin.model.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mraleksmay.projects.download_manager.client.gui.AppStart.getApplicationManager;
import static java.util.stream.Collectors.toUnmodifiableList;

public class IORESTManager {
    public static class UserService {
        private static ApplicationManager applicationManager;


        public static void sync(String token, ApplicationManager applicationManager) throws Exception {
            UserService.applicationManager = applicationManager;

            // Get downloads from server.
            List<SDownloadDto> downloadsDtoFromServer = getFromServer(token);

            // Get user downloads list and map to SDownload.
            List<SDownloadDto> downloadsDto = getApplicationManager().getDownloadsManager().getDownloads().stream()
                    .map((d) -> new BaseSDownloadMapper(new BaseSCategoryMapper()).toDto(d.getDownload()))
                    .collect(toUnmodifiableList());

            // Send downloads to server.
            try {
                sendToServer(token, downloadsDto);
            } catch (Exception e) {
            }


            // MERGER downloads.
            merger(downloadsDtoFromServer);
        }


        public static void sendToServer(String token, List<SDownloadDto> downloadsDto) throws UserNotFoundException {
            // Send SDownloads to server.
            RESTClient.UserService.addDownloadListToUser(User.getCurrentUser().getToken(), downloadsDto);
        }

        public static List<SDownloadDto> getFromServer(String token) throws Exception {
            // Get downloads from server.
            List<SDownloadDto> downloadsDto = RESTClient.UserService.getAllDownloadsFromUser(token);
            return downloadsDto;
        }

        public static void merger(List<SDownloadDto> downloadsDto) {
            List<Download> downloads = mapDownloads(downloadsDto);

            getApplicationManager().getDownloadsManager().addAll(downloads.stream()
                    .map((d) -> new DownloadEntityInformation(d, new BasicNetWorker()))
                    .collect(toUnmodifiableList())
            );
        }

        private static List<Download> mapDownloads(List<SDownloadDto> downloadsDto) {
            List<Download> downloads = new ArrayList<>();

            for (SDownloadDto downloadDto : downloadsDto) {
                try {
                    Download download = mapDownload(downloadDto);
                    downloads.add(download);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return downloads;
        }

        private static Download mapDownload(SDownloadDto downloadDto) throws Exception {
            Optional<Plugin> plugin = getApplicationManager().getPluginsManager().getPlugins().stream()
                    .filter((p) -> downloadDto.getCategoryDto().getPluginPSID().equals(p.getPSID()))
                    .findAny();

            if (plugin.isPresent()) {
                Optional<Category> category = plugin.get().getGroups().get(0).getCategories().stream()
                        .filter((c) -> downloadDto.getCategoryDto().getCategoryCSID().equals(c.getCSID()))
                        .findAny();

                if (category.isPresent()) {
                    Download download = plugin.get().getSDownloadMapper().fromDto(downloadDto, category.get());
                    return download;
                }

            }

            throw new Exception();
        }
    }

}
