package subway.route.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = {"/schema.sql", "/data.sql"})
public class RouteIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Nested
    class 나이_할인_미적용 {

        @Test
        @DisplayName("최단 거리 경로를 요청하면 구간 정보와 총 거리, 요금을 계산할 수 있다.")
        void routeTest() {
            final ExtractableResponse<Response> response = 경로를_요청한다(1, 3, 20);

            final RouteResponse routeResponse = response.as(RouteResponse.class);
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(routeResponse.getTotalDistance()).isEqualTo(74);
            assertThat(routeResponse.getFare()).isEqualTo(2350 + 500);
        }

        @Test
        @DisplayName("역이 존재하지 않으면 NOT_FOUND를 반환한다")
        void routeTestFail1() {
            final ExtractableResponse<Response> response = 경로를_요청한다(1, 0, 20);

            assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        }

        @Test
        @DisplayName("역이 존재하지 않으면 NOT_FOUND를 반환한다")
        void routeTestFail2() {
            final ExtractableResponse<Response> response = 경로를_요청한다(0, 3, 20);

            assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        }

        @Test
        @DisplayName("역 id가 long이 아니면 BAD_REQUEST를 반환한다")
        void routeTestFail3() {
            final ExtractableResponse<Response> response = 경로를_요청한다("a", "b", 20);

            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("출발역과 도착역이 연결되어 있지 않으면 NOT_FOUND를 반환한다")
        void routeTestFail4() {
            final ExtractableResponse<Response> response = 경로를_요청한다(1, 5, 20);

            assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        }

        @Test
        @DisplayName("출발역과 도착역이 같으면 BAD_REQUEST를 반환한다")
        void routeTestFail5() {
            final ExtractableResponse<Response> response = 경로를_요청한다(1, 1, 20);

            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    class 나이_할인_적용 {

        @ParameterizedTest(name = "6~12세면 성인 요금의 350원을 공제하고 반값이 할인된다")
        @CsvSource({"6,1250", "12,1250"})
        void routeAgeTest1(int age, int fare) {
            final ExtractableResponse<Response> response = 경로를_요청한다(1, 3, age);

            final RouteResponse routeResponse = response.as(RouteResponse.class);
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(routeResponse.getTotalDistance()).isEqualTo(74);
            assertThat(routeResponse.getFare()).isEqualTo(fare);
        }

        @ParameterizedTest(name = "13~19세면 성인 요금의 350원을 공제하고 반값이 할인된다")
        @CsvSource({"13,2000", "19,2000"})
        void routeAgeTest2(int age, int fare) {
            final ExtractableResponse<Response> response = 경로를_요청한다(1, 3, age);

            final RouteResponse routeResponse = response.as(RouteResponse.class);
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(routeResponse.getTotalDistance()).isEqualTo(74);
            assertThat(routeResponse.getFare()).isEqualTo(fare);
        }

        @ParameterizedTest(name = "0~5 공짜로 이용할 수 있다")
        @CsvSource({"0,0", "5,0"})
        void routeAgeTest3(int age, int fare) {
            final ExtractableResponse<Response> response = 경로를_요청한다(1, 3, age);

            final RouteResponse routeResponse = response.as(RouteResponse.class);
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(routeResponse.getTotalDistance()).isEqualTo(74);
            assertThat(routeResponse.getFare()).isEqualTo(fare);
        }

        @Test
        @DisplayName("나이가 음수이면 NOT_FOUND를 반환한다")
        void routeTestFail1() {
            final ExtractableResponse<Response> response = 경로를_요청한다(1, 3, -1);

            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }
    }

    private ExtractableResponse<Response> 경로를_요청한다(Object sourceId, Object destinationId, Object age) {
        return given().log()
                      .all()
                      .when()
                      .get("/routes?source=" + sourceId + "&destination=" + destinationId + "&age=" + age)
                      .then()
                      .log()
                      .all()
                      .extract();
    }
}
