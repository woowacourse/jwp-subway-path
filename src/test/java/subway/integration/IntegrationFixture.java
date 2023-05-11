package subway.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class IntegrationFixture {
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String jsonSerialize(final Object request) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(request);
    }
}
