package com.example.utils;

import java.io.File;
import java.io.InputStream;

public class ResourceUtil {
    public static InputStream getResourceAsStream(String path) {
        InputStream in = ResourceUtil.class.getResourceAsStream(path);
        if (in == null) throw new IllegalArgumentException("Resource not found: " + path);
        return in;
    }

    public static File getResourceAsFile(String path) {
        try {
            File f = new File(ResourceUtil.class.getResource(path).toURI());
            return f;
        } catch (Exception e) {
            throw new IllegalArgumentException("Resource not found: " + path);
        }
    }
}
