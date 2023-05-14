package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.dto.request.SectionSaveRequest;
import subway.dto.response.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("/setUpStation.sql")
public class SectionAcceptanceTest extends IntegrationTest {
    @DisplayName("최초 구간 등록-하행 종점 등록 - 순서 조회")
    @Test
    void find_sections_Line() {
        //최초 구간 등록
        long lineId = 1L;
        SectionSaveRequest initSaveRequest = new SectionSaveRequest(3L, 1L, 10);
        RestAssured
                .given()
                .body(initSaveRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{lineId}/sections", lineId)
                .then();

        // 하행 종점 등록
        SectionSaveRequest saveRequest2 = new SectionSaveRequest(1L, 2L, 10);
        RestAssured
                .given().log().all()
                .body(saveRequest2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{lineId}/sections", lineId)
                .then().log().all().
                extract();

        //순서 조회 3-1-2
        ExtractableResponse<Response> findResponse = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}/sections", lineId)
                .then().extract();

        List<Long> resultStationIds = findResponse.jsonPath()
                .getList(".", StationResponse.class).stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(findResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(resultStationIds).isEqualTo(List.of(3L, 1L, 2L));
    }
}
