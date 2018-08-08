package com.zhang.http.handler;

import com.zhang.http.request.GetRequest;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

public class GetHandlerTest {

    @Test
    public void testRead(){
        EmbeddedChannel channel = new EmbeddedChannel(new GetHandler());
        GetRequest request = new GetRequest();
        request.setUri("/index.html");
        channel.writeInbound(request);
    }
}
