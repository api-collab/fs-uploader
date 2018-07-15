package io.apicollab.uploader.fs.service;

import io.apicollab.uploader.fs.Constants;
import io.apicollab.uploader.fs.UploaderConfig;
import io.apicollab.uploader.fs.UploaderException;
import io.apicollab.uploader.fs.dto.ApiDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class ApiUploadService {

    @Autowired
    private UploaderConfig uploaderConfig;

    @Autowired
    private RestTemplate restTemplate;

    private String uriPrefix;
    private HttpHeaders httpHeaders;

    @PostConstruct
    private void initialize() {
        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        uriPrefix = uploaderConfig.getPortal().getProtocol()
                .concat("://")
                .concat(uploaderConfig.getPortal().getHost())
                .concat(":")
                .concat(uploaderConfig.getPortal().getPort())
                .concat("/applications/%s/apis");
    }

    public void upload(String filename, String spec) {
        ApiDTO apiDTO = ApiSpecParserService.parse(spec);
        if (StringUtils.isBlank(apiDTO.getApplicationId())) {
            throw new UploaderException(Constants.APP_ID_NOT_FOUND);
        }
        ByteArrayResource byteArrayResource = new ByteArrayResource(spec.getBytes()) {
            @Override
            public String getFilename() {
                return filename;
            }
        };
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("file", byteArrayResource);
        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, httpHeaders);
        ResponseEntity<?> responseEntity = restTemplate.exchange(
                String.format(uriPrefix, apiDTO.getApplicationId()),
                HttpMethod.POST,
                requestEntity,
                ApiDTO.class);
        assert HttpStatus.CREATED == responseEntity.getStatusCode() : Constants.UPLOAD_FAILED;
    }
}
