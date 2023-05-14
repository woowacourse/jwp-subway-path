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
import subway.dto.request.SectionSaveRequest;
import subway.dto.response.StationResponse;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 등록 관련 기능")
@Sql("/setUpStation.sql")
public class SectionAddIntegrationTest extends IntegrationTest {

    @DisplayName("노선에 역을 최초로 추가한다.")
    @Test
    void addInitialSectionToLine_success() {
        // when
        long lineId = 1L;
        SectionSaveRequest request = new SectionSaveRequest(1L, 2L, 10);

        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{lineId}/sections", lineId)
                .then().log().all().
                extract();

        // then
        ExtractableResponse<Response> findResponse = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}/sections", lineId)
                .then().extract();

        List<Long> resultStationIds = findResponse.jsonPath()
                .getList(".", StationResponse.class).stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(resultStationIds).isEqualTo(List.of(1L,2L));
    }

    @DisplayName("노선에 역을 추가한다.")
    @ParameterizedTest
    @MethodSource("provideRequestAndExpected")
    void addStationToLine_success(SectionSaveRequest request, List<Long> expected) {
        //given
        long lineId = 1L;
        SectionSaveRequest initSaveRequest = new SectionSaveRequest(1L, 2L, 10);
        RestAssured
                .given()
                .body(initSaveRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{lineId}/sections", lineId)
                .then();
        //현재 상태 1-2

        //when
        RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{lineId}/sections", lineId)
                .then().log().all().
                extract();

        // then
        ExtractableResponse<Response> findResponse = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}/sections", lineId)
                .then().extract();

        List<Long> resultStationIds = findResponse.jsonPath()
                .getList(".", StationResponse.class).stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultStationIds).isEqualTo(expected);
    }

    private static Stream<Arguments> provideRequestAndExpected() {
        return Stream.of(
                Arguments.of(new SectionSaveRequest(3L, 1L, 5), List.of(3L, 1L, 2L)),
                Arguments.of(new SectionSaveRequest(2L, 3L, 10), List.of(1L, 2L, 3L)),
                Arguments.of(new SectionSaveRequest(1L, 3L, 4), List.of(1L, 3L, 2L))
        );
    }
}
