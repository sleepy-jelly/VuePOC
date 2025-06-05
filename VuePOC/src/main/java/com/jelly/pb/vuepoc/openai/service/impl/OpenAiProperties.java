package com.jelly.pb.vuepoc.openai.service.impl;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "api.key")
public class OpenAiProperties {
    private String openai;

    public String getOpenai() {
        return openai;
    }

    public void setOpenai(String openai) {
        this.openai = openai;
    }
}