package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.ErrorResponse;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationsResponse;

@DisplayName("지하철 노선 관련 기능")
public class LineIntegrationTest extends IntegrationTest {
    private LineRequest lineRequest1;
    private LineRequest lineRequest2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        lineRequest1 = new LineRequest("신분당선", "강남역", "역삼역", 10);
        lineRequest2 = new LineRequest("구신분당선", "강남역", "구역삼역", 10);
    }

    @Nested
    @DisplayName("지하철 노선 생성")
    class create extends IntegrationTest {

        @DisplayName("정상 생성한다")
        @Test
        void createLine() {
            // when
            ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(lineRequest1)
                    .when().post("/lines")
                    .then().log().all().
                    extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            assertThat(response.header("Location")).isNotBlank();
        }

        @DisplayName("기존에 존재하는 이름으로 생성하면 400에러를 반환한다")
        @Test
        void createLineWithDuplicateName() {
            // given
            RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(lineRequest1)
                    .when().post("/lines")
                    .then().log().all().
                    extract();

            // when
            ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(lineRequest1)
                    .when().post("/lines")
                    .then().log().all().
                    extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            ErrorResponse errorResponse = response.body().as(ErrorResponse.class);
            assertThat(errorResponse.getMessage()).isEqualTo("이미 존재하는 이름입니다 입력값 : " + lineRequest1.getLineName());
        }

    }

    @DisplayName("지하철 노선 조회")
    @Nested
    class find extends IntegrationTest {

        @DisplayName("목록 전체를 조회한다.")
        @Test
        void getLines() {
            lineRequest1 = new LineRequest("2호선", "강남역", "역삼역", 5);
            lineRequest2 = new LineRequest("1호선", "서울역", "명동역", 7);

            // given
            ExtractableResponse<Response> createResponse1 = RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(lineRequest1)
                    .when().post("/lines")
                    .then().log().all().
                    statusCode(HttpStatus.CREATED.value()).
                    extract();

            ExtractableResponse<Response> createResponse2 = RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(lineRequest2)
                    .when().post("/lines")
                    .then().log().all().
                    statusCode(HttpStatus.CREATED.value()).
                    extract();

            // when
            ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .when().get("/lines")
                    .then().log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            List<Long> expectedLineIds = Stream.of(createResponse1, createResponse2)
                    .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                    .collect(Collectors.toList());
            List<Long> resultLineIds = response.jsonPath().getList(".", LineResponse.class).stream()
                    .map(LineResponse::getId)
                    .collect(Collectors.toList());
            assertThat(resultLineIds).containsAll(expectedLineIds);
        }

        @DisplayName("하나를 조회한다.")
        @Test
        void getLine() {
            lineRequest1 = new LineRequest("2호선", "강남역", "역삼역", 5);

            // given
            ExtractableResponse<Response> createResponse = RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(lineRequest1)
                    .when().post("/lines")
                    .then().log().all().
                    extract();

            // when
            Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
            ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .when().get("/lines/{lineId}", lineId)
                    .then().log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            StationsResponse resultResponse = response.as(StationsResponse.class);
        }
    }

    @DisplayName("지하철 노선 수정")
    @Nested
    class update extends IntegrationTest {

        @DisplayName("하나를 수정한다.")
        @Test
        void updateLine() {
            // given
            ExtractableResponse<Response> createResponse = RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(lineRequest1)
                    .when().post("/lines")
                    .then().log().all().
                    extract();

            // when
            Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
            ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(lineRequest2)
                    .when().put("/lines/{lineId}", lineId)
                    .then().log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        }
    }

    @DisplayName("지하철 노선 삭제")
    @Nested
    class delete extends IntegrationTest {

        @DisplayName("하나를 제거한다.")
        @Test
        void deleteLine() {
            // given
            ExtractableResponse<Response> createResponse = RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(lineRequest1)
                    .when().post("/lines")
                    .then().log().all().
                    extract();

            // when
            Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
            ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .when().delete("/lines/{lineId}", lineId)
                    .then().log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        }
    }
}
