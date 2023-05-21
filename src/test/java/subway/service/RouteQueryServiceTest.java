package subway.service;

import helper.IntegrationTestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import subway.service.dto.ShortestRouteRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RouteQueryServiceTest extends IntegrationTestHelper {

    @Autowired
    private RouteQueryService routeQueryService;

    @Test
    @DisplayName("searchShortestRoute() : 출발, 도착역을 통해 최소 경로를 조회할 수 있다.")
    void test_searchShortestRoute() throws Exception {
        //given
        final String currentStationName = "A";
        final String nextStationName = "D";

        final ShortestRouteRequest shortestRouteRequest =
                new ShortestRouteRequest(currentStationName, nextStationName, null);

        //when
        final List<String> route = routeQueryService.searchShortestRoute(shortestRouteRequest);

        //then
        assertThat(route).containsAnyElementsOf(List.of("A", "B", "C", "D"));
    }

    @Test
    @DisplayName("searchLeastCost() : 나이와 특정 호선에 따라서 최소 요금 정보를 조회할 수 있다.")
    void test_searchLeastCost() throws Exception {
        //given
        final String currentStationName = "A";
        final String nextStationName = "D";
        final Integer age = 14;

        final ShortestRouteRequest shortestRouteRequest =
                new ShortestRouteRequest(currentStationName, nextStationName, age);

        //when
        final double fare = routeQueryService.searchLeastCost(shortestRouteRequest);

        //then
        //부과 : 1호선 가격 : 500, 기본 요금 : 1250
        //할인 : 청소년 -350, -20%
        assertEquals(1120d, fare);
    }

    @Test
    @DisplayName("searchShortestDistance() : 출발, 도착역 간의 최소 경로 거리를 조회할 수 있다.")
    void test_searchShortestDistance() throws Exception {
        //given
        final String currentStationName = "A";
        final String nextStationName = "D";
        final Integer age = null;

        final ShortestRouteRequest shortestRouteRequest =
                new ShortestRouteRequest(currentStationName, nextStationName, age);

        //when
        final int distance = routeQueryService.searchShortestDistance(shortestRouteRequest);

        //then
        assertEquals(6, distance);
    }
}
