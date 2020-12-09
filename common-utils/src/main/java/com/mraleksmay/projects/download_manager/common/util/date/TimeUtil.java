package com.mraleksmay.projects.download_manager.common.util.date;

import java.util.concurrent.TimeUnit;

public class TimeUtil {
    /**
     * Format time.
     *
     * @param millis
     * @param delimiter
     * @return
     */
    public static String formatTime(long millis, String delimiter) {
        StringBuilder sb = new StringBuilder();

        int days = (int) TimeUnit.MILLISECONDS.toDays(millis) % 30;
        int hours = (int) TimeUnit.MILLISECONDS.toHours(millis) % 24;
        int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(millis) % 60;
        int seconds = (int) TimeUnit.MILLISECONDS.toSeconds(millis) % 60;

        if (days > 0) {
            sb.append(String.format("%2s%s", days, delimiter).replace(' ', '0'));
        }
        if (hours > 0) {
            sb.append(String.format("%2s%s", hours, delimiter).replace(' ', '0'));
        }
        if (minutes > 0) {
            sb.append(String.format("%2s%s", minutes, delimiter).replace(' ', '0'));
        }
        if (seconds > 0) {
            sb.append(String.format("%2s%s", seconds, delimiter).replace(' ', '0'));
        }

        if (sb.length() > 0)
            return sb.deleteCharAt(sb.length() - 1).toString();
        else
            return sb.toString();
    }
}
