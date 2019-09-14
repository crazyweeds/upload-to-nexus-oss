package io.cloud.layer;

import io.cloud.layer.beans.Pom;
import io.cloud.layer.common.PomFileFilter;
import io.cloud.layer.utils.DeployUtils;
import io.cloud.layer.utils.EnvUtils;
import io.cloud.layer.utils.MavenUtils;
import io.cloud.layer.utils.PomUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author RippleChan
 * @date 2019-09-10 22:30
 */
@Slf4j
public class DeployArtifacts {

    /**
     * main method
     * @param args args[0]={config file path}
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        /**
         * 解析并初始化配置
         */
        initConfig(args);
        /**
         * 获取所有pom文件列表
         */
        Map<String, File> pomFiles = getAllPomFiles();
        /**
         * 构建数据，做上传前的准备
         */
        Map<String, List<File>> poms = buildFiles(pomFiles);
        /**
         * 开始上传
         */
        deploy2RemoteRepository(poms);
    }

    private static void initConfig(String[] args) throws IOException {
        Properties defaultConfig = PropertiesLoaderUtils.loadAllProperties("config.properties");
        EnvUtils.addEnv(defaultConfig);
        if (args.length != 0) {
            FileSystemResource fileSystemResource = new FileSystemResource(new File(args[0]));
            Properties customConfig = PropertiesLoaderUtils.loadProperties(fileSystemResource);
            EnvUtils.addEnv(customConfig);
        }
    }

    /**
     * 将文件部署到远程仓库
     * @param poms
     */
    private static void deploy2RemoteRepository(Map<String, List<File>> poms) {
        if (poms.isEmpty()) {
            log.warn("等待上传的文件列表为空");
            return;
        }
        Iterator<Map.Entry<String, List<File>>> iterator = poms.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<File>> next = iterator.next();
            String pomFilePath = next.getKey();
            List<File> files = next.getValue();
            try {
                Pom pom = PomUtils.parsePom(pomFilePath);
                /**
                 * 上传POM
                 */
                boolean onlyPom = PomUtils.onlyPom(files);
                if (onlyPom) {
                    DeployUtils.deployPom(pom);
                }
                /**
                 * 上传jar
                 */
                boolean jar = PomUtils.isJar(files);
                if (jar) {
                    DeployUtils.deployPom(pom);
                }
                /**
                 * 上传源码
                 */
                boolean source = PomUtils.isSource(files);
                if (source) {
                    DeployUtils.deploySource(pom);
                }
            } catch (IOException | XmlPullParserException e) {
                log.error("", e);
                e.printStackTrace();
            }
        }
    }

    /**
     * 以pom作为key，构建文件列表
     * @param pomFiles
     * @return
     */
    private static Map<String, List<File>> buildFiles(Map<String, File> pomFiles) {
        Set<Map.Entry<String, File>> entries = pomFiles.entrySet();
        Iterator<Map.Entry<String, File>> iterator = entries.iterator();
        ConcurrentHashMap<String, List<File>> pomMap = new ConcurrentHashMap<>(pomFiles.size());
        while (iterator.hasNext()) {
            Map.Entry<String, File> next = iterator.next();
            String key = next.getKey();
            File value = next.getValue();
            String parent = value.getParent();
            File file = new File(parent);
            String[] list = file.list();
            ArrayList<File> files = new ArrayList<>();
            for (int i = 0; i < list.length; i++) {
                File tempFile = new File(parent + System.getProperty("file.separator") + list[i]);
                files.add(tempFile);
            }
            pomMap.put(key, files);
        }
        return pomMap;
    }

    /**
     * 获取所有pom文件
     * @return
     */
    private static Map<String, File> getAllPomFiles() {
        String path = System.getenv("local.repository.path");
        if (StringUtils.isBlank(path)) {
            path = MavenUtils.getDefaultLocalRepo();
            log.warn("未设置本地仓库路径，将使用默认本地仓库：{}", path);
        } else {
            log.info("仓库路径：{}", path);
        }
        File file = new File(path);
        PomFileFilter pomFileFilter = new PomFileFilter();
        File[] pomList = file.listFiles(pomFileFilter);
        if (Objects.isNull(pomList)) {
            throw new RuntimeException("empty list");
        }
        Map<String, File> pomMap = new HashMap<>(pomList.length);
        for (int i = 0; i < pomList.length; i++) {
            File pom = pomList[i];
            if (!pomMap.containsValue(pom)) {
                pomMap.put(pom.getPath(), pom);
            }
        }
        return pomMap;
    }

}
