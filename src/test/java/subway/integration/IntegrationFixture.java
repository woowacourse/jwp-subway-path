package subway.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import subway.dto.LineNodeRequest;

public class IntegrationFixture {

    public static final String LINE_NAME_2 = "2";
    public static final String LINE_NAME_3 = "3";
    public static final LineNodeRequest A_NODE = new LineNodeRequest(null, "a", 4);
    public static final LineNodeRequest B_NODE = new LineNodeRequest("a", "b", 3);
    public static final LineNodeRequest C_NODE = new LineNodeRequest("b", "c", null);

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String jsonSerialize(final Object request) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(request);
    }
}
