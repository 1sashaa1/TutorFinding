package com.jtspringproject.JtSpringProject.controller;

import com.jtspringproject.JtSpringProject.services.BackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class BackupController {

    @Autowired
    private BackupService backupService;

    @PostMapping("/backup")
    public String backup() {
        boolean success = backupService.backupDatabase("root", "1209", "jdbc:mysql://localhost:3306/tutorfind?createDatabaseIfNotExist=true", "D:/backup");

        return success ? "Ошибка при создании резервной копии." : "Резервная копия создана успешно.";
    }
}
