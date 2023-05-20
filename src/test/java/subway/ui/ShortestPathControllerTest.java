package subway.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ResponseBodyExtractionOptions;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import subway.application.ShortestPathService;
import subway.dto.LineStationResponse;
import subway.dto.ShortestPathResponse;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ShortestPathControllerTest {

    @LocalServerPort
    private int port;

    @MockBean
    private ShortestPathService shortestPathService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void findShortestPath() {
        //given
        final LineStationResponse firstStation = new LineStationResponse(1L, "출발역", 1L, "1호선", "red");
        final LineStationResponse secondStation = new LineStationResponse(1L, "출발역", 1L, "1호선", "red");
        final ShortestPathResponse shortestPathResponse = new ShortestPathResponse(List.of(firstStation, secondStation),
            10, 2300);

        Mockito.when(shortestPathService.getPath(any(), any(), anyInt())).thenReturn(shortestPathResponse);

        //when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
            .when()
            .get("/shortest-path?start=1&end=1&age=10")
            .then().log().all()
            .extract();

        //then
        final ResponseBodyExtractionOptions body = response.body();
        final ShortestPathResponse responseBody = body.as(ShortestPathResponse.class);

        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(responseBody.getRoute()).usingRecursiveComparison()
                .isEqualTo(shortestPathResponse.getRoute()),
            () -> assertThat(responseBody.getTotalDistance()).isEqualTo(shortestPathResponse.getTotalDistance()),
            () -> assertThat(responseBody.getTotalFare()).isEqualTo(shortestPathResponse.getTotalFare())
        );
    }
}
