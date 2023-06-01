package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.LineRequest;
import subway.dto.PathRequest;
import subway.dto.PathResponse;
import subway.dto.SectionCreateRequest;
import subway.dto.StationCreateRequest;
import subway.integration.step.LineStep;
import subway.integration.step.SectionStep;
import subway.integration.step.StationStep;

@DisplayName("최단 경로 구하기 기능")
@SuppressWarnings("NonAsciiCharacters")
public class PathIntegrationTest extends IntegrationTest {
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
}
