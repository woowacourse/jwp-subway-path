package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.LineRequest;
import subway.dto.SectionRequest;
import subway.dto.StationRequest;

@DisplayName("구간 관련 기능")
public class SectionIntegrationTest extends IntegrationTest {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        final LineRequest lineRequest = new LineRequest("1호선", "bg-red-600");
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .when().post("/lines")
                .then().extract();

        final StationRequest stationRequest1 = new StationRequest("반월당역");
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationRequest1)
                .when().post("/stations")
                .then().extract();

        final StationRequest stationRequest2 = new StationRequest("동대구역");
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationRequest2)
                .when().post("/stations")
                .then().extract();
    }

    private ExtractableResponse<Response> postSection(final SectionRequest sectionRequest) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/sections")
                .then().extract();
    }

    @DisplayName("구간을 생성한다.")
    @Test
    void createSection() {
        // given
        final SectionRequest sectionRequest = new SectionRequest(1L, 1L, 2L, 3);

        // when
        ExtractableResponse<Response> response = postSection(sectionRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("구간을 제거한다.")
    @Test
    void deleteSection() {
        // given
        final SectionRequest sectionRequest = new SectionRequest(1L, 1L, 2L, 3);
        postSection(sectionRequest);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/sections?lineId=" + 1 + "&stationId=" + 1)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("전체 구간을 조회한다.")
    @Test
    void getSections() {
        // given
        final SectionRequest sectionRequest = new SectionRequest(1L, 1L, 2L, 3);
        postSection(sectionRequest);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/sections?lineId=" + 1)
                .then().log().all()
                .extract();

        // then
        final List<Long> upStationId = response.jsonPath().getList("upStation.id", Long.class);
        final List<Long> downStationId = response.jsonPath().getList("downStation.id", Long.class);
        final List<Integer> distance = response.jsonPath().getList("distance", Integer.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(upStationId).containsExactly(1L);
        assertThat(downStationId).containsExactly(2L);
        assertThat(distance).containsExactly(3);
    }
}
