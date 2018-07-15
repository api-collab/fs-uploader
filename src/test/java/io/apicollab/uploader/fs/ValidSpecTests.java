package io.apicollab.uploader.fs;

import io.apicollab.uploader.fs.dto.ApiDTO;
import io.apicollab.uploader.fs.service.ApiUploadService;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.ServerSocket;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ValidSpecTests {

    private static final int port;
    private static final String responseJson = "{\"id\":\"1\"}";

    static {
        try {
            if (System.getProperty("upload.portal.port") == null) {
                ServerSocket serverSocket = new ServerSocket(0);
                port = serverSocket.getLocalPort();
                System.setProperty("upload.portal.port", String.valueOf(port));
            } else {
                port = Integer.valueOf(System.getProperty("upload.portal.port"));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error occurred during initialization.", e);
        }
    }

    @Autowired
    private UploaderConfig uploaderConfig;
    @Autowired
    private ApiUploadService apiUploadService;

    @MockBean
    private RestTemplate restTemplate;

    @After
    public void cleanup() throws IOException {
        FileSystemUploaderTestUtils.cleanupDirectory(uploaderConfig.getBaseDir());
    }

    @Test
    public void upload_oas3_json() throws IOException {
        ResponseEntity response = new ResponseEntity<>(responseJson, HttpStatus.CREATED);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(ApiDTO.class)))
                .thenReturn(response);
        String spec = FileSystemUploaderTestUtils.loadSpec("valid_oas3.json");
        assertThatCode(() -> apiUploadService.upload("swagger.json", spec)).doesNotThrowAnyException();
    }

    @Test
    public void upload_oas3_json_with_invalid_appid() throws IOException {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(ApiDTO.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        String spec = FileSystemUploaderTestUtils.loadSpec("valid_oas3_invalid_appl.json");
        assertThatExceptionOfType(AssertionError.class).isThrownBy(
                () -> apiUploadService.upload("swagger.json", spec))
                .withMessage(Constants.UPLOAD_FAILED);
    }

    @Test
    public void upload_oas3_yml() throws IOException {
        ResponseEntity response = new ResponseEntity<>(responseJson, HttpStatus.CREATED);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(ApiDTO.class)))
                .thenReturn(response);
        String spec = FileSystemUploaderTestUtils.loadSpec("valid_oas3.yml");
        assertThatCode(() -> apiUploadService.upload("swagger.yml", spec)).doesNotThrowAnyException();
    }

    @Test
    public void upload_oas3_yml_with_invalid_appid() throws IOException {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(ApiDTO.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        String spec = FileSystemUploaderTestUtils.loadSpec("valid_oas3_invalid_appl.yml");
        assertThatExceptionOfType(AssertionError.class).isThrownBy(
                () -> apiUploadService.upload("swagger.yml", spec))
                .withMessage(Constants.UPLOAD_FAILED);
    }
}
