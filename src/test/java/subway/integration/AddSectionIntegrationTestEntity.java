package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.LineRequest;
import subway.dto.SectionRequest;
import subway.dto.SectionResponse;
import subway.dto.StationRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AddSectionIntegrationTestEntity extends IntegrationTest {

    @DisplayName("라인테이블에 라인이 존재하지 않는 경우 예외 처리")
    @Test
    void createSectionWithNoLine() {
        // when
        final SectionRequest sectionRequest = new SectionRequest(1, 2, 1, "DOWN", 3);
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/sections")
                .then().log().all().
                extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).isEqualTo("존재하지 않는 라인입니다.");
    }

    @DisplayName("새로운 역이 존재하지 않는 경우 예외처리")
    @Test
    void createSectionWithNoStation() {
        // when
        final LineRequest lineRequest = new LineRequest("2호선", "green");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .when().post("/lines")
                .then().log().all();

        final StationRequest stationRequest = new StationRequest("강남역");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationRequest)
                .when().post("/stations")
                .then().log().all();

        final SectionRequest sectionRequest = new SectionRequest(1, 2, 1, "DOWN", 3);
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/sections")
                .then().log().all().
                extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).isEqualTo("2는 존재하지 않는 역 아이디입니다.");
    }

    @DisplayName("라인에 어떤 구역도 존재하지 않는 경우 등록한다.")
    @Test
    void createFirstSectionInLine() {
        // when
        final LineRequest lineRequest = new LineRequest("2호선", "green");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .when().post("/lines")
                .then().log().all();

        final StationRequest stationRequest = new StationRequest("강남역");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationRequest)
                .when().post("/stations")
                .then().log().all();

        final StationRequest stationRequest2 = new StationRequest("잠실역");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationRequest2)
                .when().post("/stations")
                .then().log().all();

        final SectionRequest sectionRequest = new SectionRequest(1, 2, 1, "DOWN", 3);
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/sections")
                .then().log().all().
                extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        final List<SectionResponse> result = response.jsonPath().getList(".", SectionResponse.class);
        assertThat(result.get(0).getDistance()).isEqualTo(3);
        assertThat(result.size()).isEqualTo(1);
    }

    @Nested
    public class InitializedTest extends IntegrationTest {

        @BeforeEach
        void setUpSection() {
            final LineRequest lineRequest = new LineRequest("2호선", "green");
            RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(lineRequest)
                    .when().post("/lines")
                    .then().log().all();

            final StationRequest stationRequest = new StationRequest("강남역");
            RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(stationRequest)
                    .when().post("/stations")
                    .then().log().all();

            final StationRequest stationRequest2 = new StationRequest("잠실역");
            RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(stationRequest2)
                    .when().post("/stations")
                    .then().log().all();

            final SectionRequest sectionRequest = new SectionRequest(1, 2, 1, "DOWN", 3);
            RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(sectionRequest)
                    .when().post("/sections")
                    .then().log().all();
        }

        @DisplayName("구역 테이블에 해당 라인의 기준역이 존재하지 않는 경우 예외처리")
        @Test
        void createSectionWithNoBaseStationInLine() {
            // when
            final StationRequest stationRequest3 = new StationRequest("성수역");
            RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(stationRequest3)
                    .when().post("/stations")
                    .then().log().all();

            final StationRequest stationRequest4 = new StationRequest("신림역");
            RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(stationRequest4)
                    .when().post("/stations")
                    .then().log().all();

            final SectionRequest sectionRequest2 = new SectionRequest(1, 4, 3, "DOWN", 3);
            final ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(sectionRequest2)
                    .when().post("/sections")
                    .then().log().all().
                    extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(response.body().asString()).isEqualTo("기준역이 라인에 존재하지 않습니다.");
        }

        @DisplayName("새로운역이 상행 종점일 경우 생성한다.")
        @Test
        void createSectionWithNewStationAndUpEndPoint() {
            // when
            final StationRequest stationRequest3 = new StationRequest("성수역");
            RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(stationRequest3)
                    .when().post("/stations")
                    .then().log().all();

            final SectionRequest sectionRequest2 = new SectionRequest(1, 3, 1, "UP", 3);
            final ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(sectionRequest2)
                    .when().post("/sections")
                    .then().log().all().
                    extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            final List<SectionResponse> result = response.jsonPath().getList(".", SectionResponse.class);
            assertThat(result.get(0).getDistance()).isEqualTo(3);
            assertThat(result.size()).isEqualTo(2);
        }

        @DisplayName("새로운역이 하행 종점일 경우 생성한다.")
        @Test
        void createSectionWithNewStationAndDownEndPoint() {
            // when
            final StationRequest stationRequest3 = new StationRequest("성수역");
            RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(stationRequest3)
                    .when().post("/stations")
                    .then().log().all();

            final SectionRequest sectionRequest2 = new SectionRequest(1, 3, 2, "DOWN", 3);
            final ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(sectionRequest2)
                    .when().post("/sections")
                    .then().log().all().
                    extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            final List<SectionResponse> result = response.jsonPath().getList(".", SectionResponse.class);
            assertThat(result.get(0).getDistance()).isEqualTo(3);
            assertThat(result.size()).isEqualTo(2);
        }

        @DisplayName("갈래길 방지 - 기존 구간의 거리를 초과할 경우 예외처리")
        @Test
        void createSectionWithExceedDistance() {
            // when
            final StationRequest stationRequest3 = new StationRequest("성수역");
            RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(stationRequest3)
                    .when().post("/stations")
                    .then().log().all();

            final SectionRequest sectionRequest2 = new SectionRequest(1, 3, 1, "DOWN", 4);
            final ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(sectionRequest2)
                    .when().post("/sections")
                    .then().log().all().
                    extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(response.body().asString()).isEqualTo("거리가 기존 구역 거리를 초과했습니다.");
        }

        @DisplayName("갈래길 방지 - 기존 구간의 거리보다 작은 경우 기존 구역 제거 하고 새로운 구역 2개 생성")
        @Test
        void createSectionWithLessDistance() {
            // when
            final StationRequest stationRequest3 = new StationRequest("성수역");
            RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(stationRequest3)
                    .when().post("/stations")
                    .then().log().all();

            final SectionRequest sectionRequest2 = new SectionRequest(1, 3, 1, "DOWN", 2);
            final ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(sectionRequest2)
                    .when().post("/sections")
                    .then().log().all().
                    extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            final List<SectionResponse> result = response.jsonPath().getList(".", SectionResponse.class);
            assertThat(result).hasSize(2);
            assertThat(result.get(0).getDistance()).isEqualTo(2);
            assertThat(result.get(1).getDistance()).isEqualTo(1);
        }

        @DisplayName("새로운 역이 구역에 존재하는 경우 예외처리")
        @Test
        void createSectionWithNewStationButInSection() {
            // when

            final SectionRequest sectionRequest2 = new SectionRequest(1, 2, 1, "DOWN", 3);
            final ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(sectionRequest2)
                    .when().post("/sections")
                    .then().log().all().
                    extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(response.body().asString()).isEqualTo("새로운역이 라인에 이미 존재합니다.");
        }

    }

}
