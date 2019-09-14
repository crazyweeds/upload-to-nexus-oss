package io.cloud.layer.utils;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

/**
 * @see PomUtils
 * @author RippleChan
 * @date 2019-09-14 22:32
 */
public class PomUtilsTest {

    private ArrayList<File> files = new ArrayList<>();

    @Before
    public void init() {
        String filePath = "/Users/chenruibo/.m2/repository/tk/mybatis/mapper-core/1.1.5";
        String[] list = new File(filePath).list();
        for (int i = 0; i < list.length; i++) {
            String s = list[i];
            File file = new File(s);
            files.add(file);
        }
    }

    /**
     * @see PomUtils#onlyPom(java.util.List)
     */
    @Test
    public void onlyPomTest() {
        boolean jar = PomUtils.onlyPom(files);
        System.out.println(jar);
    }

    /**
     * @see PomUtils#isJar(java.util.List)
     */
    @Test
    public void isJarTest() {
        boolean jar = PomUtils.isJar(files);
        System.out.println(jar);
    }

    @Test
    public void isSource() {
        boolean jar = PomUtils.isSource(files);
        System.out.println(jar);
    }

}
