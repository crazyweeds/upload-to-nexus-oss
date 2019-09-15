package io.cloud.layer.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author RippleChan
 * @date 2019-09-10 22:45
 */
@AllArgsConstructor
@Getter
public enum MavenFileTypes {

    POM("pom", false),
    JAR("jar", false),
    SOURCE("java-source", false);

    private String type;

    private boolean generatePom;


}
