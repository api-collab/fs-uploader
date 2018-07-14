package io.apicollab.uploader.fs;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.ServerSocket;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileSystemUploaderSuccessTests {

    private static final String INPUT_FOLDER = "api_input";
    private static final String PROCESSED_FOLDER = "api_processed";
    private static final String ERROR_FOLDER = "api_error";

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

    @Rule
    public final WireMockRule wireMockRule = new WireMockRule(port);

    @Autowired
    private UploaderConfig uploaderConfig;

    @After
    public void cleanup() throws IOException {
        FileSystemUploaderTestUtils.cleanupDirectory(uploaderConfig.getBaseDir());
    }

    @Test
    public void upload_oas3_json() throws IOException, InterruptedException {
        wireMockRule.stubFor(post(urlEqualTo("/applications/1/apis"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withBody(responseJson)
                        .withHeader("Content-Type", "application/json")));
        String filename = "valid_oas3.json";
        FileSystemUploaderTestUtils.copyFile(uploaderConfig.getBaseDir(), filename, 2000);
        FileSystemUploaderTestUtils.successAssertion(uploaderConfig.getBaseDir(), filename);
        wireMockRule.resetAll();
    }

    @Test
    public void upload_oas3_json_with_invalid_appid() throws IOException, InterruptedException {
        wireMockRule.stubFor(post(urlEqualTo("/applications/11/apis"))
                .willReturn(aResponse()
                        .withStatus(404)));
        String filename = "valid_oas3_invalid_appl.json";
        FileSystemUploaderTestUtils.copyFile(uploaderConfig.getBaseDir(), filename, 2000);
        FileSystemUploaderTestUtils.failureAssertion(uploaderConfig.getBaseDir(), filename);
        wireMockRule.resetAll();
    }

    @Test
    public void upload_oas3_yml() throws IOException, InterruptedException {
        wireMockRule.stubFor(post(urlEqualTo("/applications/1/apis"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withBody(responseJson)
                        .withHeader("Content-Type", "application/json")));
        String filename = "valid_oas3.yml";
        FileSystemUploaderTestUtils.copyFile(uploaderConfig.getBaseDir(), filename, 2000);
        FileSystemUploaderTestUtils.successAssertion(uploaderConfig.getBaseDir(), filename);
        wireMockRule.resetAll();
    }

    @Test
    public void upload_oas3_yml_with_invalid_appid() throws IOException, InterruptedException {
        wireMockRule.stubFor(post(urlEqualTo("/applications/11/apis"))
                .willReturn(aResponse()
                        .withStatus(404)));
        String filename = "valid_oas3_invalid_appl.yml";
        FileSystemUploaderTestUtils.copyFile(uploaderConfig.getBaseDir(), filename, 2000);
        FileSystemUploaderTestUtils.failureAssertion(uploaderConfig.getBaseDir(), filename);
        wireMockRule.resetAll();
    }

    @Test
    public void upload_oas3_yaml() throws IOException, InterruptedException {
        wireMockRule.stubFor(post(urlEqualTo("/applications/1/apis"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withBody(responseJson)
                        .withHeader("Content-Type", "application/json")));
        String filename = "valid_oas3.yaml";
        FileSystemUploaderTestUtils.copyFile(uploaderConfig.getBaseDir(), filename, 2000);
        FileSystemUploaderTestUtils.successAssertion(uploaderConfig.getBaseDir(), filename);
        wireMockRule.resetAll();
    }

    @Test
    public void upload_oas3_yaml_with_invalid_appid() throws IOException, InterruptedException {
        wireMockRule.stubFor(post(urlEqualTo("/applications/11/apis"))
                .willReturn(aResponse()
                        .withStatus(404)));
        String filename = "valid_oas3_invalid_appl.yaml";
        FileSystemUploaderTestUtils.copyFile(uploaderConfig.getBaseDir(), filename, 2000);
        FileSystemUploaderTestUtils.failureAssertion(uploaderConfig.getBaseDir(), filename);
        wireMockRule.resetAll();
    }
}
