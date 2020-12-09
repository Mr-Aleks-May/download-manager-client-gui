package com.mraleksmay.projects.download_manager.client.gui.core.util.downloader;



import com.mraleksmay.projects.download_manager.common.annotation.NotNull;
import com.mraleksmay.projects.download_manager.common.model.download.Download;

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
        download.setStatus(Download.Status.UPDATING);
        download.setCurrentSize(0);

        HttpURLConnection conn = null;
        try {
            final File fullTempPathToFile = download.getFullTempPathToFile();
            final URL url = download.getUrl();
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.123 Safari/537.36");

            final InputStream is1 = conn.getInputStream();
            try (final InputStream is = conn.getInputStream();
                 final BufferedInputStream bis = new BufferedInputStream(is);
                 final FileOutputStream fos = new FileOutputStream(fullTempPathToFile)) {
                long cLength = conn.getContentLengthLong();
                download.setFullSize(cLength);
                download.setStatus(Download.Status.DOWNLOADING);

                int read = 0;
                final byte[] buffer = new byte[1024 * 4];

                while (download.getStatus() != Download.Status.STOP &&
                        (read = bis.read(buffer)) != -1 &&
                        download.getStatus() != Download.Status.STOP) {
                    fos.write(buffer, 0, read);
                    download.addBytes(read);
                }

                if (download.getCurrentSize() == download.getFullSize()) {
                    download.setStatus(Download.Status.FINISHING);

                    final File outputDir = download.getOutputDir();
                    final File fullPathToFile = download.getFullPathToFile();

                    if (!outputDir.exists()) {
                        outputDir.mkdirs();
                    }

                    final Path from = Paths.get(fullTempPathToFile.getCanonicalPath());
                    final Path to = Paths.get(fullPathToFile.getCanonicalPath());
                    Files.move(from, to, StandardCopyOption.REPLACE_EXISTING);

                    download.setStatus(Download.Status.DOWNLOADED);
                } else if (download.getFullSize() > -1 && download.getCurrentSize() > download.getFullSize()) {
                    download.setStatus(Download.Status.ERROR);
                } else {
                    download.setStatus(Download.Status.PAUSED);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            download.setStatus(Download.Status.ERROR);
        } catch (ProtocolException e) {
            e.printStackTrace();
            download.setStatus(Download.Status.ERROR);
        } catch (IOException e) {
            e.printStackTrace();
            download.setStatus(Download.Status.ERROR);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
