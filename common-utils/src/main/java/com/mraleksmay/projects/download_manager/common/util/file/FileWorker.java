package com.mraleksmay.projects.download_manager.common.util.file;

import java.io.*;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

/**
 * Contain methods for working with files
 */
public class FileWorker {

    /**
     * Write text data to file.
     *
     * @param pathToFile full path to file
     * @param content    data to write
     * @throws IOException
     */
    public static void writeContentToFile(File pathToFile, String content) throws IOException {
        // Create FileWriter and BufferedWriter
        try (FileWriter fw = new FileWriter(pathToFile);
             BufferedWriter bw = new BufferedWriter(fw);) {
            // Write all text data to file
            bw.write(content);
            // Flush on disk
            bw.flush();
        }
    }

    /**
     * Read all data from file.
     *
     * @param pathToFile full path to file
     * @return file content
     * @throws IOException
     */
    public static StringBuilder readFileContent(File pathToFile) throws IOException {
        StringBuilder sb = new StringBuilder();

        // Create FileReader and FileWriter
        try (FileReader fr = new FileReader(pathToFile.getCanonicalFile());
             BufferedReader br = new BufferedReader(fr);) {

            // Read all lines from file
            for (String line = null; (line = br.readLine()) != null; ) {
                // Read line and add to String builder
                sb.append(line + "\n");
            }
        }

        // Return file content
        return sb;
    }

    /**
     * Format file size to human readable format.
     *
     * @param size file size
     * @return file size in human readable format
     */
    public static String getSizeFormatted(long size) {
        if (size < 0) {
            return "undefined";
        }

        long absB = size == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(size);
        if (absB < 1024) {
            return size + " B";
        }
        long value = absB;
        CharacterIterator ci = new StringCharacterIterator("KMGTPE");
        for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
            value >>= 10;
            ci.next();
        }
        value *= Long.signum(size);
        return String.format("%.1f %ciB", value / 1024.0, ci.current());
    }
}
