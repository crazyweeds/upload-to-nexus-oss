package io.cloud.layer.utils;

/**
 * @author RippleChan
 * @date 2019-09-15 00:23
 */
public class MavenUtils {

    public static String getDefaultLocalRepo() {
        String separator = System.getProperty("file.separator");
        return System.getProperty("user.home") + separator + ".m2" + separator + "repository" + separator;
    }

}
