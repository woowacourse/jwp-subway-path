package subway.ui;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static subway.TestFixture.SHORTEST_PATH_IN_LINE_A_AND_B_STATION_A_TO_E;
import static subway.TestFixture.STATION_A;
import static subway.TestFixture.STATION_E;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import subway.application.SubwayService;
import subway.domain.Path;
import subway.dto.PathResponse;

@WebMvcTest(SubwayController.class)
class SubwayControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SubwayService subwayService;

    @DisplayName("최단경로를 찾는다")
    @Test
    void getShortestPath() throws Exception {
        var path = new Path(SHORTEST_PATH_IN_LINE_A_AND_B_STATION_A_TO_E);
        PathResponse pathResponse = PathResponse.of(path);
        doReturn(pathResponse).when(subwayService).getShortestPath(anyLong(), anyLong());

        String pathResponseAsString = objectMapper.writeValueAsString(pathResponse);

        mockMvc.perform(MockMvcRequestBuilders.get(
                        "/subway/shortest-path?from={from}&to={to}",
                        STATION_A.getId(),
                        STATION_E.getId())
                )
                .andExpect(status().isOk())
                .andExpect(content().json(pathResponseAsString));
    }

    @DisplayName("쿼리스트링이 없어 최단 경로 탐색에 실패한다")
    @Test
    void getShortestPath_without_queryString_fails() throws Exception {
        var path = new Path(SHORTEST_PATH_IN_LINE_A_AND_B_STATION_A_TO_E);
        PathResponse pathResponse = PathResponse.of(path);
        doReturn(pathResponse).when(subwayService).getShortestPath(anyLong(), anyLong());

        mockMvc.perform(MockMvcRequestBuilders.get("/subway/shortest-path"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("'from'")));
    }
}
