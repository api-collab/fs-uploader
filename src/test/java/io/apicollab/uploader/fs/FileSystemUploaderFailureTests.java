package io.apicollab.uploader.fs;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.ServerSocket;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileSystemUploaderFailureTests {

    private static final String INPUT_FOLDER = "api_input";
    private static final String ERROR_FOLDER = "api_error";

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

    @After
    public void cleanup() throws IOException {
        FileSystemUploaderTestUtils.cleanupDirectory(uploaderConfig.getBaseDir());
    }

    @Test
    public void upload_unsupported_filetype() throws IOException, InterruptedException {
        String filename = "abc.txt";
        FileSystemUploaderTestUtils.createFile(uploaderConfig.getBaseDir(), filename);
        FileSystemUploaderTestUtils.failureAssertion(uploaderConfig.getBaseDir(), filename);
    }

    @Test
    public void upload_empty_yml() throws IOException, InterruptedException {
        String filename = "abc.yml";
        FileSystemUploaderTestUtils.createFile(uploaderConfig.getBaseDir(), filename);
        FileSystemUploaderTestUtils.failureAssertion(uploaderConfig.getBaseDir(), filename);
    }

    @Test
    public void upload_empty_yaml() throws IOException, InterruptedException {
        String filename = "abc.yaml";
        FileSystemUploaderTestUtils.createFile(uploaderConfig.getBaseDir(), filename);
        FileSystemUploaderTestUtils.failureAssertion(uploaderConfig.getBaseDir(), filename);
    }

    @Test
    public void upload_empty_json() throws IOException, InterruptedException {
        String filename = "abc.json";
        FileSystemUploaderTestUtils.createFile(uploaderConfig.getBaseDir(), filename);
        FileSystemUploaderTestUtils.failureAssertion(uploaderConfig.getBaseDir(), filename);
    }

    @Test
    public void upload_oas3_json_without_application_id() throws IOException, InterruptedException {
        String filename = "no_appl_oas3.json";
        FileSystemUploaderTestUtils.copyFile(uploaderConfig.getBaseDir(), filename);
        FileSystemUploaderTestUtils.failureAssertion(uploaderConfig.getBaseDir(), filename);
    }

    @Test
    public void upload_oas3_yml_without_application_id() throws IOException, InterruptedException {
        String filename = "no_appl_oas3.yml";
        FileSystemUploaderTestUtils.copyFile(uploaderConfig.getBaseDir(), filename);
        FileSystemUploaderTestUtils.failureAssertion(uploaderConfig.getBaseDir(), filename);
    }

    @Test
    public void upload_oas3_yaml_without_application_id() throws IOException, InterruptedException {
        String filename = "no_appl_oas3.yaml";
        FileSystemUploaderTestUtils.copyFile(uploaderConfig.getBaseDir(), filename);
        FileSystemUploaderTestUtils.failureAssertion(uploaderConfig.getBaseDir(), filename);
    }

    @Test
    public void upload_invalid_oas3_yml() throws IOException, InterruptedException {
        String filename = "invalid_oas3.yml";
        FileSystemUploaderTestUtils.copyFile(uploaderConfig.getBaseDir(), filename);
        FileSystemUploaderTestUtils.failureAssertion(uploaderConfig.getBaseDir(), filename);
    }

    @Test
    public void upload_invalid_oas3_yaml() throws IOException, InterruptedException {
        String filename = "invalid_oas3.yaml";
        FileSystemUploaderTestUtils.copyFile(uploaderConfig.getBaseDir(), filename);
        FileSystemUploaderTestUtils.failureAssertion(uploaderConfig.getBaseDir(), filename);
    }

    @Test
    public void upload_invalid_oas3_json() throws IOException, InterruptedException {
        String filename = "invalid_oas3.json";
        FileSystemUploaderTestUtils.copyFile(uploaderConfig.getBaseDir(), filename);
        FileSystemUploaderTestUtils.failureAssertion(uploaderConfig.getBaseDir(), filename);
    }

    @Test
    public void upload_invalid_swagger2_yml() throws IOException, InterruptedException {
        String filename = "invalid_swagger2.yml";
        FileSystemUploaderTestUtils.copyFile(uploaderConfig.getBaseDir(), filename);
        FileSystemUploaderTestUtils.failureAssertion(uploaderConfig.getBaseDir(), filename);
    }

    @Test
    public void upload_invalid_swagger2_yaml() throws IOException, InterruptedException {
        String filename = "invalid_swagger2.yaml";
        FileSystemUploaderTestUtils.copyFile(uploaderConfig.getBaseDir(), filename);
        FileSystemUploaderTestUtils.failureAssertion(uploaderConfig.getBaseDir(), filename);
    }

    @Test
    public void upload_invalid_swagger2_json() throws IOException, InterruptedException {
        String filename = "invalid_swagger2.json";
        FileSystemUploaderTestUtils.copyFile(uploaderConfig.getBaseDir(), filename);
        FileSystemUploaderTestUtils.failureAssertion(uploaderConfig.getBaseDir(), filename);
    }

    @Test
    public void upload_swagger2_json_without_application_id() throws IOException, InterruptedException {
        String filename = "no_appl_swagger2.json";
        FileSystemUploaderTestUtils.copyFile(uploaderConfig.getBaseDir(), filename);
        FileSystemUploaderTestUtils.failureAssertion(uploaderConfig.getBaseDir(), filename);
    }

    @Test
    public void upload_swagger2_yml_without_application_id() throws IOException, InterruptedException {
        String filename = "no_appl_swagger2.yml";
        FileSystemUploaderTestUtils.copyFile(uploaderConfig.getBaseDir(), filename);
        FileSystemUploaderTestUtils.failureAssertion(uploaderConfig.getBaseDir(), filename);
    }

    @Test
    public void upload_swagger2_yaml_without_application_id() throws IOException, InterruptedException {
        String filename = "no_appl_swagger2.yaml";
        FileSystemUploaderTestUtils.copyFile(uploaderConfig.getBaseDir(), filename);
        FileSystemUploaderTestUtils.failureAssertion(uploaderConfig.getBaseDir(), filename);
    }
}
