package io.apicollab.uploader.fs;

import lombok.experimental.UtilityClass;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@UtilityClass
class FileSystemUploaderTestUtils {

    private static final String INPUT_FOLDER = "api_input";
    private static final String PROCESSED_FOLDER = "api_processed";
    private static final String ERROR_FOLDER = "api_error";

    public static void cleanupDirectory(String fileUploadDir) throws IOException {
        Files.walk(Paths.get(fileUploadDir))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .forEach(File::delete);
    }

    public void createFile(String fileUploadDir, String filename) throws IOException, InterruptedException {
        Path path = Paths.get(fileUploadDir, INPUT_FOLDER, filename);
        Files.createFile(path);
        Thread.sleep(500);
    }

    public void copyFile(String fileUploadDir, String filename) throws IOException, InterruptedException {
        copyFile(fileUploadDir, filename, 500);
    }

    public void copyFile(String fileUploadDir, String filename, int timeout) throws IOException, InterruptedException {
        Path path = Paths.get(fileUploadDir, INPUT_FOLDER, filename);
        Files.copy(FileSystemUploaderTestUtils.class.getClassLoader().getResourceAsStream(filename), path);
        Thread.sleep(timeout);
    }

    public void failureAssertion(String fileUploadDir, String filename) throws IOException {
        assertThat(Files.exists(Paths.get(fileUploadDir, INPUT_FOLDER, filename))).isFalse();
        try (Stream<Path> files = Files.list(Paths.get(fileUploadDir, ERROR_FOLDER))) {
            assertThat(Stream.of(files).count()).isEqualTo(1);
            String errorFilename = files.findFirst().get().getFileName().toString();
            assertThat(errorFilename).startsWith(FilenameUtils.getBaseName(filename));
            assertThat(errorFilename).endsWith(FilenameUtils.getExtension(filename));
        }
    }

    public void successAssertion(String fileUploadDir, String filename) throws IOException {
        assertThat(Files.exists(Paths.get(fileUploadDir, INPUT_FOLDER, filename))).isFalse();
        try (Stream<Path> files = Files.list(Paths.get(fileUploadDir, PROCESSED_FOLDER))) {
            assertThat(Stream.of(files).count()).isEqualTo(1);
            String errorFilename = files.findFirst().get().getFileName().toString();
            assertThat(errorFilename).startsWith(FilenameUtils.getBaseName(filename));
            assertThat(errorFilename).endsWith(FilenameUtils.getExtension(filename));
        }
    }
}
