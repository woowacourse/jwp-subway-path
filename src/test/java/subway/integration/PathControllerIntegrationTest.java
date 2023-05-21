package subway.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Sql("/path_data_initialize.sql")
class PathControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("경로를 조회하면 최단 경로와 요금이 조회되어야 한다.")
    void findPath_success() throws Exception {
        // given
        String originStationName = "A역";
        String destinationStationName = "I역";

        // expect
        mockMvc.perform(get("/path?originStation={originStationName}&destinationStation={destinationStationName}",
                        originStationName, destinationStationName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("A역에서 I역까지의 경로가 조회되었습니다."))
                .andExpect(jsonPath("$.result.stations[0]").value("A역"))
                .andExpect(jsonPath("$.result.stations[1]").value("D역"))
                .andExpect(jsonPath("$.result.stations[2]").value("F역"))
                .andExpect(jsonPath("$.result.stations[3]").value("G역"))
                .andExpect(jsonPath("$.result.stations[4]").value("I역"))
                .andExpect(jsonPath("$.result.totalDistance").value(11))
                .andExpect(jsonPath("$.result.price").value(1350));
    }

    @ParameterizedTest(name = "{0}하면 경로 조회에 실패해야 한다.")
    @MethodSource("illegalPathRequest")
    void findPath_IllegalPathRequest(String displayName, String originStationName,
                                     String destinationStationName, String expectMessage) throws Exception {
        // expect
        mockMvc.perform(get("/path?originStation={originStationName}&destinationStation={destinationStationName}",
                        originStationName, destinationStationName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(expectMessage));
    }

    static Stream<Arguments> illegalPathRequest() {
        return Stream.of(
                Arguments.of("없는 역을 조회", "A역", "없는역", "해당 역이 구간에 존재하지 않습니다."),
                Arguments.of("같은 역을 조회", "A역", "A역", "조회할 역이 서로 같습니다."),
                Arguments.of("구간에 없는 역을 조회", "A역", "없는역", "해당 역이 구간에 존재하지 않습니다."),
                Arguments.of("갈 수 없는 경로를 조회", "A역", "Z역", "해당 경로를 찾을 수 없습니다.")
        );
    }
}
