package subway.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import subway.domain.Distance;
import subway.domain.Fare;
import subway.domain.Path;
import subway.domain.Station;
import subway.dto.PathSearchResponse;
import subway.service.PathService;

@WebMvcTest(PathController.class)
class PathControllerTest {

    @Autowired
    protected MockMvc mockMvc;
    @MockBean
    private PathService pathService;

    @DisplayName("경로를 조회한다.")
    @Test
    void findPath() throws Exception {
        given(pathService.getShortestPath(any())).willReturn(new PathSearchResponse(
                new Path(List.of(
                        new Station(1L, "선릉역"), new Station(2L, "잠실역")),
                        new Distance(10)),
                new Fare(1250)
        ));

        mockMvc.perform(get("/paths/1/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.path.stations", hasSize(2)))
                .andExpect(jsonPath("$.path.stations[0].id", is(1)))
                .andExpect(jsonPath("$.path.stations[0].name", is("선릉역")))
                .andExpect(jsonPath("$.path.stations[1].id", is(2)))
                .andExpect(jsonPath("$.path.stations[1].name", is("잠실역")))
                .andExpect(jsonPath("$.path.distance.value", is(10)))
                .andExpect(jsonPath("$.fare.value", is(1250)));
    }
}
