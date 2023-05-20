package subway.integration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.LineRequest;
import subway.dto.SectionCreateRequest;
import subway.dto.SectionDeleteRequest;
import subway.dto.StationCreateRequest;

@DisplayName("지하철 구간 관련 기능")
public class SectionIntegrationTest extends IntegrationTest {
    private SectionCreateRequest sectionCreateRequest;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        // 노선을 생성한다.
        LineRequest lineRequest = new LineRequest("2호선");
        RestAssured.given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        // 역을 생성한다.
        StationCreateRequest createRequest1 = new StationCreateRequest("잠실역");
        StationCreateRequest createRequest2 = new StationCreateRequest("잠실새내역");

        RestAssured.given().log().all()
                .body(createRequest1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        RestAssured.given().log().all()
                .body(createRequest2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        sectionCreateRequest = new SectionCreateRequest("잠실역", "잠실새내역", 10, 1);
    }

    @DisplayName("지하철 구간을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(sectionCreateRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/sections")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("지하철 구간을 생성할 때 존재하지 않는 역이면 오류를 생성한다.")
    @Test
    void createStationWhenNoExistStation() {
        // given
        sectionCreateRequest = new SectionCreateRequest("잠실역", "봉천역", 10, 1);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(sectionCreateRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/sections")
                .then().log().all()
                .extract();

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.body().jsonPath().get("message").equals("존재하지 않는 역입니다.")).isTrue()
        );
    }

    @DisplayName("역이 두 개 존재하는 노선의 지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        SectionDeleteRequest sectionDeleteRequest = new SectionDeleteRequest("잠실역", 1L);

        RestAssured.given().log().all()
                .body(sectionCreateRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/sections")
                .then().log().all()
                .extract();

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(sectionDeleteRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/sections")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("지하철 구간을 삭제할 때 존재하지 않는 역이면 오류를 생성한다.")
    @Test
    void deleteStationWhenNoExistStation() {
        // given
        SectionDeleteRequest sectionDeleteRequest = new SectionDeleteRequest("봉천역", 1L);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(sectionDeleteRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/sections")
                .then().log().all()
                .extract();

        // then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.body().jsonPath().get("message").equals("존재하지 않는 역입니다.")).isTrue()
        );
    }
}
