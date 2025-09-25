package com.edgn.edml.resources;

import com.edgn.HTMLMyScreen;
import com.edgn.edml.exceptions.EdmlParsingException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class EdmlResourceLoader {

    public static String loadHtml(String filename) throws EdmlParsingException {
        String fullPath = EdmlResourceConfig.HTML_BASE_PATH + filename + EdmlResourceConfig.HTML_EXTENSION;
        return loadResourceWithValidation(fullPath, "HTML");
    }

    public static String loadHtmlWithPath(String path) throws EdmlParsingException {
        String fullPath = EdmlResourceConfig.HTML_BASE_PATH + path + EdmlResourceConfig.HTML_EXTENSION;
        return loadResourceWithValidation(fullPath, "HTML");
    }

    public static String loadCss(String filename) throws EdmlParsingException {
        String fullPath = EdmlResourceConfig.CSS_BASE_PATH + filename + EdmlResourceConfig.CSS_EXTENSION;
        return loadResourceWithValidation(fullPath, "CSS");
    }

    public static String loadCssWithPath(String path) throws EdmlParsingException {
        String fullPath = EdmlResourceConfig.CSS_BASE_PATH + path + EdmlResourceConfig.CSS_EXTENSION;
        return loadResourceWithValidation(fullPath, "CSS");
    }

    private static String loadResourceWithValidation(String resourcePath, String type) throws EdmlParsingException {
        HTMLMyScreen.LOGGER.info("EDGN: Attempting to load {} from: {}", type, resourcePath);

        try (InputStream inputStream = EdmlResourceLoader.class.getResourceAsStream(resourcePath)) {
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
                throw new EdmlParsingException(errorMsg);
            }

            String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            if (content.trim().isEmpty()) {
                HTMLMyScreen.LOGGER.warn("{} file is empty: {}", type, resourcePath);
            }

            System.out.println("EDGN: Successfully loaded " + type + " (" + content.length() + " characters)");
            return content;

        } catch (IOException e) {
            throw new EdmlParsingException("Failed to read " + type + " file: " + resourcePath + " - " + e.getMessage(), e);
        }
    }
}