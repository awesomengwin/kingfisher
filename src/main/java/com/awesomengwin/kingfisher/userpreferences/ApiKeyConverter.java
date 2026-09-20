package com.awesomengwin.kingfisher.userpreferences;

import com.awesomengwin.kingfisher.common.EncryptionService;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;

@Converter
@Component
public class ApiKeyConverter implements AttributeConverter<String, String> {

    private final EncryptionService encryptionService;

    public ApiKeyConverter(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return attribute == null ? null : encryptionService.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData == null ? null : encryptionService.decrypt(dbData);
    }
}
