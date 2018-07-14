package io.apicollab.uploader.fs;

import io.apicollab.uploader.fs.service.ApiUploadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

@Component
@Slf4j
class FileWatcher {

    private static final String INPUT_FOLDER = "api_input";
    private static final String PROCESSED_FOLDER = "api_processed";
    private static final String ERROR_FOLDER = "api_error";

    @Autowired
    private ApiUploadService apiUploadService;
    @Autowired
    private UploaderConfig uploaderConfig;

    @Async
    public void initialize() throws InterruptedException, IOException {
        createRequiredDirectories();
        setupFileWatcher();
    }

    private void setupFileWatcher() throws InterruptedException, IOException {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path path = Paths.get(uploaderConfig.getBaseDir(), INPUT_FOLDER);
        path.register(watchService, ENTRY_CREATE);

        WatchKey watchKey;
        log.info("Scanning filesystem for changes");
        log.info("Place api specs in '{}' folder to upload them to portal", path.toAbsolutePath().toString());
        while ((watchKey = watchService.take()) != null) {
            for (WatchEvent<?> event : watchKey.pollEvents()) {
                log.info("File: {} - {}", event.context(), event.kind());
                String filename = event.context().toString();
                log.debug("Processing file: {}", filename);
                try {
                    String spec = loadSpec(filename);
                    apiUploadService.upload(spec);
                    moveToProcessedFolder(filename);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    moveToErrorFolder(filename);
                }
            }
            log.info("Scanning filesystem for changes...");
            watchKey.reset();
        }
    }

    private String loadSpec(String filename) throws IOException {
        String swaggerSpec;
        String fileType = FilenameUtils.getExtension(filename);
        if (!uploaderConfig.getSupportedFileTypes().contains(fileType)) {
            throw new RuntimeException("Unsupported file type - " + filename);
        }
        Path path = Paths.get(uploaderConfig.getBaseDir(), INPUT_FOLDER, filename);
        try (FileInputStream inputStream = FileUtils.openInputStream(new File(path.toAbsolutePath().toString()))) {
            swaggerSpec = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while reading file content.", e);
        }
        return swaggerSpec;
    }

    private void moveToErrorFolder(String filename) {
        Path inputPath = Paths.get(uploaderConfig.getBaseDir(), INPUT_FOLDER, filename);
        Path errorPath = Paths.get(uploaderConfig.getBaseDir(), ERROR_FOLDER, getTargetFilename(filename));
        try {
            Files.move(inputPath, errorPath);
        } catch (IOException e) {
            log.error("Error occurred while moving file to error folder", e);
        }
        log.info("File {} moved to error folder", filename);
    }

    private void moveToProcessedFolder(String filename) {
        Path inputPath = Paths.get(uploaderConfig.getBaseDir(), INPUT_FOLDER, filename);
        Path processedPath = Paths.get(uploaderConfig.getBaseDir(), PROCESSED_FOLDER, getTargetFilename(filename));
        try {
            Files.move(inputPath, processedPath);
        } catch (IOException e) {
            log.error("Error occurred while moving file to processed folder", e);
        }
        log.info("File {} moved to processed folder", filename);
    }

    private void createRequiredDirectories() throws IOException {
        Files.createDirectories(Paths.get(uploaderConfig.getBaseDir(), INPUT_FOLDER));
        Files.createDirectories(Paths.get(uploaderConfig.getBaseDir(), PROCESSED_FOLDER));
        Files.createDirectories(Paths.get(uploaderConfig.getBaseDir(), ERROR_FOLDER));
    }

    private String getTargetFilename(String filename) {
        return FilenameUtils.getBaseName(filename)
                .concat("_")
                .concat(String.valueOf(System.currentTimeMillis()))
                .concat(".").concat(FilenameUtils.getExtension(filename));
    }
}
