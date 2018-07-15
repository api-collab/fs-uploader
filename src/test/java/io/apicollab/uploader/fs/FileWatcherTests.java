package io.apicollab.uploader.fs;

import io.apicollab.uploader.fs.service.ApiUploadService;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileWatcherTests {

    @Autowired
    private UploaderConfig uploaderConfig;

    @MockBean
    private ApiUploadService apiUploadService;

    @After
    public void cleanup() throws IOException {
        FileSystemUploaderTestUtils.cleanupDirectory(uploaderConfig.getBaseDir());
    }

    @Test
    public void upload_unsupported_filetype() throws IOException, InterruptedException {
        String filename = "abc.txt";
        FileSystemUploaderTestUtils.createFile(uploaderConfig.getBaseDir(), filename);
        Thread.sleep(100);
        FileSystemUploaderTestUtils.assertFailure(uploaderConfig.getBaseDir(), filename);
    }

    @Test
    public void supports_json() throws IOException, InterruptedException {
        String filename = "abc.json";
        doNothing().when(apiUploadService).upload(eq("abc.json"), anyString());
        FileSystemUploaderTestUtils.createFile(uploaderConfig.getBaseDir(), filename);
        Thread.sleep(100);
        FileSystemUploaderTestUtils.assertSuccess(uploaderConfig.getBaseDir(), filename);
    }

    @Test
    public void supports_yml() throws IOException, InterruptedException {
        String filename = "abc.yml";
        doNothing().when(apiUploadService).upload(eq("abc.yml"), anyString());
        FileSystemUploaderTestUtils.createFile(uploaderConfig.getBaseDir(), filename);
        Thread.sleep(100);
        FileSystemUploaderTestUtils.assertSuccess(uploaderConfig.getBaseDir(), filename);
    }

    @Test
    public void supports_yaml() throws IOException, InterruptedException {
        String filename = "abc.yaml";
        doNothing().when(apiUploadService).upload(eq("abc.yaml"), anyString());
        FileSystemUploaderTestUtils.createFile(uploaderConfig.getBaseDir(), filename);
        Thread.sleep(100);
        FileSystemUploaderTestUtils.assertSuccess(uploaderConfig.getBaseDir(), filename);
    }

    @Test
    public void move_to_error_folder_when_processing_failed() throws IOException, InterruptedException {
        String filename = "swagger.yml";
        doThrow(RuntimeException.class).when(apiUploadService).upload(eq("swagger.yml"), anyString());
        FileSystemUploaderTestUtils.createFile(uploaderConfig.getBaseDir(), filename);
        Thread.sleep(100);
        FileSystemUploaderTestUtils.assertFailure(uploaderConfig.getBaseDir(), filename);
    }

    @Test
    public void move_to_process_folder_when_processing_failed() throws IOException, InterruptedException {
        String filename = "swagger.yml";
        doThrow(RuntimeException.class).when(apiUploadService).upload(eq("swagger.yml"), anyString());
        FileSystemUploaderTestUtils.createFile(uploaderConfig.getBaseDir(), filename);
        Thread.sleep(100);
        FileSystemUploaderTestUtils.assertFailure(uploaderConfig.getBaseDir(), filename);
    }
}
