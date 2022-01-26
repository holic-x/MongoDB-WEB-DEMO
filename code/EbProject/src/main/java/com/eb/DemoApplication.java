package com.eb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class DemoApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    // 不重写打包war部署到tomcat接口会报404
    // 问题分析:（vue.config.js中module.exports配置publicPath:'/boxing/static'\''）,原来没有加下述配置，通过‘’不能访问到index.html页面
    // 后续项目部署到linux的时候发现访问404,加上下述配置后方能正常访问
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DemoApplication.class);
    }

}