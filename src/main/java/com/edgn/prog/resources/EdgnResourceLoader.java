package com.edgn.prog.resources;

import com.edgn.HTMLMyScreen;
import com.edgn.prog.exceptions.EdgnParsingException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class EdgnResourceLoader {

    public static String loadHtml(String filename) throws EdgnParsingException {
        String fullPath = EdgnResourceConfig.HTML_BASE_PATH + filename + EdgnResourceConfig.HTML_EXTENSION;
        return loadResourceWithValidation(fullPath, "HTML");
    }

    public static String loadHtmlWithPath(String path) throws EdgnParsingException {
        String fullPath = EdgnResourceConfig.HTML_BASE_PATH + path + EdgnResourceConfig.HTML_EXTENSION;
        return loadResourceWithValidation(fullPath, "HTML");
    }

    public static String loadCss(String filename) throws EdgnParsingException {
        String fullPath = EdgnResourceConfig.CSS_BASE_PATH + filename + EdgnResourceConfig.CSS_EXTENSION;
        return loadResourceWithValidation(fullPath, "CSS");
    }

    public static String loadCssWithPath(String path) throws EdgnParsingException {
        String fullPath = EdgnResourceConfig.CSS_BASE_PATH + path + EdgnResourceConfig.CSS_EXTENSION;
        return loadResourceWithValidation(fullPath, "CSS");
    }

    private static String loadResourceWithValidation(String resourcePath, String type) throws EdgnParsingException {
        HTMLMyScreen.LOGGER.info("EDGN: Attempting to load {} from: {}", type, resourcePath);

        try (InputStream inputStream = EdgnResourceLoader.class.getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                String errorMsg = String.format(
                        """
                                EDGN %s file not found: %s
                                Expected structure:
                                  /edgn/html/ for .edml files
                                  /edgn/css/ for .edss files
                                Make sure the file exists in src/main/resources%s""",
                        type, resourcePath, resourcePath
                );
                throw new EdgnParsingException(errorMsg);
            }

            String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            if (content.trim().isEmpty()) {
                throw new EdgnParsingException(type + " file is empty: " + resourcePath);
            }

            System.out.println("EDGN: Successfully loaded " + type + " (" + content.length() + " characters)");
            return content;

        } catch (IOException e) {
            throw new EdgnParsingException("Failed to read " + type + " file: " + resourcePath + " - " + e.getMessage(), e);
        }
    }
}