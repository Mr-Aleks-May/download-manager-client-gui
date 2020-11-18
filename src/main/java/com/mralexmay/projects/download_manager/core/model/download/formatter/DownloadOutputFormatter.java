package com.mralexmay.projects.download_manager.core.model.download.formatter;

import com.mralexmay.projects.download_manager.core.annotation.NotNull;
import com.mralexmay.projects.download_manager.core.model.download.Download;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

public abstract class DownloadOutputFormatter {
    protected final Download download;
    private long previousCurrentSize;
    private long previousCheckMillis;
    private float[] downloadSpeed = new float[10];

    public DownloadOutputFormatter(@NotNull final Download download) {
        this.download = download;
    }


    public long getId() {
        return download.getId();
    }

    public String getName() {
        return download.getFileName();
    }

    public String getFullSize() {
        long fullSize = download.getFullSize();

        if (fullSize <= 0) {
            return "undefined";
        }

        long absB = fullSize == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(fullSize);
        if (absB < 1024) {
            return fullSize + " B";
        }
        long value = absB;
        CharacterIterator ci = new StringCharacterIterator("KMGTPE");
        for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
            value >>= 10;
            ci.next();
        }
        value *= Long.signum(fullSize);
        return String.format("%.1f %ciB", value / 1024.0, ci.current());
    }

    public String getCurrentSize() {
        long currentSize = download.getCurrentSize();

        if (currentSize < 0) {
            return "undefined";
        }

        long absB = currentSize == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(currentSize);
        if (absB < 1024) {
            return currentSize + " B";
        }
        long value = absB;
        CharacterIterator ci = new StringCharacterIterator("KMGTPE");
        for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
            value >>= 10;
            ci.next();
        }
        value *= Long.signum(currentSize);
        return String.format("%.1f %ciB", value / 1024.0, ci.current());
    }

    public String getPath() throws IOException {
        return download.getOutputDir().getCanonicalPath().trim();
    }

    public String getUrl() throws MalformedURLException {
        return download.getUrl().toString().trim();
    }

    public String getProgress() {
        return String.format("%.2f%s", download.getProgress(), " %");
    }

    public String getStatus() {
        if (download.getStatus() == Download.Status.CREATED) {
            return "Created";
        }
        if (download.getStatus() == Download.Status.PAUSED) {
            return "Pause";
        }
        if (download.getStatus() == Download.Status.UPDATING) {
            return "Updating";
        }
        if (download.getStatus() == Download.Status.DOWNLOADING) {
            return "Downloading";
        }
        if (download.getStatus() == Download.Status.FINISHING) {
            return "Finishing";
        }
        if (download.getStatus() == Download.Status.DOWNLOADED) {
            return "Complete";
        }
        if (download.getStatus() == Download.Status.STOP) {
            return "Stop";
        }

        return "Error";
    }

    public String getSpeed() {
        if (download.getStatus() != Download.Status.DOWNLOADING) {
            if (downloadSpeed[0] != 0) {
                for (int i = 0; i < downloadSpeed.length; i++) {
                    downloadSpeed[i] = 0;
                }
            }

            return "";
        }

        long currentCheckMillis = System.currentTimeMillis();
        float speed = (float) (download.getCurrentSize() - previousCurrentSize) / (float) (currentCheckMillis - previousCheckMillis);
        previousCheckMillis = currentCheckMillis;
        previousCurrentSize = download.getCurrentSize();

        float avgSpeed = 0;
        downloadSpeed[downloadSpeed.length > 0 ? downloadSpeed.length - 1 : downloadSpeed.length] = speed;
        for (int i = 0; i < downloadSpeed.length - 1; i++) {
            avgSpeed += downloadSpeed[i];
            downloadSpeed[i] = downloadSpeed[i + 1];
        }
        avgSpeed /= downloadSpeed.length;
        avgSpeed *= 1000;

        long val = (long) avgSpeed;
        long absB = val == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(val);
        if (absB < 1024) {
            return val + " B";
        }
        long value = absB;
        CharacterIterator ci = new StringCharacterIterator("KMGTPE");
        for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
            value >>= 10;
            ci.next();
        }
        value *= Long.signum(val);

        return String.format("%.2f %sB/s", avgSpeed / 1024, ci.current());
    }
}
