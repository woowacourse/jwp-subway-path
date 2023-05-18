package subway.ui;

import io.restassured.RestAssured;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;
import java.util.stream.Stream;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RouteControllerTest {
    @LocalServerPort
    private int port;

    @BeforeEach
    void setPort(){
        RestAssured.port = port;
    }

    @Test
    @DisplayName("최단 경로를 찾는 테스트")
    void findOptimalRoute(){
        List<String> optimalStations = RestAssured.given()
                .when().get("/route/departure-id=1&arrival-id=5")
                .then().log().all()
                .extract()
                .jsonPath()
                .getList("sections");
        Assertions.assertThat(optimalStations).containsExactly("신림","봉천","서울대입구","낙성대","사당");
    }
    @ParameterizedTest(name = "{0}km이면 {3}원이다")
    @MethodSource("distanceProvider")
    void getFeeTest(final String name, final long departureId, final long arrivalId, final int expected){
        final String url = "/route/departure-id="+departureId+"&arrival-id="+arrivalId;
        int actual = RestAssured.given()
                .when().get(url)
                .then().log().all()
                .extract()
                .jsonPath()
                .getInt("fee");
        Assertions.assertThat(actual).isEqualTo(expected);
    }
    private static Stream<Arguments> distanceProvider(){
        return Stream.of(
                Arguments.of("9",1l,8l,1250),
                Arguments.of("10",1l,9l,1250),
                Arguments.of("11",1l,10l,1350),
                Arguments.of("16",1l,11l,1450),
                Arguments.of("58",1l,12l,2150)
        );
    }
}
