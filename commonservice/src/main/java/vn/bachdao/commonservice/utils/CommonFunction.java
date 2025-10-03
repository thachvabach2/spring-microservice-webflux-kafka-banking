package vn.bachdao.commonservice.utils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.ValidationMessage;
import com.networknt.schema.SpecVersion.VersionFlag;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import vn.bachdao.commonservice.errors.ValidateException;

@Slf4j
@NoArgsConstructor
public class CommonFunction {

    @SneakyThrows
    public static void jsonValidate(InputStream inputStream, String json) {
        JsonSchema schema = JsonSchemaFactory.getInstance(VersionFlag.V7).getSchema(inputStream);
        ObjectMapper om = new ObjectMapper();
        JsonNode jsonNode = om.readTree(json);
        Set<ValidationMessage> errors = schema.validate(jsonNode);
        log.info(errors.toString());
        Map<String, String> stringSetMap = new HashMap<>();
        for (ValidationMessage error : errors) {
            String fieldError = error.getInstanceLocation().toString();

            if (stringSetMap.containsKey(formatStringValidate(fieldError))) {
                String message = stringSetMap.get(formatStringValidate(fieldError));

                stringSetMap.put(formatStringValidate(fieldError),
                        message + ", " + formatStringValidate(error.getMessage()));
            } else {
                stringSetMap.put(formatStringValidate(fieldError),
                        formatStringValidate(error.getMessage()));
            }
        }
        if (!errors.isEmpty()) {
            throw new ValidateException("RQ01", stringSetMap, HttpStatus.BAD_REQUEST);
        }
    }

    public static String formatStringValidate(String message) {
        return message.replaceAll("\\$.", "");
    }
}
