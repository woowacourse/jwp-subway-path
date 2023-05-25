package subway.route.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import subway.route.application.RouteQueryService;
import subway.route.application.dto.ShortestRouteResponse;

@WebMvcTest(RouteController.class)
class RouteControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private RouteQueryService routeQueryService;

  @Test
  @DisplayName("showShortestRoute() : 출발, 도착역이 주어지고, 나이가 주어지지 않으면 추가 요금 없이 최단 경로와 가격을 조회되면 200 OK를 반환한다.")
  void test_showShortestRoute() throws Exception {
    //given
    final String startStationName = "A";
    final String endStationName = "B";

    final List<String> shortestRoute = List.of("A", "K", "Z", "B");
    final double leastCost = 100d;
    final int shortestDistance = 10;

    ShortestRouteResponse shortestRouteResponse = new ShortestRouteResponse(shortestRoute,
        leastCost, shortestDistance);

    when(routeQueryService.searchShortestRoute(any()))
        .thenReturn(shortestRouteResponse);

    //then
    mockMvc.perform(get("/route?startStation={startStation}&endStation={endStation}",
            startStationName, endStationName))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("showShortestRoute() : 나이가 주어지면 나이에 따른 요금이 추가된 최단 경로와 가격을 조회되면 200 OK를 반환한다.")
  void test_showShortestRoute_age() throws Exception {
    //given
    final String startStationName = "A";
    final String endStationName = "B";
    final Integer age = 14;

    final List<String> shortestRoute = List.of("A", "K", "Z", "B");
    final double leastCost = 100d;
    final int shortestDistance = 10;

    ShortestRouteResponse shortestRouteResponse = new ShortestRouteResponse(shortestRoute,
        leastCost, shortestDistance);

    when(routeQueryService.searchShortestRoute(any()))
        .thenReturn(shortestRouteResponse);

    //then
    mockMvc.perform(get("/route?startStation={startStation}&endStation={endStation}&age={age}",
            startStationName, endStationName, age))
        .andExpect(status().isOk());
  }
}
