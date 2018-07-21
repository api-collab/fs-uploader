package io.apicollab.uploader.fs;

import io.apicollab.uploader.fs.service.ApiUploadService;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.ServerSocket;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InvalidSpecTests {

    static {
        try {
            if (System.getProperty("upload.portal.port") == null) {
                ServerSocket serverSocket = new ServerSocket(0);
                System.setProperty("upload.portal.port", String.valueOf(serverSocket.getLocalPort()));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error occurred during initialization.", e);
        }
    }

    @Autowired
    private UploaderConfig uploaderConfig;

    @Autowired
    private ApiUploadService apiUploadService;

    @After
    public void cleanup() throws IOException {
        FileSystemUploaderTestUtils.cleanupDirectory(uploaderConfig.getBaseDir());
    }

    @Test
    public void upload_empty_spec() {
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(
                () -> apiUploadService.upload("swagger.yml", ""))
                .withMessage(Constants.API_SPEC_EMPTY);
    }

    @Test
    public void upload_oas3_json_without_application_id() throws IOException {
        String spec = FileSystemUploaderTestUtils.loadSpec("no_appl_oas3.json");
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(
                () -> apiUploadService.upload("swagger.json", spec))
                .withMessage(Constants.APP_ID_NOT_FOUND);
    }

    @Test
    public void upload_oas3_yml_without_application_id() throws IOException {
        String spec = FileSystemUploaderTestUtils.loadSpec("no_appl_oas3.yml");
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(
                () -> apiUploadService.upload("swagger.yml", spec))
                .withMessage(Constants.APP_ID_NOT_FOUND);
    }

    @Test
    public void upload_invalid_oas3_yml() throws IOException {
        String spec = FileSystemUploaderTestUtils.loadSpec("invalid_oas3.yml");
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(
                () -> apiUploadService.upload("swagger.yml", spec))
                .withMessage(Constants.API_SPEC_PARSE_FAILURE);
    }

    @Test
    public void upload_invalid_oas3_json() throws IOException {
        String spec = FileSystemUploaderTestUtils.loadSpec("invalid_oas3.json");
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(
                () -> apiUploadService.upload("swagger.json", spec))
                .withMessage(Constants.API_SPEC_PARSE_FAILURE);
    }

    @Test
    public void upload_invalid_swagger2_yml() throws IOException {
        String spec = FileSystemUploaderTestUtils.loadSpec("invalid_swagger2.yml");
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(
                () -> apiUploadService.upload("swagger.yml", spec))
                .withMessage(Constants.API_SPEC_PARSE_FAILURE);
    }

    @Test
    public void upload_invalid_swagger2_json() throws IOException {
        String spec = FileSystemUploaderTestUtils.loadSpec("invalid_swagger2.json");
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(
                () -> apiUploadService.upload("swagger.json", spec))
                .withMessage(Constants.API_SPEC_PARSE_FAILURE);
    }

    @Test
    public void upload_swagger2_json_without_application_id() throws IOException {
        String spec = FileSystemUploaderTestUtils.loadSpec("no_appl_swagger2.json");
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(
                () -> apiUploadService.upload("swagger.json", spec))
                .withMessage(Constants.APP_ID_NOT_FOUND);
    }

    @Test
    public void upload_swagger2_yml_without_application_id() throws IOException {
        String filename = "no_appl_swagger2.yml";
        String spec = FileSystemUploaderTestUtils.loadSpec("no_appl_swagger2.yml");
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(
                () -> apiUploadService.upload("swagger.yml", spec))
                .withMessage(Constants.APP_ID_NOT_FOUND);
    }
}
