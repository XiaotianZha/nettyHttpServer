package com.zhang.http.controller;

import com.zhang.http.HttpRequestHolder;
import com.zhang.http.annotation.Controller;
import com.zhang.http.annotation.RequestMapping;
import com.zhang.http.model.Person;
import com.zhang.http.request.HttPRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/hello")
public class HelloController {

    private static Logger logger = LoggerFactory.getLogger(HelloController.class);

    @RequestMapping("/world")
    public String hello(){
        return "helloWorld";
    }

    @RequestMapping("/person")
    public Person person(){
        HttPRequest request=HttpRequestHolder.getHttpRequest();
        logger.info("request param {}",request.getParams());
        Person p = new Person();
        if (StringUtils.isNotEmpty(request.getParams().get("name"))){
            p.setName(request.getParams().get("name"));
        }
        return p;
    }
}
