package io.cloud.layer.utils;

import io.cloud.layer.DeployArtifacts;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Objects;
import java.util.Properties;

/**
 * @author RippleChan
 * @date 2019-09-15 00:31
 */
public class SystemUtils {

    static Log log = LogFactory.getLog(DeployArtifacts.class);

    public static void addProperties(Properties properties) {
        if (Objects.isNull(properties) || properties.isEmpty()) {
            log.warn("配置参数为空");
        }
        Properties defaultProperties = System.getProperties();
        defaultProperties.putAll(properties);
    }

}
