package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.SectionCreateRequest;

@DisplayName("노선 구간 관련 기능")
public class SectionIntegrationTest extends IntegrationTest {

    @DisplayName("노선이 비어 있을 때 역을 등록한다.")
    @Test
    void createSection_emptyLine_success() {
        //given
        final SectionCreateRequest sectionCreateRequest = new SectionCreateRequest("기준역", "추가역", "상행", 10);

        //when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
            .body(sectionCreateRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines/1/sections")
            .then().log().all()
            .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("노선의 역과 역 사이에 역을 등록한다.")
    @Test
    void createSection_betweenStations_success() {
        //given
        final SectionCreateRequest firstSectionCreateRequest = new SectionCreateRequest("초기상행역", "초기하행역", "하행", 10);
        final SectionCreateRequest secondSectionCreateRequest = new SectionCreateRequest("초기상행역", "추가역", "하행", 5);

        //when
        final ExtractableResponse<Response> firstResponse = RestAssured.given().log().all()
            .body(firstSectionCreateRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines/1/sections")
            .then().log().all()
            .extract();

        final ExtractableResponse<Response> secondResponse = RestAssured.given().log().all()
            .body(secondSectionCreateRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines/1/sections")
            .then().log().all()
            .extract();

        //then
        assertThat(secondResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(secondResponse.header("Location")).isNotBlank();
    }

    @DisplayName("노선이 역과 역 사이에 기존 구간보다 더 긴 구간을 생성한다.")
    @Test
    void createSection_betweenStations_notEnoughDistance() {
        //given
        final SectionCreateRequest firstSectionCreateRequest = new SectionCreateRequest("초기상행역", "초기하행역", "하행", 5);
        final SectionCreateRequest secondSectionCreateRequest = new SectionCreateRequest("초기상행역", "추가역", "하행", 5);

        //when
        final ExtractableResponse<Response> firstResponse = RestAssured.given().log().all()
            .body(firstSectionCreateRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines/1/sections")
            .then().log().all()
            .extract();

        final ExtractableResponse<Response> secondResponse = RestAssured.given().log().all()
            .body(secondSectionCreateRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines/1/sections")
            .then().log().all()
            .extract();

        //then
        assertThat(secondResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("노선의 상행종점역의 상행 방향으로 역을 등록한다.")
    @Test
    void createSection_addOnTop() {
        //given
        final SectionCreateRequest firstSectionCreateRequest = new SectionCreateRequest("초기상행역", "초기하행역", "하행", 10);
        final SectionCreateRequest secondSectionCreateRequest = new SectionCreateRequest("초기상행역", "추가역", "상행", 5);

        //when
        final ExtractableResponse<Response> firstResponse = RestAssured.given().log().all()
            .body(firstSectionCreateRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines/1/sections")
            .then().log().all()
            .extract();

        final ExtractableResponse<Response> secondResponse = RestAssured.given().log().all()
            .body(secondSectionCreateRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines/1/sections")
            .then().log().all()
            .extract();

        //then
        assertThat(secondResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(secondResponse.header("Location")).isNotBlank();
    }

    @DisplayName("노선의 하행종점역의 하행 방향으로 역을 등록한다.")
    @Test
    void createSection_addOnBottom() {
        //given
        final SectionCreateRequest firstSectionCreateRequest = new SectionCreateRequest("초기상행역", "초기하행역", "하행", 10);
        final SectionCreateRequest secondSectionCreateRequest = new SectionCreateRequest("초기하행역", "추가역", "하행", 5);

        //when
        final ExtractableResponse<Response> firstResponse = RestAssured.given().log().all()
            .body(firstSectionCreateRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines/1/sections")
            .then().log().all()
            .extract();

        final ExtractableResponse<Response> secondResponse = RestAssured.given().log().all()
            .body(secondSectionCreateRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines/1/sections")
            .then().log().all()
            .extract();

        //then
        assertThat(secondResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(secondResponse.header("Location")).isNotBlank();
    }

    @DisplayName("노선에 존재하지 않는 역을 기준으로 역을 등록한다.")
    @Test
    void createSection_baseStationNotExist() {
        //given
        final SectionCreateRequest firstSectionCreateRequest = new SectionCreateRequest("초기상행역", "초기하행역", "하행", 10);
        final SectionCreateRequest secondSectionCreateRequest = new SectionCreateRequest("존재하지않는역", "추가역", "하행", 5);

        //when
        final ExtractableResponse<Response> firstResponse = RestAssured.given().log().all()
            .body(firstSectionCreateRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines/1/sections")
            .then().log().all()
            .extract();

        final ExtractableResponse<Response> secondResponse = RestAssured.given().log().all()
            .body(secondSectionCreateRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines/1/sections")
            .then().log().all()
            .extract();

        //then
        assertThat(secondResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("노선에서 역이 포함된 구간을 삭제한다.")
    @Test
    void deleteSection_success() {
        //given
        final SectionCreateRequest firstSectionCreateRequest = new SectionCreateRequest("초기상행역", "초기하행역", "하행", 10);

        final ExtractableResponse<Response> firstResponse = RestAssured.given().log().all()
            .body(firstSectionCreateRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines/1/sections")
            .then().log().all()
            .extract();

        //when
        final ExtractableResponse<Response> secondResponse = RestAssured.given().log().all()
            .when()
            .delete("/lines/1/section?stationId=1")
            .then().log().all()
            .extract();

        //then
        assertThat(secondResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("노선에서 존재하지 않는 구간을 삭제한다.")
    @Test
    void deleteSection() {
        //given
        //when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
            .when()
            .delete("/lines/1/section?stationId=-3")
            .then().log().all()
            .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
