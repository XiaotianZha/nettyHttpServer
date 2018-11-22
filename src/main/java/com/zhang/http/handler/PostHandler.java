package com.zhang.http.handler;

import com.zhang.http.ContextHolder;
import com.zhang.http.HttpRequestHolder;
import com.zhang.http.model.ApplicationContext;
import com.zhang.http.model.PostResponse;
import com.zhang.http.model.Router;
import com.zhang.http.request.HttPRequest;
import com.zhang.http.request.PostRequest;
import com.zhang.http.util.JsonUtil;
import com.zhang.http.util.ParamUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class PostHandler extends ChannelInboundHandlerAdapter{

    private static Logger logger = LoggerFactory.getLogger(PostHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.debug("post handler");
        if(msg instanceof PostRequest){
            HttpRequestHolder.clear();
            PostRequest request =(PostRequest)msg;
            logger.info(request.getUri());
            //handle request param
            Map<String,String> params= this.processParam(request.getContentType(),request.getRawStr());
            com.zhang.http.request.HttPRequest httPRequest = new HttPRequest(params);
            HttpRequestHolder.setHolder(httPRequest);
            logger.info("toMap: "+params);
            //transfer to controller
            PostResponse<Object> result = this.transfer(request.getUri());
            HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            ctx.write(response);
            ctx.write(Unpooled.copiedBuffer(JsonUtil.toJson(result), CharsetUtil.UTF_8));
            ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            future.addListener(ChannelFutureListener.CLOSE);
        }else{
            super.channelRead(ctx, msg);
        }
    }

    private PostResponse<Object> transfer(String url){
        PostResponse<Object> response = new PostResponse<>();
        ApplicationContext context=ContextHolder.getContext();
        if (null != context){
            Router router = context.getRouters().get(url);
            if(router == null){
                logger.warn("can not found handler for this request {}",url);
            }else{
               Class<?> clazz= router.getClazz();
                try {
//                    clazz.getMethod()
                    Method method = clazz.getDeclaredMethod(router.getMethod(),null);
                    Object instance = router.getInstanse();
                    Object result=method.invoke(instance,null);
                    response.setContent(result);
                } catch (Exception e) {
                    logger.error(e.getMessage(),e);
                    response.setContent(e.getMessage());
                }
            }
        }else {
            logger.error("context init fail");
            response.setContent("context init fail");
        }
        return response;
    }

    private Map<String,String> processParam(String contentType,String rawPara){
        Map<String,String> params = new HashMap<>();
        if(contentType.contains("application/x-www-form-urlencoded")){
            params= ParamUtil.processGet(rawPara);
        }
        if (contentType.contains("application/json")){
            params=ParamUtil.processPost(rawPara);
        }
        return params;
    }
}
