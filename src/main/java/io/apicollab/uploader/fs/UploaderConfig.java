package io.apicollab.uploader.fs;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "upload")
@EnableConfigurationProperties
@Data
public class UploaderConfig {

    @Value("#{systemProperties['java.io.tmpdir']}")
    private String baseDir;
    private List<String> supportedFileTypes = Arrays.asList("json", "yaml", "yml");
    private Portal portal = new Portal();

    @Data
    public class Portal {
        private String host;
        private String port;
        private String protocol;
    }
}
