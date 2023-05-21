package subway.integration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.LineRequest;
import subway.dto.SectionCreateRequest;
import subway.dto.SectionDeleteRequest;
import subway.dto.StationCreateRequest;
import subway.integration.step.LineStep;
import subway.integration.step.SectionStep;
import subway.integration.step.StationStep;

@DisplayName("지하철 구간 관련 기능")
@SuppressWarnings("NonAsciiCharacters")
public class SectionIntegrationTest extends IntegrationTest {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @DisplayName("지하철 구간을 생성한다.")
    @Test
    void createStation() {
        // given
        Long lineId = LineStep.노선을_생성한다(new LineRequest("2호선"));
        StationStep.역을_생성한다(new StationCreateRequest("잠실역"));
        StationStep.역을_생성한다(new StationCreateRequest("잠실새내역"));
        SectionCreateRequest sectionCreateRequest = new SectionCreateRequest("잠실역", "잠실새내역", 10, 1);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(sectionCreateRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/sections")
                .then().log().all()
                .extract();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isEqualTo("/lines/" + lineId)
        );
    }

    @DisplayName("지하철 구간을 생성할 때 존재하지 않는 역이면 오류를 생성한다.")
    @Test
    void createStationWhenNoExistStation() {
        // given
        LineStep.노선을_생성한다(new LineRequest("2호선"));
        StationStep.역을_생성한다(new StationCreateRequest("잠실역"));
        StationStep.역을_생성한다(new StationCreateRequest("잠실새내역"));
        SectionCreateRequest sectionCreateRequest = new SectionCreateRequest("잠실역", "봉천역", 10, 1);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(sectionCreateRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/sections")
                .then().log().all()
                .extract();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.body().jsonPath().get("message").equals("존재하지 않는 역입니다.")).isTrue()
        );
    }

    @DisplayName("역이 두 개 존재하는 노선의 지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        LineStep.노선을_생성한다(new LineRequest("2호선"));
        StationStep.역을_생성한다(new StationCreateRequest("잠실역"));
        StationStep.역을_생성한다(new StationCreateRequest("잠실새내역"));
        SectionStep.구간을_생성한다(new SectionCreateRequest("잠실역", "잠실새내역", 10, 1));

        SectionDeleteRequest sectionDeleteRequest = new SectionDeleteRequest("잠실역", 1L);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
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
        LineStep.노선을_생성한다(new LineRequest("2호선"));
        StationStep.역을_생성한다(new StationCreateRequest("잠실역"));
        StationStep.역을_생성한다(new StationCreateRequest("잠실새내역"));
        SectionStep.구간을_생성한다(new SectionCreateRequest("잠실역", "잠실새내역", 10, 1));
        SectionDeleteRequest sectionDeleteRequest = new SectionDeleteRequest("봉천역", 1L);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(sectionDeleteRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/sections")
                .then().log().all()
                .extract();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.body().jsonPath().get("message").equals("존재하지 않는 역입니다.")).isTrue()
        );
    }
}
