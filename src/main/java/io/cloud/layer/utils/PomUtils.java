package io.cloud.layer.utils;

import io.cloud.layer.beans.Pom;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * 解析pom文件的信息
 *
 * @author RippleChan
 * @date 2019-09-10 23:55
 */
public class PomUtils {

    private static final String SOURCES_FILE_SUFFIX = "sources.jar";
    private static final String JAR_FILE_SUFFIX = ".jar";
    private static final String POM_FILE_NAME = ".pom";

    public static Pom parsePom(String pomFilePath) throws IOException, XmlPullParserException {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = reader.read(new FileReader(pomFilePath));
        String groupId = null;
        String artifactId = model.getArtifactId();
        String version = null;
        if (!Objects.isNull(model.getParent())) {
            Parent parent = model.getParent();
            groupId = parent.getGroupId();
        } else {
            groupId = model.getGroupId();
        }
        if (Objects.isNull(model.getVersion())) {
            version = model.getParent().getVersion();
        } else {
            version = model.getVersion();
        }
        String packaging = model.getPackaging();
        Pom build = Pom.builder()
                .groupId(groupId)
                .artifactId(artifactId)
                .version(version)
                .packaging(packaging)
                .pomFile(new File(pomFilePath))
                .build();
        return build;
    }

    /**
     * 是否包含源码
     *
     * @param files
     * @return
     */
    public static boolean isSource(List<File> files) {
        if (Objects.isNull(files) || files.isEmpty()) {
            return false;
        }
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            if (file.getName().toLowerCase().endsWith(SOURCES_FILE_SUFFIX)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否包含jar
     *
     * @param files
     * @return
     */
    public static boolean isJar(List<File> files) {
        if (Objects.isNull(files) || files.isEmpty()) {
            return false;
        }
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            if (!file.getName().toLowerCase().endsWith(SOURCES_FILE_SUFFIX) && file.getName().toLowerCase().endsWith(JAR_FILE_SUFFIX)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 是否只有pom
     *
     * @return
     */
    public static boolean onlyPom(List<File> files) {
        if (isSource(files)) {
            return false;
        }
        if (isJar(files)) {
            return false;
        }
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            if (file.getName().endsWith(POM_FILE_NAME)) {
                return true;
            }
        }
        return false;
    }

    public static void setJarFile(Pom pom, List<File> files) {
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            if (!file.getName().toLowerCase().endsWith(SOURCES_FILE_SUFFIX) && file.getName().toLowerCase().endsWith(JAR_FILE_SUFFIX)) {
                pom.setJarFile(file);
            }
        }
    }

    public static void setSource(Pom pom, List<File> files) {
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            if (file.getName().toLowerCase().endsWith(SOURCES_FILE_SUFFIX)) {
                pom.setSourceFile(file);
            }
        }
    }

}
