package com.jiubo.oa.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.jiubo.oa.filter.CorsFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @desc:配置类
 * @date: 2020-01-02 13:20
 * @author: dx
 * @version: 1.0
 */
@Configuration
public class OaConfig {

    //分页插件
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        //List<ISqlParser> sqlParserList = new ArrayList<>();
        // 攻击 SQL 阻断解析器、加入解析链
        //sqlParserList.add(new BlockAttackSqlParser());
        //paginationInterceptor.setSqlParserList(sqlParserList);
        return paginationInterceptor;
    }

    //注册过滤器
    @Bean
    public FilterRegistrationBean registFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new CorsFilter());
        registration.addUrlPatterns("/*");
        registration.setName("CorsFilter");
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
