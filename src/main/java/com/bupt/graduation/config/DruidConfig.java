package com.bupt.graduation.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;

/**
 * 配置 Druid,后台监控数据库信息
 * <p>
 * http://{ you address }/druid/login.html
 * </p>
 *
 * @author wangz
 * @since 1.0
 */
@Configuration
public class DruidConfig {

    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource druidDataSource() {
        return new DruidDataSource();
    }


    @Bean
    public ServletRegistrationBean<StatViewServlet> statViewServlet() {


        ServletRegistrationBean<StatViewServlet> registrationBean = new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");

        HashMap<String, String> init = new HashMap<>(3);
        init.put("loginUsername", "wangzhigang");
        init.put("loginPassword", "zhigang911");
        init.put("allow", "");


        registrationBean.setInitParameters(init);

        return registrationBean;
    }


}
