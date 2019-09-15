package io.cloud.layer.common;


import java.io.File;
import java.io.FileFilter;

/**
 * @author RippleChan
 * @date 2019-09-10 22:42
 */
public class PomFileFilter implements FileFilter {

    private static final String FILE_SUFFIX = "pom";

    @Override
    public boolean accept(File pathname) {
        return pathname.getName().endsWith(FILE_SUFFIX);
    }

}
