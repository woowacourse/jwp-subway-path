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
import subway.dto.request.LineRequest;
import subway.dto.request.PathRequest;
import subway.dto.request.SectionCreateRequest;
import subway.dto.request.StationCreateRequest;
import subway.dto.response.PathResponse;
import subway.integration.step.LineStep;
import subway.integration.step.SectionStep;
import subway.integration.step.StationStep;

@SuppressWarnings("NonAsciiCharacters")
public class ShortestPathIntegrationTest extends IntegrationTest {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        LineStep.노선을_생성한다(new LineRequest("1호선"));
        LineStep.노선을_생성한다(new LineRequest("2호선"));
        LineStep.노선을_생성한다(new LineRequest("3호선"));

        StationStep.역을_생성한다(new StationCreateRequest("찰리역"));
        StationStep.역을_생성한다(new StationCreateRequest("토리역"));
        StationStep.역을_생성한다(new StationCreateRequest("망고역"));

        SectionStep.구간을_생성한다(new SectionCreateRequest("찰리역", "토리역", 100, 1));
        SectionStep.구간을_생성한다(new SectionCreateRequest("토리역", "망고역", 1, 2));
        SectionStep.구간을_생성한다(new SectionCreateRequest("망고역", "찰리역", 1, 3));
    }

    @DisplayName("최단 경로를 조회한다.")
    @Test
    void shortestPath() {
        // given
        PathRequest pathRequest = new PathRequest("찰리역", "토리역");

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(pathRequest)
                .when()
                .get("/paths/shortest")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(PathResponse.class).getDistance()).isEqualTo(2);
        assertThat(response.as(PathResponse.class).getFare()).isEqualTo(1250);
    }

    @DisplayName("출발역과 도착역이 같을 때 예외가 발생한다.")
    @Test
    void shortestPathWithSameStation() {
        // given
        PathRequest pathRequest = new PathRequest("찰리역", "찰리역");

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(pathRequest)
                .when()
                .get("/paths/shortest")
                .then().log().all()
                .extract();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.body().jsonPath().get("message").equals("시작역과 도착역은 같을 수 없습니다.")).isTrue()
        );
    }

    @DisplayName("존재하지 않는 역이 입력되면 예외가 발생한다.")
    @Test
    void shortestPathWithNotExistStation() {
        // given
        PathRequest pathRequest = new PathRequest("베로역", "찰리역");

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(pathRequest)
                .when()
                .get("/paths/shortest")
                .then().log().all()
                .extract();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.body().jsonPath().get("message").equals("베로역은 존재하지 않는 역입니다.")).isTrue()
        );
    }

    @DisplayName("경로를 찾을 수 없을 때 예외가 발생한다.")
    @Test
    void shortestPathWithNotFoundPath() {
        // given
        LineStep.노선을_생성한다(new LineRequest("4호선"));

        StationStep.역을_생성한다(new StationCreateRequest("잠실역"));
        StationStep.역을_생성한다(new StationCreateRequest("잠실새내역"));

        SectionStep.구간을_생성한다(new SectionCreateRequest("잠실역", "잠실새내역", 10, 4));

        PathRequest pathRequest = new PathRequest("잠실역", "찰리역");

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(pathRequest)
                .when()
                .get("/paths/shortest")
                .then().log().all()
                .extract();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.body().jsonPath().get("message").equals("존재하지 않는 경로입니다.")).isTrue()
        );
    }
}
