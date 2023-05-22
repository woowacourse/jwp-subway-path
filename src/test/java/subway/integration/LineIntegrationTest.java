//package subway.integration;
//
//import io.restassured.RestAssured;
//import io.restassured.response.ExtractableResponse;
//import io.restassured.response.Response;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import subway.line.presentation.dto.LineRequest;
//import subway.line.presentation.dto.LineResponse;
//import subway.line.presentation.dto.SectionSavingRequest;
//
//import java.util.List;
//import java.util.Map;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DisplayName("지하철 노선 관련 기능")
//public class LineIntegrationTest extends IntegrationTest {
//    private LineRequest lineRequest1;
//    private LineRequest lineRequest2;
//
//    @BeforeEach
//    public void setUp() {
//        super.setUp();
//
//        lineRequest1 = new LineRequest("신분당선", "bg-red-600");
//        lineRequest2 = new LineRequest("구신분당선", "bg-red-600");
//    }
//
//    @DisplayName("지하철 노선을 생성한다.")
//    @Test
//    void createLine() {
//        // when
//        ExtractableResponse<Response> response = RestAssured
//                .given().log().all()
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .body(lineRequest1)
//                .when().post("/lines")
//                .then().log().all().
//                extract();
//
//        // then
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
//        assertThat(response.header("Location")).isNotBlank();
//    }
//
//    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
//    @Test
//    void createLineWithDuplicateName() {
//        // given
//        saveLine(lineRequest1);
//
//        // when
//        ExtractableResponse<Response> response = RestAssured
//                .given().log().all()
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .body(lineRequest1)
//                .when().post("/lines")
//                .then().log().all().
//                extract();
//
//        // then
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
//    }
//
//    @DisplayName("지하철 노선 목록을 조회한다.")
//    @Test
//    void getLines() {
//        // given
//        String location1 = saveLine(lineRequest1);
//        String location2 = saveLine(lineRequest2);
//
//        saveStation("개룡역");
//        saveStation("거여역");
//        saveSection(location1, "개룡역", "거여역");
//
//        saveStation("용인역");
//        saveStation("강릉역");
//        saveSection(location2, "용인역", "강릉역");
//
//        // when
//        ExtractableResponse<Response> response = RestAssured
//                .given().log().all()
//                .accept(MediaType.APPLICATION_JSON_VALUE)
//                .when().get("/lines")
//                .then().log().all()
//                .extract();
//
//        // then
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
//        List<LineResponse> responses = response.jsonPath().getList(".", LineResponse.class);
//        for (LineResponse lineResponse : responses) {
//            assertThat(lineResponse.getStations()).hasSize(2);
//        }
//    }
//
//    @DisplayName("지하철 노선을 조회한다.")
//    @Test
//    void getLine() {
//        // given
//        String requestUri = saveLine(lineRequest1);
//
//        saveStation("개룡역");
//        saveStation("거여역");
//        saveSection(requestUri, "개룡역", "거여역");
//
//        // when
//        ExtractableResponse<Response> response = RestAssured
//                .given().log().all()
//                .accept(MediaType.APPLICATION_JSON_VALUE)
//                .when().get(requestUri)
//                .then().log().all()
//                .extract();
//
//        // then
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
//        Long lineId = Long.parseLong(requestUri.split("/")[2]);
//        LineResponse resultResponse = response.as(LineResponse.class);
//        assertThat(resultResponse.getId()).isEqualTo(lineId);
//        assertThat(resultResponse.getStations()).hasSize(2);
//    }
//
//    @DisplayName("지하철 노선을 수정한다.")
//    @Test
//    void updateLine() {
//        // given
//        String location = saveLine(lineRequest1);
//
//        // when
//        Long lineId = Long.parseLong(location.split("/")[2]);
//        ExtractableResponse<Response> response = RestAssured
//                .given().log().all()
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .body(lineRequest2)
//                .when().put("/lines/{lineId}", lineId)
//                .then().log().all()
//                .extract();
//
//        // then
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
//    }
//
//    @DisplayName("지하철 노선을 제거한다.")
//    @Test
//    void deleteLine() {
//        // given
//        String location = saveLine(lineRequest1);
//
//        // when
//        Long lineId = Long.parseLong(location.split("/")[2]);
//        ExtractableResponse<Response> response = RestAssured
//                .given().log().all()
//                .when().delete("/lines/{lineId}", lineId)
//                .then().log().all()
//                .extract();
//
//        // then
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
//    }
//
//    private static void saveSection(String requestUri, String previousStationName, String nextStationName) {
//        SectionSavingRequest sectionSavingRequest
//                = new SectionSavingRequest(previousStationName, nextStationName, 5, true);
//        RestAssured
//                .given().log().all()
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .body(sectionSavingRequest)
//                .when().post(requestUri + "/section")
//                .then().log().all();
//    }
//
//    private static void saveStation(String name) {
//        RestAssured.given().log().all()
//                .body(Map.of("name", name))
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .when()
//                .post("/stations")
//                .then().log().all();
//    }
//
//    private String saveLine(LineRequest lineRequest) {
//        return RestAssured
//                .given().log().all()
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .body(lineRequest)
//                .when().post("/lines")
//                .then().log().all()
//                .extract()
//                .header("location");
//    }
//}
