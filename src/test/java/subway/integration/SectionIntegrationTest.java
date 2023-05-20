package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.LineRequest;
import subway.dto.SectionCreateRequest;
import subway.exception.ExceptionResponse;

@DisplayName("노선 구간 관련 기능")
public class SectionIntegrationTest extends IntegrationTest {

    @DisplayName("노선이 비어 있을 때 역을 등록한다.")
    @Test
    void createSection_emptyLine_success() {
        //given
        final Long lineId = createLine();
        final SectionCreateRequest sectionCreateRequest = new SectionCreateRequest("첫째역", "둘째역", "하행", 10);

        //when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
            .body(sectionCreateRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines/" + lineId + "/sections")
            .then().log().all()
            .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private Long createLine() {
        final LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 0);

        final ExtractableResponse<Response> extract = RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(lineRequest)
            .when().post("/lines")
            .then().log().all()
            .extract();

        final String location = extract.header("Location");
        return Long.parseLong(location.split("/", -1)[2]);
    }

    @DisplayName("노선의 역과 역 사이에 역을 등록한다.")
    @Test
    void createSection_betweenStations_success() {
        //given
        final Long lineId = createLine();
        addOneSection(lineId);

        final SectionCreateRequest sectionCreateRequest = new SectionCreateRequest("둘째역", "셋째역", "상행", 5);

        //when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
            .body(sectionCreateRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines/" + lineId + "/sections")
            .then().log().all()
            .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private void addOneSection(final Long lineId) {
        final SectionCreateRequest request = new SectionCreateRequest("첫째역", "둘째역", "하행", 10);

        RestAssured.given().log().all()
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines/" + lineId + "/sections")
            .then().log().all()
            .extract();
    }

    @DisplayName("노선이 역과 역 사이에 기존 구간보다 짧지 않은 구간을 생성한다.")
    @Test
    void createSection_betweenStations_notEnoughDistance() {
        //given
        final Long lineId = createLine();
        addOneSection(lineId);

        final SectionCreateRequest sectionCreateRequest = new SectionCreateRequest("첫째역", "셋째역", "하행", 10);

        //when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
            .body(sectionCreateRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines/" + lineId + "/sections")
            .then().log().all()
            .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().as(ExceptionResponse.class).getMessage()).isEqualTo("구간의 거리는 0보다 커야 합니다.");
    }

    @DisplayName("노선의 상행종점역의 상행 방향으로 역을 등록한다.")
    @Test
    void createSection_addOnTop() {
        //given
        final Long lineId = createLine();
        addOneSection(lineId);

        final SectionCreateRequest sectionCreateRequest = new SectionCreateRequest("첫째역", "셋째역", "상행", 5);

        //when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
            .body(sectionCreateRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines/" + lineId + "/sections")
            .then().log().all()
            .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("노선의 하행종점역의 하행 방향으로 역을 등록한다.")
    @Test
    void createSection_addOnBottom() {
        //given
        final Long lineId = createLine();
        addOneSection(lineId);

        final SectionCreateRequest sectionCreateRequest = new SectionCreateRequest("둘째역", "셋째역", "하행", 5);

        //when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
            .body(sectionCreateRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines/" + lineId + "/sections")
            .then().log().all()
            .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("노선에 존재하지 않는 역을 기준으로 역을 등록한다.")
    @Test
    void createSection_baseStationNotExist() {
        //given
        final Long lineId = createLine();
        addOneSection(lineId);

        final SectionCreateRequest sectionCreateRequest = new SectionCreateRequest("없는역", "셋째역", "상행", 5);

        //when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
            .body(sectionCreateRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines/" + lineId + "/sections")
            .then().log().all()
            .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().as(ExceptionResponse.class).getMessage()).isEqualTo("기준이 되는 역이 노선에 존재하지 않습니다.");
    }

    @DisplayName("노선에서 상행종점역을 삭제시 상행종점역을 포함한 구간이 같이 삭제된다.")
    @Test
    void deleteSection_success_startOfLine() {
        //given
        final Long lineId = createLine();
        addOneSection(lineId);

        //when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
            .when()
            .delete("/lines/" + lineId + "/sections?stationName=첫째역")
            .then().log().all()
            .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("노선에서 하행종점역을 삭제시 하행종점역을 포함한 구간이 같이 삭제된다.")
    @Test
    void deleteSection_success_endOfLine() {
        //given
        final Long lineId = createLine();
        addOneSection(lineId);

        //when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
            .when()
            .delete("/lines/" + lineId + "/sections?stationName=둘째역")
            .then().log().all()
            .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("노선 중간에 있는 역을 삭제시 해당 역을 포함한 구간이 같이 삭제된다.")
    @Test
    void deleteSection_success() {
        //given
        final Long lineId = createLine();
        addTwoSection(lineId);

        //when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
            .when()
            .delete("/lines/" + lineId + "/sections?stationName=둘째역")
            .then().log().all()
            .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void addTwoSection(final Long lineId) {
        final SectionCreateRequest firstRequest = new SectionCreateRequest("첫째역", "둘째역", "하행", 10);

        RestAssured.given().log().all()
            .body(firstRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines/" + lineId + "/sections")
            .then().log().all()
            .extract();

        final SectionCreateRequest secondRequest = new SectionCreateRequest("둘째역", "셋째역", "하행", 10);

        RestAssured.given().log().all()
            .body(secondRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines/" + lineId + "/sections")
            .then().log().all()
            .extract();
    }

    @DisplayName("노선에서 존재하지 않는 구간을 삭제한다.")
    @Test
    void deleteSection() {
        //given
        final Long lineId = createLine();
        addOneSection(lineId);

        //when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
            .when()
            .delete("/lines/" + lineId + "/sections?stationName=없는역")
            .then().log().all()
            .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().as(ExceptionResponse.class).getMessage()).isEqualTo("노선에 역이 존재하지 않습니다. ( 없는역 )");
    }
}
