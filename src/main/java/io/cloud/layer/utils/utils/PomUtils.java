package io.cloud.layer.utils.utils;

import io.cloud.layer.utils.beans.Pom;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

/**
 * 解析pom文件的信息
 * @author RippleChan
 * @date 2019-09-10 23:55
 */
public class PomUtils {

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
                .build();
        return build;
    }

}
