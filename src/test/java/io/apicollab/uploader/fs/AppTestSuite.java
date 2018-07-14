package io.apicollab.uploader.fs;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.net.ServerSocket;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        FileSystemUploaderFailureTests.class,
        FileSystemUploaderSuccessTests.class
})
public class AppTestSuite {

    static {
        try {
            ServerSocket serverSocket = new ServerSocket(0);
            System.setProperty("upload.portal.port", String.valueOf(serverSocket.getLocalPort()));
        } catch (Exception e) {
            throw new RuntimeException("Error occurred during initialization.", e);
        }
    }
}
