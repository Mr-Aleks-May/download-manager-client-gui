package com.mraleksmay.projects.download_manager.common.util.downloader;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class NetworkUtil {


    public ByteArrayOutputStream getImage(URL url) throws Exception {
        HttpURLConnection conn = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            // Open new connection
            conn = (HttpURLConnection) url.openConnection();
            // Set request properties
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.123 Safari/537.36");

            // Get input stream
            final InputStream is1 = conn.getInputStream();
            try (final InputStream is = conn.getInputStream();
                 final BufferedInputStream bis = new BufferedInputStream(is);) {
                int read = 0;
                final byte[] buffer = new byte[1024 * 4];

                // Read all file content
                while ((read = bis.read(buffer)) != -1) {
                    // Write data from buffer to ByteArrayStream
                    baos.write(buffer, 0, read);
                }

                // Return ByteArrayStream
                return baos;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return null;
    }
}
