package com.mraleksmay.projects.download_manager.client.gui.platform.util.downloader;


import com.mraleksmay.projects.download_manager.common.annotation.NotNull;
import com.mraleksmay.projects.download_manager.plugin.model.download.Download;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class BasicNetWorker implements NetWorker {
    /**
     * Download from begin.
     *
     * @param download
     */
    @Override
    public synchronized void downloadOnDisk(@NotNull final Download download) {
        // Update download state
        download.setStatus(Download.Status.UPDATING);
        download.setCurrentSize(0);

        HttpURLConnection conn = null;
        try {
            // Create temp file
            final File fullTempPathToFile = download.getFullTempPathToFile();
            // Open connection
            final URL url = download.getUrl();
            conn = (HttpURLConnection) url.openConnection();
            // Set properties to connection
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.123 Safari/537.36");

            // Open Input Stream and File Output Stream
            try (final InputStream is = conn.getInputStream();
                 final BufferedInputStream bis = new BufferedInputStream(is);
                 final FileOutputStream fos = new FileOutputStream(fullTempPathToFile)) {
                // Set size
                long cLength = conn.getContentLengthLong();
                download.setFullSize(cLength);
                // Update download status to downloading
                download.setStatus(Download.Status.DOWNLOADING);

                // Create buffers
                int read = 0;
                final byte[] buffer = new byte[1024 * 4];

                // Download file while data available or user not cancelled download
                while (download.getStatus() != Download.Status.STOP &&
                        (read = bis.read(buffer)) != -1 &&
                        download.getStatus() != Download.Status.STOP) {
                    // Write data to temp file
                    fos.write(buffer, 0, read);
                    // Update downloaded size
                    download.addBytes(read);
                }

                // Check if download is completed
                if (download.getCurrentSize() == download.getFullSize() || bis.read() == -1) {
                    // If download completed set download state to finishing
                    download.setStatus(Download.Status.FINISHING);
                    download.setFullSize(download.getCurrentSize());

                    // Create output file information
                    final File outputDir = download.getOutputDir();
                    final File fullPathToFile = download.getFullPathToFile();

                    // Check if output path exists
                    if (!outputDir.exists()) {
                        // Create directories
                        outputDir.mkdirs();
                    }

                    // Move file from temp to output directory
                    final Path from = Paths.get(fullTempPathToFile.getCanonicalPath());
                    final Path to = Paths.get(fullPathToFile.getCanonicalPath());
                    Files.move(from, to, StandardCopyOption.REPLACE_EXISTING);

                    // Update download state
                    download.setStatus(Download.Status.DOWNLOADED);
                } else if (download.getFullSize() > -1 && download.getCurrentSize() > download.getFullSize()) {
                    download.setStatus(Download.Status.ERROR);
                } else {
                    download.setStatus(Download.Status.PAUSED);
                }
            }
            // Update download state if error occurred
        } catch (MalformedURLException e) {
            download.setStatus(Download.Status.ERROR);
        } catch (ProtocolException e) {
            download.setStatus(Download.Status.ERROR);
        } catch (IOException e) {
            download.setStatus(Download.Status.ERROR);
        } finally {
            // Close connection
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
