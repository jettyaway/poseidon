package com.voxlearning.poseidon.settings;

import org.junit.Assert;
import org.junit.Test;

/**
 * Setting 测试类
 *
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-12-11
 * @since 17-12-11
 */
public class SettingTest {

    @Test
    public void testSettingRead() {
        Setting setting = new Setting("test.setting", true);
        String pass = setting.getStr("demo.pass").orElse("abc");
        Assert.assertEquals(pass, "123456");
    }

    @Test
    public void testForAbsPath() {
        Setting setting = new Setting("/home/suhao/gitproject/poseidon/poseidon-settings/target/test-classes/test.setting", true);
        String pass = setting.getStr("demo.pass").orElse("abc");
        Assert.assertEquals(pass, "123456");
    }

    @Test
    public void testAutoLoad() {
        Setting setting = new Setting("test.setting", true);
        setting.autoLoad(true);
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
