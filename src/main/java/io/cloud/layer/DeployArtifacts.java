package io.cloud.layer;

import cn.hutool.core.io.FileUtil;
import io.cloud.layer.beans.Pom;
import io.cloud.layer.common.PomFileFilter;
import io.cloud.layer.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author RippleChan
 * @date 2019-09-10 22:30
 */
public class DeployArtifacts {

    public static final AtomicInteger TOTAL_COUNT = new AtomicInteger(0);

    public static final AtomicInteger SUCCESS_COUNT = new AtomicInteger(0);

    /**
     * main method
     *
     * @param args args[0]={config file path}
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        long l = System.currentTimeMillis();
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
        /**
         * 检查线程池
         */
        while (true) {
            try {
                ThreadPoolExecutor threadPoolExecutor = ThreadUtils.getThreadPoolExecutor();
                int activeCount = threadPoolExecutor.getActiveCount();
                long completedTaskCount = threadPoolExecutor.getCompletedTaskCount();
                int size = threadPoolExecutor.getQueue().size();
                long taskCount = threadPoolExecutor.getTaskCount();
                System.out.println("上传中：" + activeCount + "；完成：" + completedTaskCount + ";等待：" + size + ";总数：" + taskCount);
                TimeUnit.SECONDS.sleep(1);
                if (activeCount == 0 && taskCount - completedTaskCount == 0) {
                    threadPoolExecutor.shutdown();
                    long l3 = (System.currentTimeMillis() - l) / (1000 * 60);
                    System.out.println("任务执行完成，一共耗费：" + l3 + "分钟");
                    System.out.println("上传次数：" + TOTAL_COUNT + ";成功次数：" + SUCCESS_COUNT);
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void initConfig(String[] args) throws IOException {
        Properties defaultConfig = PropertiesLoaderUtils.loadAllProperties("config.properties");
        SystemUtils.addProperties(defaultConfig);
        if (args.length != 0) {
            FileSystemResource fileSystemResource = new FileSystemResource(new File(args[0]));
            Properties customConfig = PropertiesLoaderUtils.loadProperties(fileSystemResource);
            SystemUtils.addProperties(customConfig);
        }
    }

    /**
     * 将文件部署到远程仓库
     *
     * @param poms
     */
    private static void deploy2RemoteRepository(Map<String, List<File>> poms) {
        if (poms.isEmpty()) {
            System.out.println("等待上传的文件列表为空");
            return;
        }
        Iterator<Map.Entry<String, List<File>>> iterator = poms.entrySet().iterator();
        ThreadPoolExecutor threadPoolExecutor = ThreadUtils.getThreadPoolExecutor();
        System.out.println("pom数量：" + poms.size());
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
                    Runnable runnable = () -> {
                        DeployUtils.deployPom(pom);
                    };
                    threadPoolExecutor.execute(runnable);
                }
                /**
                 * 上传jar
                 */
                boolean jar = PomUtils.isJar(files);
                if (jar) {
                    Runnable runnable = () -> {
                        PomUtils.setJarFile(pom, files);
                        DeployUtils.deployJar(pom);
                    };
                    threadPoolExecutor.execute(runnable);
                }
                /**
                 * 上传源码
                 */
                boolean source = PomUtils.isSource(files);
                if (source) {
                    Runnable runnable = () -> {
                        PomUtils.setSource(pom,files);
                        DeployUtils.deploySource(pom);
                    };
                    threadPoolExecutor.execute(runnable);
                }
            } catch (IOException | XmlPullParserException e) {
                System.err.println("POM解析失败：" + pomFilePath);
                e.printStackTrace();
            }
        }
        System.out.println("pom全部提交至线程池");
    }

    /**
     * 以pom作为key，构建文件列表
     *
     * @param pomFiles
     * @return
     */
    private static Map<String, List<File>> buildFiles(Map<String, File> pomFiles) {
        Set<Map.Entry<String, File>> entries = pomFiles.entrySet();
        Iterator<Map.Entry<String, File>> iterator = entries.iterator();
        ConcurrentHashMap<String, List<File>> pomMap = new ConcurrentHashMap<>(pomFiles.size());
        String property = System.getProperty("file.separator");
        while (iterator.hasNext()) {
            Map.Entry<String, File> next = iterator.next();
            String key = next.getKey();
            File value = next.getValue();
            String parent = value.getParent();
            File file = new File(parent);
            String[] list = file.list();
            ArrayList<File> files = new ArrayList<>();
            for (int i = 0; i < list.length; i++) {
                File tempFile = new File(parent + property + list[i]);
                System.out.println(tempFile);
                files.add(tempFile);
            }
            pomMap.put(key, files);
        }
        return pomMap;
    }

    /**
     * 获取所有pom文件
     *
     * @return
     */
    private static Map<String, File> getAllPomFiles() {
        String path = System.getProperty("local.repository.path");
        if (StringUtils.isBlank(path)) {
            path = MavenUtils.getDefaultLocalRepo();
            System.out.println("未设置本地仓库路径，将使用默认本地仓库：" + path);
        } else {
            System.out.println("仓库路径：" + path);
        }
        File file = new File(path);
        PomFileFilter pomFileFilter = new PomFileFilter();
        List<File> pomList = FileUtil.loopFiles(file,pomFileFilter);
        if (Objects.isNull(pomList)) {
            throw new RuntimeException("empty list");
        }
        Map<String, File> pomMap = new HashMap<>(pomList.size());
        for (int i = 0; i < pomList.size(); i++) {
            File pom = pomList.get(i);
            if (!pomMap.containsValue(pom)) {
                pomMap.put(pom.getPath(), pom);
            }
        }
        return pomMap;
    }

}
