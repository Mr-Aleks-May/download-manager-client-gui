package com.mraleksmay.projects.download_manager.common.util.file;

import java.io.*;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

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
     * Write text data to file.
     *
     * @param pathToFile full path to file
     * @param content    data to write
     * @throws IOException
     */
    public static void writeContentToFile(File pathToFile, StringBuilder content) throws IOException {
        // Create FileWriter and BufferedWriter
        try (FileWriter fw = new FileWriter(pathToFile);
             BufferedWriter bw = new BufferedWriter(fw);
             IntStream is = content.chars();) {
            // Write all text data to file
            is.forEach((c) -> {
                try {
                    bw.write(c);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
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

    public static Byte[] readFileAsBytes(File pluginDir) throws IOException {
        List<Byte> bytes = new ArrayList<>();

        // Create FileReader and FileWriter
        try (InputStream is = new FileInputStream(pluginDir.getCanonicalFile());
             BufferedInputStream bis = new BufferedInputStream(is);) {


            byte[] buffer = new byte[1024 * 4];

            // Read all bytes from file
            for (int read = 0; (read = bis.read(buffer, 0, buffer.length)) != -1; ) {
                // Read bytes and add
                bytes.addAll(Arrays.asList(toObjects(buffer)));
            }
        }

        // Return file content
        return bytes.toArray(new Byte[0]);
    }

    private static Byte[] toObjects(byte[] bytesPrim) {
        Byte[] bytes = new Byte[bytesPrim.length];
        Arrays.setAll(bytes, n -> bytesPrim[n]);
        return bytes;
    }
}
