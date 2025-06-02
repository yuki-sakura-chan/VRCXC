package com.github.vrcxc.config;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    /**
     * <p>配置 Jackson 序列化 Long 类型转为 String 类型</p>
     * <p>避免前端精度丢失问题</p>
     *
     * @return Jackson2ObjectMapperBuilderCustomizer
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder
                .serializerByType(Long.TYPE, new ToStringSerializer())
                .serializerByType(Long.class, new ToStringSerializer());
    }

}
