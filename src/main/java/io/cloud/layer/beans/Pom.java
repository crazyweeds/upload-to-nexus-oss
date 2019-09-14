package io.cloud.layer.beans;

import io.cloud.layer.constants.MavenFileTypes;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.File;

/**
 * @author RippleChan
 * @date 2019-09-10 23:55
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@ToString
public class Pom {

    /**
     * groupId
     */
    private String groupId;

    /**
     * artifactId
     */
    private String artifactId;

    /**
     * version
     */
    private String version;

    /**
     * 是否自动生成pom，本项目依赖的是本地仓库，所以始终为false
     */
    private boolean generatePom;

    /**
     * pom文件
     */
    private File pomFile;

    /**
     * 文件
     */
    private File jarFile;

    /**
     * 源码
     */
    private File sourceFile;

    /**
     * 打包方式，非必选参数
     */
    private String packaging;


    /**
     * 和settings.xml相对应serverId
     */
    private String repositoryId;

    /**
     * 私有仓库地址
     */
    private String url;

    /**
     * 文件类型
     */
    private MavenFileTypes mavenFileTypes;


}
