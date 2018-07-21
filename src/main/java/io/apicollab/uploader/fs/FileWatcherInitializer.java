package io.apicollab.uploader.fs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
@Slf4j
class FileWatcherInitializer {

    @Autowired
    private FileWatcher fileWatcher;

    @PostConstruct
    void initialize() throws InterruptedException, IOException {
        log.info("Initializing file system watcher...");
        fileWatcher.initialize();
    }
}
