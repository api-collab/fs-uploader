package io.apicollab.uploader.fs;

import lombok.experimental.UtilityClass;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@UtilityClass
class FileSystemUploaderTestUtils {

    public static void cleanupDirectory(String fileUploadDir) throws IOException {
        Files.walk(Paths.get(fileUploadDir))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .forEach(File::delete);
    }

    public String loadSpec(String filename) throws IOException {
        return IOUtils.toString(
                FileSystemUploaderTestUtils.class.getClassLoader().getResourceAsStream(filename),
                StandardCharsets.UTF_8.name());
    }

    public void createFile(String fileUploadDir, String filename) throws IOException {
        Path path = Paths.get(fileUploadDir, Constants.INPUT_FOLDER, filename);
        Files.createFile(path);
    }

    public Callable<Boolean> assertFailure(String fileUploadDir, String filename) throws IOException {
        return () -> {
            assertThat(Files.exists(Paths.get(fileUploadDir, Constants.INPUT_FOLDER, filename))).isFalse();
            try (Stream<Path> files = Files.list(Paths.get(fileUploadDir, Constants.ERROR_FOLDER))) {
                assertThat(Stream.of(files).count()).isEqualTo(1);
                String errorFilename = files.findFirst().get().getFileName().toString();
                assertThat(errorFilename).startsWith(FilenameUtils.getBaseName(filename));
                assertThat(errorFilename).endsWith(FilenameUtils.getExtension(filename));
            }
            return true;
        };
    }

    public Callable<Boolean> assertSuccess(String fileUploadDir, String filename) throws IOException {
        return () -> {
            assertThat(Files.exists(Paths.get(fileUploadDir, Constants.INPUT_FOLDER, filename))).isFalse();
            try (Stream<Path> files = Files.list(Paths.get(fileUploadDir, Constants.PROCESSED_FOLDER))) {
                assertThat(Stream.of(files).count()).isEqualTo(1);
                String errorFilename = files.findFirst().get().getFileName().toString();
                assertThat(errorFilename).startsWith(FilenameUtils.getBaseName(filename));
                assertThat(errorFilename).endsWith(FilenameUtils.getExtension(filename));
            }
            return true;
        };
    }
}
