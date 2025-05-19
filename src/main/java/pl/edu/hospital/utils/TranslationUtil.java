package pl.edu.hospital.utils;


import org.springframework.stereotype.Service;
import pl.edu.hospital.entity.enums.Language;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

@Service
public class TranslationUtil {
    public static Map<Language, Map<String, String>> translateLabels() {
        Map<Language, Map<String, String>> result = new HashMap<>();

        for (Language l : Language.values()) {
            Map<String, String> translations = new LinkedHashMap<>();
            String path = "/languages/" + l.name().toLowerCase() + ".properties";

            try (InputStream input = TranslationUtil.class.getResourceAsStream(path);
                 InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {

                Properties props = new Properties();
                props.load(reader);

                for (String key : props.stringPropertyNames()) {
                    translations.put(key, props.getProperty(key));
                }

                result.put(l, translations);

            } catch (IOException e) {
                throw new RuntimeException("Error loading translations for language: " + l, e);
            }
        }

        return result;
    }
}
