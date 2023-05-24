package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.dto.response.StationResponse;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@Sql("/setUpStationPath.sql")
public class PathIntegrationTest extends IntegrationTest {


    //given
    //   (1)   (2)   (3)
    // 1  -  2  -  3 - 7
    //       |         | (10)
    //       4-5-6 -8 -9
    @DisplayName("최단 경로와 그에따른 요금을 조회한다.")
    @ParameterizedTest
    @MethodSource("paramProvider")
    void addInitialSectionToLine_success(List<Long> queryParam, List<Long> path, Long expectedFare) {
        // when
        ExtractableResponse<Response> findResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("age", 30)
                .when().get("/paths/{startId}/{endId}", queryParam.get(0), queryParam.get(1))
                .then().log().all().
                extract();

        // then
        List<Long> resultStationIds = findResponse.jsonPath()
                .getList("path", StationResponse.class).stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        long fare = findResponse.jsonPath().getLong("money");

        assertThat(findResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(resultStationIds).isEqualTo(path);
        assertThat(fare).isEqualTo(expectedFare);
    }

    private static Stream<Arguments> paramProvider() {
        return Stream.of(
                arguments(Arrays.asList(1L, 3L), Arrays.asList(1L, 2L, 3L), 1350L),
                arguments(Arrays.asList(1L, 9L), Arrays.asList(1L, 2L, 3L, 7L, 9L), 1550L)
        );
    }
}
