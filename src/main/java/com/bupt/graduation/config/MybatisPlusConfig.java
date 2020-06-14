package com.bupt.graduation.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 配置类
 *
 * @author wangz
 */
@Configuration
@EnableTransactionManagement
public class MybatisPlusConfig {

    /**
     * mybatisPlus 配置
     * 配置一个分页拦截器,否则即使使用分页查询得到的结果仍是全部的查询结果
     *
     * @return 分页拦截器
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
