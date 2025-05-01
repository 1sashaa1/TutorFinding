package com.jtspringproject.JtSpringProject.services;

import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class BackupService {

    public boolean backupDatabase(String username, String password, String databaseName, String backupDir) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String backupFile = backupDir + "/backup_" + timestamp + ".sql";

        String command = String.format("mysqldump -u%s -p%s %s -r %s", username, password, databaseName, backupFile);

        try {
            Process process = Runtime.getRuntime().exec(command);

            int processComplete = process.waitFor();

            return processComplete == 0;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}
