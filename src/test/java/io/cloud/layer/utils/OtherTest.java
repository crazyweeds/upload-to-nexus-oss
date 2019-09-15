package io.cloud.layer.utils;

import cn.hutool.core.io.FileUtil;
import io.cloud.layer.common.PomFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author RippleChan
 * @date 2019-09-15 00:17
 */
public class OtherTest {

    @Test
    public void userHomeTest() {
        String property = System.getProperty("user.home");
        System.out.println(property);
        String separator = System.getProperty("file.separator");
        String s = System.getProperty("user.home") + separator + ".m2" + separator + "repository";
        System.out.println(s);
    }


}
