package com.voxlearning.poseidon.aerospike;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:hao.su@17zuoye.com">hao.su</a>
 * @version 2017-12-20
 * @since 17-12-20
 */
public class TestAeroSpike {

    private static final String HOST = "10.0.2.93";
    private static final int PORT = 3000;
    private static final String NAMESPACE = "vox";
    private static final String SETNAME = "TestBytes";

    private AedisClient aedisClient;

    @Before
    public void init() {
        aedisClient = new AedisClient(NAMESPACE, SETNAME, HOST, PORT);
    }

    @Test
    public void testAdd() {
        String rs = aedisClient.set("test-111", "value-111");
        Assert.assertEquals(rs, "OK");
    }

    @Test
    public void testRead() {
        String rs = aedisClient.get("test-111");
        Assert.assertEquals(rs, "value-111");
    }
}
