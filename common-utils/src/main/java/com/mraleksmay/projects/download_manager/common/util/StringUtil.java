package com.mraleksmay.projects.download_manager.common.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StringUtil {

    public static List<String> split(String content, String delimiter) {
        final List<String> substrings = new ArrayList<>();

        for (int currentIndex = 0, lastIndex = 0;
             (currentIndex = content.indexOf(delimiter, lastIndex)) >= 0;
             currentIndex++) {
            substrings.add(content.substring(lastIndex, currentIndex));
            lastIndex = currentIndex;
        }

        if (substrings.size() == 0) {
            substrings.add(content);
        }

        return Collections.unmodifiableList(substrings);
    }

    public static List<String> split(StringBuilder content, String delimiter) {
        final List<String> substrings = new ArrayList<>();

        for (int currentIndex = 0, lastIndex = 0;
             (currentIndex = content.indexOf(delimiter, lastIndex)) >= 0;
             currentIndex++) {
            substrings.add(content.substring(lastIndex, currentIndex));
            lastIndex = currentIndex;
        }

        if (substrings.size() == 0) {
            substrings.add(content.toString());
        }

        return Collections.unmodifiableList(substrings);
    }
}
