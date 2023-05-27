package subway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import subway.controller.dto.response.ShortestPathResponse;
import subway.controller.dto.response.StationResponse;
import subway.exception.StationNotFoundException;
import subway.service.PathService;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PathController.class)
class PathControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PathService pathService;

    @Nested
    @DisplayName("최단 경로 조회 - GET /path?start={start}&end={end}")
    class FindShortestPath {

        @Test
        @DisplayName("성공")
        void success() throws Exception {
            // given
            final Long startStationId = 1L;
            final Long endStationId = 8L;

            final ShortestPathResponse response = new ShortestPathResponse(
                    20, 1450,
                    List.of(
                            new StationResponse(1L, "잠실"), new StationResponse(2L, "잠실새내"),
                            new StationResponse(3L, "종합운동장"), new StationResponse(4L, "삼성"),
                            new StationResponse(5L, "선릉"), new StationResponse(6L, "역삼"),
                            new StationResponse(7L, "강남"), new StationResponse(8L, "양재")
                    )
            );

            given(pathService.findShortestPath(startStationId, endStationId)).willReturn(response);

            // when, then
            final String responseBody = "{\n" +
                    "    \"distance\": 20,\n" +
                    "    \"fee\": 1450,\n" +
                    "    \"path\": [\n" +
                    "        {\n" +
                    "            \"id\": 1,\n" +
                    "            \"name\": \"잠실\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"id\": 2,\n" +
                    "            \"name\": \"잠실새내\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"id\": 3,\n" +
                    "            \"name\": \"종합운동장\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"id\": 4,\n" +
                    "            \"name\": \"삼성\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"id\": 5,\n" +
                    "            \"name\": \"선릉\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"id\": 6,\n" +
                    "            \"name\": \"역삼\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"id\": 7,\n" +
                    "            \"name\": \"강남\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"id\": 8,\n" +
                    "            \"name\": \"양재\"\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}";
            mockMvc.perform(get("/path").param("start", String.valueOf(startStationId)).param("end", String.valueOf(endStationId)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(responseBody));
        }

        @Test
        @DisplayName("실패 - 잘못된 id")
        void fail_invalid_id() throws Exception {
            // given
            final Long startStationId = 1L;
            final Long endStationId = 88L;

            given(pathService.findShortestPath(startStationId, endStationId)).willThrow(StationNotFoundException.class);

            // when, then
            mockMvc.perform(get("/path").param("start", String.valueOf(startStationId)).param("end", String.valueOf(endStationId)))
                    .andExpect(status().isNotFound());
        }
    }
}
