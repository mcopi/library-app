package com.cop.books.configs;

import com.cop.books.dtos.FieldsDto;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "books")
public class FieldsPropsConfig {
    List<FieldsDto> fields;

    public List<FieldsDto> getFields() {
        return fields;
    }

    public void setFields(List<FieldsDto> fields) {
        this.fields = fields;
    }
}
