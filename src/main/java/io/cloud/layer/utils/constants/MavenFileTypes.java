package io.cloud.layer.utils.constants;

/**
 * @author RippleChan
 * @date 2019-09-10 22:45
 */
public enum MavenFileTypes {

    pom,
    jar;

    public static MavenFileTypes endWith(String name) {
        MavenFileTypes[] values = values();
        for (int i = 0; i < values.length; i++) {
            MavenFileTypes value = values[i];
            if (name.endsWith(value.name())) {
                return value;
            }
        }
        return null;
    }

}
