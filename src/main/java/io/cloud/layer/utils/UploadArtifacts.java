package io.cloud.layer.utils;

import io.cloud.layer.utils.beans.Pom;
import io.cloud.layer.utils.common.PomFileFilter;
import io.cloud.layer.utils.utils.PomUtils;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author RippleChan
 * @date 2019-09-10 22:30
 */
@Slf4j
public class UploadArtifacts {

    /**
     * @param args args[0] 本地路径
     *             args[] 远程仓库地址
     *             args[] 远程仓库用户名
     *             args[] 远程仓库密码
     *             args[1] 线程数，默认CPU核心数*2
     *             args[2]
     */
    public static void main(String[] args) {
        String filePath = "/Users/chenruibo/Desktop/test";
        /**
         * 获取所有pom文件列表
         */
        Map<String, File> pomFiles = getAllPomFiles(filePath);
        /**
         * 构建数据，做上传前的准备
         */
        Map<String, List<File>> poms = buildFiles(pomFiles);
        /**
         * 开始上传
         */
        uploadFile2NexusOss(poms);
    }

    private static void uploadFile2NexusOss(Map<String, List<File>> poms) {
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
                 * pom文件
                 */
                FileSystemResource asset1 = new FileSystemResource(pomFilePath);
                pom.setAsset1(asset1);
                pom.setAsset1Extension("pom");
                /**
                 * jar文件
                 */
                files.forEach(file -> {
                    String name = file.getName();
                    /**
                     * jar文件
                     */
                    if (name.endsWith("jar") && !name.endsWith("sources.jar")) {
                        FileSystemResource fileSystemResource = new FileSystemResource(file);
                        pom.setAsset2(fileSystemResource);
                        pom.setAsset2Extension("jar");
                    }
                    /**
                     * sources文件
                     */
                    if (name.endsWith("sources.jar")) {
                        FileSystemResource fileSystemResource = new FileSystemResource(file);
                        pom.setAsset3(fileSystemResource);
                        pom.setAsset3Extension("jar");
                    }

                });
            } catch (IOException e) {
                log.error("", e);
                e.printStackTrace();
            } catch (XmlPullParserException e) {
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
     * @param path
     * @return
     */
    private static Map<String, File> getAllPomFiles(String path) {
        if (Objects.isNull(path)) {
            throw new RuntimeException("Path can not be null");
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
