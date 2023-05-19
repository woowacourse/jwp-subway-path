package subway.integration;

import io.restassured.RestAssured;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.stream.Stream;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {
    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    @Sql(scripts = {"classpath:truncate.sql", "classpath:data/lineTest.sql", "classpath:data/sectionTest.sql", "classpath:data/stationTest.sql"})
    static
    class RouteIntegrationTest {
        @org.springframework.boot.test.web.server.LocalServerPort
        private int port;

        @BeforeEach
        void setPort(){
            RestAssured.port = port;
        }

        @Test
        @DisplayName("최단 경로를 찾는다")
        void findOptimalRoute(){
            List<String> optimalStations = RestAssured.given()
                    .when().get("/route?departure-id=1&arrival-id=5")
                    .then().log().all()
                    .extract()
                    .jsonPath()
                    .getList("route");
            Assertions.assertThat(optimalStations).containsExactly("신림","봉천","서울대입구","낙성대","사당");
        }
        @ParameterizedTest(name = "{0}km면 {3}원이다")
        @MethodSource("distanceProvider")
        void getFeeTest(final String name, final String departureId, final String arrivalId, final int expected){
            final String url = "/route?departure-id="+departureId+"&arrival-id="+arrivalId;
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
                    Arguments.of("9","1","8",1250),
                    Arguments.of("10", "1","9",1250),
                    Arguments.of("11","1","10",1350),
                    Arguments.of("16","1","11",1450),
                    Arguments.of("58","1","12",2150)
            );
        }
    }
}
