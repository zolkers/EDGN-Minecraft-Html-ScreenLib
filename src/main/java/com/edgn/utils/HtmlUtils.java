package com.edgn.utils;

public final class HtmlUtils {
    
    public static String decodeEntities(String html) {
        if (html == null || html.isEmpty()) {
            return html;
        }
        
        return html.replace("&lt;", "<")
                  .replace("&gt;", ">")
                  .replace("&amp;", "&")
                  .replace("&quot;", "\"")
                  .replace("&apos;", "'")
                  .replace("&#39;", "'");
    }
    
    public static String encodeEntities(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        
        return text.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&#39;");
    }
}
