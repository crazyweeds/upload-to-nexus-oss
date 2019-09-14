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

    public static ThreadPoolExecutor getThreadPoolExecutor() {
        String threadCount = System.getenv("thread.count");
        int coreSize = Runtime.getRuntime().availableProcessors() * 2;
        if (!StringUtils.isBlank(threadCount)) {
            coreSize = Integer.parseInt(threadCount);
        }
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(coreSize, coreSize * 10, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>());
        return threadPoolExecutor;
    }

}
