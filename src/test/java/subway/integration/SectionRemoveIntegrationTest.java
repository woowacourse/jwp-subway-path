package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.dto.response.StationResponse;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 제거 관련 기능")
@Sql("/setUpStationAndSection.sql")
public class SectionRemoveIntegrationTest extends IntegrationTest {
    public static final long LINE_ID = 1;

    @DisplayName("노선에 역을 제거한다.")
    @ParameterizedTest
    @MethodSource("provideRemoveStationAndExpected")
    void removeStationInLine_success(Long removeId, List<Long> expected) {
        //when
        RestAssured
                .given().log().all()
                .body(removeId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/{lineId}/stations/{stationId}", LINE_ID, removeId)
                .then().log().all().
                extract();

        // then
        ExtractableResponse<Response> findResponse = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}/stations", LINE_ID)
                .then().extract();

        List<Long> resultStationIds = findResponse.jsonPath()
                .getList(".", StationResponse.class).stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultStationIds).isEqualTo(expected);
    }

    private static Stream<Arguments> provideRemoveStationAndExpected() {
        return Stream.of(
                Arguments.of(1L, List.of(2L, 3L)),
                Arguments.of(3L, List.of(1L, 2L)),
                Arguments.of(2L, List.of(1L, 3L))
        );
    }

    @DisplayName("노선에 존재하지 않는 역을 제거하면 400에러를 발생시킨다.")
    @Test
    void removeStation_not_exist_fail() {
        //when
        //현재상태: 1-2
        int notExistingId = 99;

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/{lineId}/stations/{stationId}", LINE_ID, notExistingId)
                .then().log().all().
                extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
