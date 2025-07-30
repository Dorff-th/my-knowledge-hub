package dev.mkhub.knowledge.search.util;

public class SummaryUtil {

    public static String extractSummary(String text, String keyword, int radius) {
        if (text == null || keyword == null) return null;

        String lowerText = text.toLowerCase();
        String lowerKeyword = keyword.toLowerCase();

        int idx = lowerText.indexOf(lowerKeyword);
        if (idx == -1) return null;

        int start = Math.max(0, idx - radius);
        int end = Math.min(text.length(), idx + keyword.length() + radius);

        StringBuilder sb = new StringBuilder();
        if (start > 0) sb.append("...");
        sb.append(text, start, end);
        if (end < text.length()) sb.append("...");

        return sb.toString();
    }
}

