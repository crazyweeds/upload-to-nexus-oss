package io.cloud.layer.utils;

import io.cloud.layer.beans.Pom;
import io.cloud.layer.constants.MavenFileTypes;

import java.util.HashMap;
import java.util.Map;

import static io.cloud.layer.DeployArtifacts.SUCCESS_COUNT;
import static io.cloud.layer.DeployArtifacts.TOTAL_COUNT;

/**
 * @author RippleChan
 * @date 2019-09-14 23:39
 */
public class DeployUtils {

    private static final String COMMAND = "mvn deploy:deploy-file";


    public static void deployPom(Pom pom) {
        TOTAL_COUNT.addAndGet(1);
        Map<String, String> arg = defaultArgs(pom);
        arg.put("packaging", MavenFileTypes.POM.getType());
        arg.put("file", pom.getPomFile().getPath());
        String deployCommandArgs = CommandUtils.getDeployCommandArgs(arg);
        boolean b = CommandUtils.execCommand(COMMAND + deployCommandArgs);
        if (b) {
            SUCCESS_COUNT.addAndGet(1);
        }
    }

    public static void deployJar(Pom pom) {
        TOTAL_COUNT.addAndGet(1);
        Map<String, String> arg = defaultArgs(pom);
        arg.put("file", pom.getJarFile().getPath());
        arg.put("packaging", MavenFileTypes.JAR.getType());
        String deployCommandArgs = CommandUtils.getDeployCommandArgs(arg);
        boolean b = CommandUtils.execCommand(COMMAND + deployCommandArgs);
        if (b) {
            SUCCESS_COUNT.addAndGet(1);
        }
    }

    public static void deploySource(Pom pom) {
        TOTAL_COUNT.addAndGet(1);
        Map<String, String> arg = defaultArgs(pom);
        arg.put("packaging", MavenFileTypes.SOURCE.getType());
        arg.put("file", pom.getJarFile().getPath());
        String deployCommandArgs = CommandUtils.getDeployCommandArgs(arg);
        boolean b = CommandUtils.execCommand(COMMAND + deployCommandArgs);
        if (b) {
            SUCCESS_COUNT.addAndGet(1);
        }
    }

    private static Map<String, String> defaultArgs(Pom pom) {
        Map<String, String> arg = new HashMap<>();
        arg.put("pomFile", pom.getPomFile().getPath());
        arg.put("repositoryId", System.getProperty("repository.id"));
        arg.put("url", System.getProperty("target.repo.url"));
        arg.put("generatePom", "false");
        arg.put("artifactId", pom.getArtifactId());
        arg.put("version", pom.getVersion());
        arg.put("groupId", pom.getGroupId());
        return arg;
    }


}
