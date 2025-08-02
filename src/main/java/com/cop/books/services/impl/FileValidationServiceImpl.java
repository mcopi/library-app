package com.cop.books.services.impl;

import com.cop.books.configs.FieldsPropsConfig;
import com.cop.books.dtos.FieldsDto;
import com.cop.books.services.FileValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@SuppressWarnings({"java:S2259", "java:S112"})
public class FileValidationServiceImpl implements FileValidationService {
    private static final String TODAYS_FORMAT = "yyyyMMdd";
    private FieldsPropsConfig props = new FieldsPropsConfig();
    Logger log = LoggerFactory.getLogger(FileValidationServiceImpl.class);

    public FileValidationServiceImpl(FieldsPropsConfig props) {
        this.props = props;
    }

    @Override
    public void fileNameCheck(String fileName) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat(TODAYS_FORMAT);
        String date = format.format(new Date());
        String books = "books";

        if (!StringUtils.hasLength(fileName)) {
            throw new Exception("File name is null");
        } else {
            if (!fileName.contains(books) && !fileName.contains(date)) {
                throw new Exception("File name is not valid");
            }
        }
    }

    @Override
    public void fileFormatCheck(String fileName) throws Exception {
        String format = Objects.requireNonNull(fileName).substring(fileName.lastIndexOf("."));
        log.info("Format {}: {}", fileName, format);
        if (!format.equalsIgnoreCase(".csv")) {
            throw new Exception("File format is not valid");
        }
    }

    @Override
    public void fileColumnCheck(MultipartFile file) throws Exception {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            int lineNumber = 0;
            String line;
            List<FieldsDto> propsFields = props
                    .getFields()
                    .stream()
                    .sorted(Comparator.comparing(FieldsDto::getNum))
                    .toList();

            while ((line = br.readLine()) != null && lineNumber == 0) {
                String[] columnsName = line.split(",");
                log.info("Columns: {}", (Object) columnsName);

                // Matching csv fields with properties fields
                for (int i= 0; i < columnsName.length; i++) {
                    if (!propsFields.get(i).getName().equals(columnsName[i])) {
                        log.error("CSV Field {} doesn't match properties {}", columnsName[i], propsFields.get(i).getName());
                        throw new Exception("Column tidak sesuai");
                    }
                }

                lineNumber ++;
            }
        } catch (IOException e) {
            log.error("Error while reading file: {}", e.getMessage());
        }
    }
}
