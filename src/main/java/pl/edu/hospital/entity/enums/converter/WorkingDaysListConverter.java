package pl.edu.hospital.entity.enums.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import pl.edu.hospital.entity.enums.WorkingDay;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class WorkingDaysListConverter implements AttributeConverter<List<WorkingDay>, String> {

    @Override
    public String convertToDatabaseColumn(List<WorkingDay> attribute) {
        return attribute == null ? null :
                attribute.stream().map(Enum::name).collect(Collectors.joining(","));
    }

    @Override
    public List<WorkingDay> convertToEntityAttribute(String dbData) {
        return dbData == null || dbData.isEmpty() ? List.of() :
                Arrays.stream(dbData.split(","))
                        .map(WorkingDay::valueOf)
                        .collect(Collectors.toList());
    }
}