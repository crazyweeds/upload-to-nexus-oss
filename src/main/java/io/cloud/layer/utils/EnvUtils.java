package io.cloud.layer.utils;

import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.util.*;

/**
 * @author RippleChan
 * @date 2019-09-15 00:31
 */
public class EnvUtils {

    public static void addEnv(Properties properties) {
        Set<Map.Entry<Object, Object>> entries = properties.entrySet();
        Iterator<Map.Entry<Object, Object>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<Object, Object> next = iterator.next();
            Map<String, String> env = System.getenv();
            if (!Objects.isNull(next.getValue())) {
                env.put(next.getKey().toString(), next.getValue().toString());
            }
        }
    }

}
