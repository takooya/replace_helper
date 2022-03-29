package org.example.i18n.config;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author nobody
 */
@Slf4j
@Configuration
@MapperScan("org.example.i18n.mapper")
public class MybatisMapperConfig {
    @Bean
    public CreateTimeInterceptor createTimeInterceptor() {
        return new CreateTimeInterceptor();
    }
}
