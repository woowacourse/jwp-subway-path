package subway.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static subway.fixture.StationFixture.강남역;
import static subway.fixture.StationFixture.서울역;
import static subway.fixture.StationFixture.선릉역;

import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import subway.application.SubwayService;
import subway.controller.dto.RouteSearchResponse;
import subway.controller.dto.StationResponse;

@WebMvcTest(controllers = SubwayController.class)
class SubwayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubwayService subwayService;

    @Test
    @DisplayName("출발역과 도착역을 요청으로 받아 경로를 반환한다.")
    void findShortestRoute() throws Exception {
        RouteSearchResponse routeResponse = createRouteResponse();
        given(subwayService.findRoute(any(), any())).willReturn(routeResponse);

        mockMvc.perform(get("/subway/shortest-route")
                        .queryParam("startStation", "강남역")
                        .queryParam("endStation", "서울역")
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("출발역이 빈 값으로 오는 경우 BAD REQUSET를 반환한다.")
    void findShortestRouteFailWithEmptyStartStation() throws Exception {
        RouteSearchResponse routeResponse = createRouteResponse();
        given(subwayService.findRoute(any(), any())).willReturn(routeResponse);

        mockMvc.perform(get("/subway/shortest-route")
                        .queryParam("startStation", "")
                        .queryParam("endStation", "서울역")
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("출발역은 반드시 입력해야 합니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("도착역이 빈 값으로 오는 경우 BAD REQUSET를 반환한다.")
    void findShortestRouteFailWithEmptyEndStation() throws Exception {
        RouteSearchResponse routeResponse = createRouteResponse();
        given(subwayService.findRoute(any(), any())).willReturn(routeResponse);

        mockMvc.perform(get("/subway/shortest-route")
                        .queryParam("startStation", "서울역")
                        .queryParam("endStation", "")
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("도착역은 반드시 입력해야 합니다."))
                .andDo(print());
    }

    private RouteSearchResponse createRouteResponse() {
        StationResponse stationResponse1 = StationResponse.of(강남역);
        StationResponse stationResponse2 = StationResponse.of(선릉역);
        StationResponse stationResponse3 = StationResponse.of(서울역);
        return new RouteSearchResponse(
                List.of(stationResponse1, stationResponse2, stationResponse3),
                20,
                1500);
    }
}
