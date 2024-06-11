package it.cgmconsulting.msbackup.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvValidationException;
import it.cgmconsulting.msbackup.model.Authority;
import it.cgmconsulting.msbackup.repository.AuthorityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class BackupRestoreCsv {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Value("${backup.path}")
    String backupPath;

    public void saveDataFromCsv(List<Authority> authorities) {
        authorityRepository.deleteAll();
        authorityRepository.saveAll(authorities);
    }

    public List<Authority> parseAuthorityCsv(Reader reader){
        List<Authority> authorities = new ArrayList<>();
        CsvToBean<Authority> csvToBean = new CsvToBeanBuilder(reader)
                .withType(Authority.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        Iterator<Authority> authorityIterator = csvToBean.iterator();
        while (authorityIterator.hasNext()) {
            Authority authority = authorityIterator.next();
            authorities.add(new Authority(authority.getId(), authority.getAuthorityName()));
        }
        try {
            reader.close();
        } catch (IOException e) {
            log.error("Error closing authority csv: "+e.getMessage());
        }
        return authorities;
    }


    public void restoreAuthority(MultipartFile file) throws IOException, CsvValidationException {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(backupPath + file.getOriginalFilename()));
            List<Authority> authorities = parseAuthorityCsv(reader);
            saveDataFromCsv(authorities);
            log.info("AUTHORITY BACKUP WAS WRITTEN ON DATABASE FROM REQUEST");
        } catch (Exception e) {
            log.error("SOMETHING WENT WRONG RESTORING AUTHORITY FROM REQUEST: "+e.getMessage());
        }
    }

    @Scheduled(fixedRate = 60000)
    public void restoreAuthority() throws IOException, CsvValidationException {
        try {
            List<String> foundFilesList = listFilesUsingFilesList(backupPath,"authority");
            if(foundFilesList.size() == 1) {
                Reader reader = Files.newBufferedReader(Paths.get(backupPath + foundFilesList.get(0)));
                List<Authority> authorities = parseAuthorityCsv(reader);
                saveDataFromCsv(authorities);
                log.info("AUTHORITY BACKUP WAS WRITTEN ON DATABASE FROM SCHEDULED SERVICE");
            } else {
                log.warn("AUTHORITY BACKUP NOT FOUND");
            }
        } catch (Exception e) {
            log.error("SOMETHING WENT WRONG RESTORING AUTHORITY FROM SCHEDULED SERVICE: "+e.getMessage());
        }
    }



    public List<String> listFilesUsingFilesList(String dir, String entityName) throws IOException {
        try (Stream<Path> stream = Files.list(Paths.get(dir))) {
            return stream
                    .filter(
                            file -> !Files.isDirectory(file) &&
                                    file.getFileName().toString().contains(entityName) &&
                                    file.getFileName().toString().contains(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                    )
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toList());
        }
    }

}
