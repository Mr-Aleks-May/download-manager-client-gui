package com.mralexmay.projects.download_manager.util;

import com.mralexmay.projects.download_manager.core.model.download.Download;
import com.mralexmay.projects.download_manager.core.annotation.NotNull;
import com.mralexmay.projects.download_manager.core.util.NetWorker;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class AdvancedNetWorker implements NetWorker {
    /**
     * Download from begin.
     *
     * @param download
     */
    @Override
    public synchronized void downloadOnDiskSync(@NotNull final Download download) {
        download.setStatus(Download.Status.UPDATING);

        HttpURLConnection conn = null;
        try {
            final URL url = download.getUrl();
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.123 Safari/537.36");

            if (download.getCurrentSize() == 0) {
                startDownloadOnDisk(download, conn);
            } else if (isDownloadResumeable(download, conn)) {
                resumeDownloadOnDisk(download, conn);
            } else {
                startDownloadOnDisk(download, conn);
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

    public synchronized void startDownloadOnDisk(@NotNull final Download download,
                                                 HttpURLConnection conn) throws IOException {
        final File fullPath = download.getFullTempPathToFile();
        final InputStream is1 = conn.getInputStream();
        try (final InputStream is = conn.getInputStream();
             final BufferedInputStream bis = new BufferedInputStream(is);
             final FileOutputStream fos = new FileOutputStream(fullPath)) {
            long cLength = conn.getContentLengthLong();
            download.setFullSize(cLength);
            download.setStatus(Download.Status.DOWNLOADING);

            int read = 0;
            final byte[] buffer = new byte[1024];

            while (download.getStatus() != Download.Status.STOP &&
                    (read = bis.read(buffer)) != -1 &&
                    download.getStatus() != Download.Status.STOP) {
                fos.write(buffer, 0, read);
                download.addBytes(read);
            }

            if (download.getCurrentSize() == download.getFullSize()) {
                download.setStatus(Download.Status.DOWNLOADED);
            } else if (download.getFullSize() > -1 && download.getCurrentSize() > download.getFullSize()) {
                download.setStatus(Download.Status.ERROR);
            } else {
                download.setStatus(Download.Status.PAUSED);
            }
        }
    }

    public synchronized void resumeDownloadOnDisk(@NotNull final Download download,
                                                  HttpURLConnection conn) throws IOException {
        File fullPath = download.getFullTempPathToFile();

        try (InputStream is = conn.getInputStream();
             BufferedInputStream bis = new BufferedInputStream(is);
             FileOutputStream fos = new FileOutputStream(fullPath, true)) {
            download.setStatus(Download.Status.DOWNLOADING);

            int read = 0;
            byte[] buffer = new byte[1024];

            while (download.getStatus() != Download.Status.STOP &&
                    (read = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, read);
                download.addBytes(read);
            }

            if (download.getCurrentSize() == download.getFullSize()) {
                download.setStatus(Download.Status.DOWNLOADED);
            } else if (download.getFullSize() > -1 && download.getCurrentSize() > download.getFullSize()) {
                download.setStatus(Download.Status.ERROR);
            } else {
                download.setStatus(Download.Status.PAUSED);
            }
        }
    }

    public synchronized boolean isDownloadResumeable(Download download, HttpURLConnection conn) throws IOException {
        String range = String.format("bytes %s-", download.getCurrentSize());
        conn.setRequestProperty("Content-Range", range);

        int responseCode = conn.getResponseCode();
        if (conn.getResponseCode() / 100 == 2)
            return true;
        else
            return false;
    }
}
