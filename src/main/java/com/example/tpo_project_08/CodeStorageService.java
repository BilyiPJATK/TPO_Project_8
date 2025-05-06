package com.example.tpo_project_08;

import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.*;

@Service
public class CodeStorageService {

    private final String storageDir = "saved_codes";
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public CodeStorageService() {
        File dir = new File(storageDir);if (!dir.exists()) {dir.mkdirs();}}

    public void saveCode(SavedCode code) throws IOException {
        String filename = storageDir + "/" + code.getId() + ".ser";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(code);
        }

        scheduleDeletion(filename, code.getExpiresAt());
    }

    public SavedCode loadCode(String id) throws IOException, ClassNotFoundException {
        String filename = storageDir + "/" + id + ".ser";
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (SavedCode) ois.readObject();
        }
    }

    private void scheduleDeletion(String filename, LocalDateTime expiresAt) {
        long delay = Duration.between(LocalDateTime.now(), expiresAt).toMillis();
        if (delay <= 0) {
            deleteFile(filename);
        } else {
            scheduler.schedule(() -> deleteFile(filename), delay, TimeUnit.MILLISECONDS);
        }
    }

    private void deleteFile(String filename) {
        try {
            Files.deleteIfExists(Paths.get(filename));
            System.out.println("Deleted expired file: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
