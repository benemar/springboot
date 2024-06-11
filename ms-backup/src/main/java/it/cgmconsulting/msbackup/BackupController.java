package it.cgmconsulting.msbackup;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.exceptions.CsvValidationException;
import it.cgmconsulting.msbackup.model.Authority;
import it.cgmconsulting.msbackup.service.BackupCsvService;
import it.cgmconsulting.msbackup.service.BackupRestoreCsv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
public class BackupController {

    @Autowired
    BackupCsvService backupCsvService;

    @Autowired
    BackupRestoreCsv backupRestoreCsv;



    @GetMapping("/csv")
    //@Scheduled(cron = "0 0 0 L * *") // last day of the month at midnight
    public void writeCsv() throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        backupCsvService.commentCsv();
        backupCsvService.postCsv();
        backupCsvService.ratingCsv();
        backupCsvService.userCsv();
        backupCsvService.authorityCsv();
    }

    @PostMapping("/authority-restore")
    public ResponseEntity<String> restoreData(@RequestParam(required = false) MultipartFile file) {
        try {
            if(file != null)
                backupRestoreCsv.restoreAuthority(file);
            else
                backupRestoreCsv.restoreAuthority();
            return ResponseEntity.ok().body("Authority Restore successfully completed");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error during Authority restore: " + e.getMessage());
        }
    }



}
