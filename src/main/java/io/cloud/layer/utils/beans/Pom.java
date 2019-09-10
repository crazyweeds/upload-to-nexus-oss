package io.cloud.layer.utils.beans;

import io.cloud.layer.utils.annotations.Param;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.core.io.FileSystemResource;

/**
 * @author RippleChan
 * @date 2019-09-10 23:55
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
public class Pom {

    /**
     * groupId
     */
    @Param(name = "maven2.groupId", require = true)
    private String groupId;

    /**
     * artifactId
     */
    @Param(name = "maven2.artifactId", require = true)
    private String artifactId;

    /**
     * version
     */
    @Param(name = "maven2.version", require = true)
    private String version;

    /**
     * 是否自动生成pom，本项目依赖的是本地仓库，所以始终为false
     */
    @Param(name = "maven2.generate-pom")
    private boolean generatePom;

    /**
     * 打包方式，非必选参数
     */
    @Param(name = "maven2.packaging")
    private String packaging;

    /**
     * 代码编译所生成的jar包,比如：xxx.jar
     */
    @Param(name = "maven2.asset1")
    private FileSystemResource asset1;

    /**
     * 类型
     */
    @Param(name = "maven2.asset1.classifier")
    private String asset1Classifier;

    /**
     * 扩展名
     */
    @Param(name = "maven2.asset1.extension")
    private String asset1Extension;

    /**
     * 源码jar包：比如:xxx-sources.jar
     */
    @Param(name = "maven2.asset2")
    private FileSystemResource asset2;

    /**
     * 类型：如果是源码的话，就是sources
     */
    @Param(name = "maven2.asset2.classifier")
    private String asset2Classifier;

    /**
     * 扩展名
     */
    @Param(name = "maven2.asset2.extension")
    private String asset2Extension;


    @Param(name = "maven2.asset3")
    private FileSystemResource asset3;

    @Param(name = "maven2.asset3.classifier")
    private String asset3Classifier;

    @Param(name = "maven2.asset3.extension")
    private String asset3Extension;

}
