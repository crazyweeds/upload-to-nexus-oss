package io.cloud.layer.utils;


import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author RippleChan
 * @date 2019-09-14 23:46
 */
public class ThreadUtils {

    private static ThreadPoolExecutor threadPoolExecutor;

    public static ThreadPoolExecutor getThreadPoolExecutor() {
        if (!Objects.isNull(threadPoolExecutor)) {
            return threadPoolExecutor;
        }
        String threadCount = System.getProperty("thread.count");
        int coreSize = Runtime.getRuntime().availableProcessors() * 2;
        if (!StringUtils.isBlank(threadCount)) {
            coreSize = Integer.parseInt(threadCount);
        }
        threadPoolExecutor = new ThreadPoolExecutor(coreSize, coreSize * 10, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>());
        return threadPoolExecutor;
    }

}
