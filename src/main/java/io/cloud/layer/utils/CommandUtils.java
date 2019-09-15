package io.cloud.layer.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author RippleChan
 * @date 2019-09-15 00:54
 */
public class CommandUtils {

    public static boolean execCommand(String command) {
        String deployTimeout = System.getProperty("deploy.timeout");
        int timeout = 600;
        if (!StringUtils.isBlank(deployTimeout)) {
            timeout = Integer.parseInt(deployTimeout);
        }
        try {
            System.out.println("准备执行:" + command);
            Runtime runtime = Runtime.getRuntime();
            Process exec = runtime.exec(command);
            exec.waitFor(timeout, TimeUnit.SECONDS);
            int i = exec.exitValue();
            return i == 0;
        } catch (InterruptedException | IOException e) {
            System.out.println("命令执行异常:" + command);
            e.printStackTrace();
        }
        return false;
    }

    public static String getDeployCommandArgs(Map<String, String> args) {
        StringBuffer stringBuffer = new StringBuffer(" ");
        Iterator<Map.Entry<String, String>> iterator = args.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            String key = "-D" + next.getKey();
            String value = next.getValue();
            stringBuffer.append(key).append("=").append(value).append(" ");
        }
        return stringBuffer.toString();
    }

}
