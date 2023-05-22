package subway.integration;

import io.restassured.RestAssured;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import subway.dto.RouteRequest;
import subway.dto.RouteResponse;
import subway.dto.StationResponse;

public class RouteIntegrationTest extends IntegrationTest {
    
    @Test
    @DisplayName("최단 경로를 조회한다 - 잠실나루역 -> 왕십리역")
    void findRouteBetween() {
        final RouteRequest routeRequest = new RouteRequest(8, 1);
        final RouteResponse routeResponse = RestAssured.given().log().all()
                .body(routeRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/route")
                .then().log().all()
                .extract()
                .as(RouteResponse.class);
        final List<StationResponse> stations = routeResponse.getStations();
        Assertions.assertThat(stations.size()).isEqualTo(8);
        Assertions.assertThat(stations.get(0).getName()).isEqualTo("잠실나루역");
        Assertions.assertThat(stations.get(1).getName()).isEqualTo("강변역");
        Assertions.assertThat(stations.get(2).getName()).isEqualTo("구의역");
        Assertions.assertThat(stations.get(3).getName()).isEqualTo("건대입구역");
        Assertions.assertThat(stations.get(4).getName()).isEqualTo("성수역");
        Assertions.assertThat(stations.get(5).getName()).isEqualTo("뚝섬역");
        Assertions.assertThat(stations.get(6).getName()).isEqualTo("한양대역");
        Assertions.assertThat(stations.get(7).getName()).isEqualTo("왕십리역");
        
        Assertions.assertThat(routeResponse.getDistance()).isEqualTo(35);
        Assertions.assertThat(routeResponse.getFare()).isEqualTo(1750);
    }
    
    @Test
    @DisplayName("최단 경로를 조회한다 - 건대입구 -> 천호")
    void findRouteBetween2() {
        final RouteRequest routeRequest = new RouteRequest(5, 26);
        final RouteResponse routeResponse = RestAssured.given().log().all()
                .body(routeRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/route")
                .then().log().all()
                .extract()
                .as(RouteResponse.class);
        final List<StationResponse> stations = routeResponse.getStations();
        Assertions.assertThat(stations.size()).isEqualTo(8);
        Assertions.assertThat(stations.get(0).getName()).isEqualTo("건대입구역");
        Assertions.assertThat(stations.get(1).getName()).isEqualTo("구의역");
        Assertions.assertThat(stations.get(2).getName()).isEqualTo("강변역");
        Assertions.assertThat(stations.get(3).getName()).isEqualTo("잠실나루역");
        Assertions.assertThat(stations.get(4).getName()).isEqualTo("잠실역");
        Assertions.assertThat(stations.get(5).getName()).isEqualTo("몽촌토성역");
        Assertions.assertThat(stations.get(6).getName()).isEqualTo("강동구청역");
        Assertions.assertThat(stations.get(7).getName()).isEqualTo("천호역");
        
        Assertions.assertThat(routeResponse.getDistance()).isEqualTo(29);
        Assertions.assertThat(routeResponse.getFare()).isEqualTo(1650);
    }
    
    @Test
    @DisplayName("경로 조회 요청 검증 - 역 아이디가 1보다 작은 경우")
    void findRouteBetweenWithInvalidLineId() {
        final RouteRequest routeRequest = new RouteRequest(0, 1);
        RestAssured.given().log().all()
                .body(routeRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/route")
                .then().log().all()
                .statusCode(400);
    }
}
