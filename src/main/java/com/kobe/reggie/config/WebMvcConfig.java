package com.kobe.reggie.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.kobe.reggie.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

@Slf4j
public class WebMvcConfig  implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("resource mapping");
        //如果將網頁不放在static,直接放在resources下可以采用以下方式映射網頁
        //registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend");
       // registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front");
        registry.addResourceHandler("classpath:/static/");
    }
    @ConfigurationProperties("spring.datasource")
    @Bean
    public DataSource dataSource() throws SQLException {
        DruidDataSource druidDataSource = new DruidDataSource();
        return druidDataSource;
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(new JacksonObjectMapper());
        converters.add(0,converter);
        //WebMvcConfigurer.super.extendMessageConverters(converters);

    }
}
