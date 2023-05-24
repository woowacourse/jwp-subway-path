package subway.ui;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import subway.dto.LineSaveDto;
import subway.dto.RouteDto;
import subway.dto.SectionSaveDto;
import subway.integration.IntegrationTest;

@DisplayName("요금 관련 기능")
class RouteControllerTest extends IntegrationTest {

    @DisplayName("요금 목록을 조회한다.")
    @Test
    void getLines() {
        LineSaveDto lineSaveDto = new LineSaveDto("1호선");

        // 라인 생성
        ExtractableResponse<Response> lineCreateResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineSaveDto)
                .when().post("/subway/lines")
                .then().log().all().
                extract();

        Long lineId = Long.parseLong(lineCreateResponse.header("Location").split("/")[3]);

        SectionSaveDto sectionSaveDto = new SectionSaveDto("일역", "이역", 3);
        // 구간 등록
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionSaveDto)
                .when().post("/sections/{lineId}", lineId)
                .then().log().all().
                extract();

        ExtractableResponse<Response> selectRoute = RestAssured
                .given()
                .param("startStationName", "일역")
                .param("endStationName", "이역")
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineSaveDto)
                .when().get("/routes")
                .then().log().all().
                extract();

        assertThat(selectRoute.statusCode()).isEqualTo(200);
        RouteDto response = selectRoute.body().as(RouteDto.class);
        assertThat(response.getStations()).hasSize(2);
        assertThat(response.getStations()).containsExactly("일역", "이역");
        assertThat(response.getDistance().getDistance()).isEqualTo(3);
        assertThat(response.getFee().getFee()).isEqualTo(1250);
    }

}