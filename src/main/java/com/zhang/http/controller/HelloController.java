package com.zhang.http.controller;

import com.zhang.http.annotation.Controller;
import com.zhang.http.annotation.RequestMapping;

@Controller
@RequestMapping("/hello")
public class HelloController {

    @RequestMapping("/world")
    public String hello(){
        return "helloWorld";
    }
}
