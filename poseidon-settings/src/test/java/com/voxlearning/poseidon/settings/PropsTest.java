package com.voxlearning.poseidon.settings;

import com.voxlearning.poseidon.settings.dialect.Props;
import org.junit.Assert;
import org.junit.Test;

/**
 * Props 单元测试
 *
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-12-05
 * @since 17-12-5
 */
public class PropsTest {

    @Test
    public void testRead() {
        Props props = new Props("test.properties");
        String user = props.getProperty("user");
        Assert.assertEquals(user, "root");
        int passwd = props.getInt("pass").orElse(123);
        Assert.assertEquals(passwd, 123456);

        String driver = props.getProperty("driver");
        Assert.assertEquals(driver, "com.mysql.jdbc.Driver");
    }


    public void testAbsPath() {
        Props props = new Props("/home/suhao/gitproject/poseidon/poseidon-settings/src/test/resources/test.properties");
        String user = props.getProperty("user");
        Assert.assertEquals(user, "root");
        int passwd = props.getInt("pass").orElse(123);
        Assert.assertEquals(passwd, 123456);

        String driver = props.getProperty("driver");
        Assert.assertEquals(driver, "com.mysql.jdbc.Driver");
    }
}
