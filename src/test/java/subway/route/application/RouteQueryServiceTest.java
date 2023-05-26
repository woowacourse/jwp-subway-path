package subway.route.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import helper.IntegrationTestHelper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import subway.route.application.dto.ShortestRouteRequest;
import subway.route.application.dto.ShortestRouteResponse;

class RouteQueryServiceTest extends IntegrationTestHelper {

  @Autowired
  private RouteQueryService routeQueryService;

  @Test
  @DisplayName("searchShortestRoute() : 출발, 도착역을 통해 최소 경로, 요금, 거리를 구할 수 있다.")
  void test_searchShortestRoute() throws Exception {
    //given
    final String currentStationName = "A";
    final String nextStationName = "D";

    final ShortestRouteRequest shortestRouteRequest =
        new ShortestRouteRequest(currentStationName, nextStationName, 14);

    //when
    ShortestRouteResponse shortestRouteResponse = routeQueryService.searchShortestRoute(
        shortestRouteRequest);

    //then
    assertAll(
        () -> assertThat(shortestRouteResponse.getStations()).containsAnyElementsOf(
            List.of("A", "B", "C", "D")),
        () -> assertEquals(1120d, shortestRouteResponse.getCost()),
        () -> assertEquals(6, shortestRouteResponse.getDistance())
    );
  }
}
