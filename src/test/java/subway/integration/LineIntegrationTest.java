package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static subway.TestFixture.JAMSILNARU_ID;
import static subway.TestFixture.JAMSILSAENAE_ID;
import static subway.TestFixture.JAMSIL_ID;
import static subway.TestFixture.JEONGJA_ID;
import static subway.TestFixture.PANKYO_ID;
import static subway.TestFixture.종합운동장역_ID;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.SectionRequest;

@DisplayName("지하철 노선 관련 기능")
class LineIntegrationTest extends IntegrationTest {
    private LineRequest lineRequest1;
    private LineRequest lineRequest2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        lineRequest1 = new LineRequest("신분당선", "bg-red-600");
        lineRequest2 = new LineRequest("구신분당선", "bg-red-600");
    }

    @DisplayName("지하철 노선을 생성한다.")
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

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
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
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        ExtractableResponse<Response> createResponse1 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();
        Long lineId1 = Long.parseLong(createResponse1.header("Location").split("/")[2]);

        ExtractableResponse<Response> createResponse2 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest2)
                .when().post("/lines")
                .then().log().all().
                extract();
        Long lineId2 = Long.parseLong(createResponse2.header("Location").split("/")[2]);

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new SectionRequest(JEONGJA_ID, PANKYO_ID, 10))
                .when().post("/lines/{lineId}/sections", lineId1)
                .then().log().all().
                extract();
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new SectionRequest(JAMSIL_ID, JAMSILNARU_ID, 10))
                .when().post("/lines/{lineId}/sections", lineId2)
                .then().log().all().
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

        List<LineResponse> lineResponses = response.jsonPath().getList(".", LineResponse.class)
                .stream()
                .filter(lineResponse -> lineResponse.getName().equals("신분당선") || lineResponse.getName().equals("구신분당선") )
                .collect(Collectors.toList());

        assertThat(lineResponses.get(0).getStations())
                .extracting("id", "name")
                .containsExactly(
                        tuple(JEONGJA_ID, "정자"),
                        tuple(PANKYO_ID, "판교")
                );
        assertThat(lineResponses.get(1).getStations())
                .extracting("id", "name")
                .containsExactly(
                        tuple(JAMSIL_ID, "잠실"),
                        tuple(JAMSILNARU_ID, "잠실나루")
                );
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

        Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new SectionRequest(JAMSILNARU_ID, JAMSILSAENAE_ID, 10))
                .when().post("/lines/{lineId}/sections", lineId)
                .then().log().all().
                extract();

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        LineResponse resultResponse = response.as(LineResponse.class);
        assertThat(resultResponse.getId()).isEqualTo(lineId);
        assertThat(resultResponse.getStations())
                .extracting("id", "name")
                .containsExactly(
                        tuple(JAMSILNARU_ID, "잠실나루"),
                        tuple(JAMSILSAENAE_ID, "잠실새내")
                );
    }

    @DisplayName("지하철 노선을 수정한다.")
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

    @DisplayName("지하철 노선을 제거한다.")
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

    @DisplayName("빈 노선에 구간을 등록한다")
    @Test
    void addSectionToEmptyLine() {
        //when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new SectionRequest(JAMSIL_ID, JAMSILNARU_ID, 10))
                .when().post("/lines/2/sections")
                .then().log().all().
                extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("구간이 있는 노선에 비연결 구간을 추가하면 예외가 발생한다")
    @Test
    void addingUnlinkedSection_toLineThatHasSection_() {
        //given
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new SectionRequest(JAMSILNARU_ID, JAMSIL_ID, 10))
                .when().post("/lines/2/sections")
                .then().log().all().
                extract();

        //when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new SectionRequest(JAMSILSAENAE_ID, 종합운동장역_ID, 10))
                .when().post("/lines/2/sections")
                .then().log().all().
                extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("구간이 있는 노선에 기점과 이어지는 구간을 추가한다")
    @Test
    void addUpperLinkedSection() {
        //given
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new SectionRequest(JAMSILNARU_ID, JAMSIL_ID, 10))
                .when().post("/lines/2/sections")
                .then().log().all().
                extract();

        //when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new SectionRequest(JAMSILSAENAE_ID, JAMSILNARU_ID, 5))
                .when().post("/lines/2/sections")
                .then().log().all().
                extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("구간이 있는 노선에 종점과 이어지는 구간을 등록한다")
    @Test
    void addLowerLinkedSection() {
        //given
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new SectionRequest(JAMSILNARU_ID, JAMSIL_ID, 10))
                .when().post("/lines/2/sections")
                .then().log().all().
                extract();

        //when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new SectionRequest(JAMSIL_ID, JAMSILSAENAE_ID, 5))
                .when().post("/lines/2/sections")
                .then().log().all().
                extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("노선에 잘못된 거리 정보를 가지는 구간을 추가할 수 없다")
    @Test
    void addSectionWithWrongDistance_fails() {
        //given
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new SectionRequest(JAMSILNARU_ID, JAMSILSAENAE_ID, 15))
                .when().post("/lines/2/sections")
                .then().log().all().
                extract();

        //when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new SectionRequest(JAMSILNARU_ID, JAMSIL_ID, 15))
                .when().post("/lines/2/sections")
                .then().log().all().
                extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("노선에 이미 있는 구간을 등록하면 예외가 발생한다.")
    @Test
    void addDuplicateSection_fails() {
        //given
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new SectionRequest(JAMSILNARU_ID, JAMSIL_ID, 10))
                .when().post("/lines/2/sections")
                .then().log().all().
                extract();

        //when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new SectionRequest(JAMSIL_ID, JAMSILNARU_ID, 5))
                .when().post("/lines/2/sections")
                .then().log().all().
                extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("노선의 역을 제거한다")
    @Test
    void deleteStation() {
        // given
        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();
        Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new SectionRequest(JAMSIL_ID, JAMSILNARU_ID, 10))
                .when().post("/lines/{lineId}/sections", lineId)
                .then().statusCode(HttpStatus.OK.value());

        // when
        RestAssured
                .given().log().all()
                .when().delete("/lines/{lineId}/stations/{stationId}", lineId, JAMSIL_ID)
                .then().log().all()
                .extract();

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().get("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();
        LineResponse lineResponse = response.body().as(LineResponse.class);

        // then
        assertThat(lineResponse.getStations()).hasSize(0);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
