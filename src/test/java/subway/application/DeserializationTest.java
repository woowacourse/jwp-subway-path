package subway.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import subway.line.domain.section.domain.Distance;
import subway.line.domain.section.dto.SectionSavingRequest;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class DeserializationTest {
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("StationsSavingRequest 역직렬화 테스트")
    void stationsSavingRequest() throws JsonProcessingException {
        String json = "{\n" +
                "    \"previousStationName\": \"개룡역\",\n" +
                "    \"nextStationName\": \"거여역\",\n" +
                "    \"distance\": 5,\n" +
                "    \"down\": false\n" +
                "}";
        SectionSavingRequest sectionSavingRequest = objectMapper.readValue(json, SectionSavingRequest.class);
        assertThat(sectionSavingRequest.getDistance()).isEqualTo(Distance.of(5));
    }
}
