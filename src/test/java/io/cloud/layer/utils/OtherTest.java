package io.cloud.layer.utils;

import org.junit.Test;

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
