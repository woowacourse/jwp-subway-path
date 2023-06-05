package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.request.SectionRequest;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("구간 관련 기능")
class SectionIntegrationTest extends IntegrationTest {

    @DisplayName("구간을 추가할 수 있다")
    @Test
    void createSection() {
        // given
        Long lineId = 1L;
        SectionRequest sectionRequest = new SectionRequest(3L, 7L, 8);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                                                            .body(sectionRequest)
                                                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                            .when().post("sections/" + lineId)
                                                            .then().log().all()
                                                            .extract();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isEqualTo("/lines/1")
        );
    }

    @DisplayName("잘못된 구간을 추가하면 예외가 반환된다")
    @Test
    void createSectionException() {
        // given
        Long lineId = 1L;
        SectionRequest sectionRequest = new SectionRequest(3L, 5L, 8);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                                                            .body(sectionRequest)
                                                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                            .when().post("sections/" + lineId)
                                                            .then().log().all()
                                                            .extract();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.body().jsonPath().get("messages").toString())
                        .contains("현재 구간보다 큰 구간은 입력할 수 없습니다.")
        );
    }

    @DisplayName("특정 역을 삭제해 구간을 삭제할 수 있다")
    @Test
    void deleteSection() {
        // given
        Long lineId = 1L;
        Long stationId = 1L;

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                                                            .body(stationId)
                                                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                            .when().delete("sections/" + lineId)
                                                            .then().log().all()
                                                            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("잚소된 역을 삭제하면 예외가 반환된다")
    @Test
    void deleteSectionException() {
        // given
        Long lineId = 1L;
        Long stationId = 1000L;

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                                                            .body(stationId)
                                                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                            .when().delete("sections/" + lineId)
                                                            .then().log().all()
                                                            .extract();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.body().jsonPath().get("messages").toString())
                        .contains("해당 역이 존재하지 않습니다.")
        );
    }
}
