package com.zhang.http.handler;

import com.zhang.http.TestPojo;
import com.zhang.http.request.GetRequest;
import com.zhang.http.request.PostRequest;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

public class PostHandlerTest {

    @Test
    public void testRead(){
        EmbeddedChannel channel = new EmbeddedChannel(new PostHandler());
        PostRequest request = new PostRequest();
        TestPojo test = new TestPojo();
        test.setAge(11);
        test.setName("zhang");
        request.setUri("/index.html");
        channel.writeInbound(request);
    }
}
