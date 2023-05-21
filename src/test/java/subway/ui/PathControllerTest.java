package subway.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import subway.application.PathService;
import subway.dto.PathRequest;
import subway.dto.PathResponse;

import static fixtures.PathFixtures.REQUEST_PATH_강변역_TO_성수역;
import static fixtures.PathFixtures.RESPONSE_PATH_강변역_TO_성수역;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PathController.class)
class PathControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PathService pathService;

    @Test
    @DisplayName("GET /path uri로 요청해서 최단 거리 경로를 조회한다.")
    void findShortestPathTest() throws Exception {
        // given
        PathRequest request = REQUEST_PATH_강변역_TO_성수역;
        PathResponse response = RESPONSE_PATH_강변역_TO_성수역;
        when(pathService.findShortestPath(request)).thenReturn(response);

        // when, then
        mockMvc.perform(get("/path")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stationNames").value(is(RESPONSE_PATH_강변역_TO_성수역.getStationNames())))
                .andExpect(jsonPath("$.distance").value(is(RESPONSE_PATH_강변역_TO_성수역.getDistance())))
                .andExpect(jsonPath("$.fare").value(is(RESPONSE_PATH_강변역_TO_성수역.getFare())));
    }
}