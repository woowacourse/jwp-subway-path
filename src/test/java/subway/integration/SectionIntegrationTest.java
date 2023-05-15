package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.*;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("특정 노선에서의 역 추가 및 삭제 테스트")
public class SectionIntegrationTest extends IntegrationTest {
    @DisplayName("지하철 노선에 처음 두 역을 등록할 수 있다.")
    @Test
    void initialize() {
        String location = saveLine("신분당선", "bg-red-600");
        saveStation("개룡역");
        saveStation("거여역");
        final var response = saveSection("개룡역", "거여역", 5, location, false);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("지하철 노선에 상행 방향으로 역을 추가할 수 있다.")
    @Test
    void insertUp() {
        String requestUri = saveLine("신분당선", "bg-red-600");
        saveStation("개룡역");
        saveStation("거여역");
        final var response = saveSection("개룡역", "거여역", 5, requestUri, false);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("역과 역 사이의 거리로 양의 정수만을 입력할 수 있다.")
    @Test
    void invalidDistance() {
        final var requestUri = saveLine("신분당선", "bg-red-600");
        saveStation("개룡역");
        saveStation("거여역");

        String json = "{\n" +
                "    \"previousStationName\": \"개룡역\",\n" +
                "    \"nextStationName\": \"거여역\",\n" +
                "    \"distance\": -3,\n" +
                "    \"down\": false\n" +
                "}";
        ExtractableResponse<Response> response2 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(json)
                .when().post(requestUri + "/section")
                .then().log().all()
                .extract();

        assertThat(response2.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("역과 역 사이에 추가하는 과정에서 거리가 양의 정수보다 내려갈 수 없다.")
    @Test
    void invalidDistance2() {
        String requestUri = saveLine("신분당선", "bg-red-600");
        saveStation("개룡역");
        saveStation("거여역");
        saveStation("용암역");

        saveSection("개룡역", "거여역", 5, requestUri, false);
        final var response = saveSection("개룡역", "용암역", 5, requestUri, false);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("노선상에 있는 역을 삭제할 수 있습니다.")
    void delete() {
        String requestUri = saveLine("신분당선", "bg-red-600");
        saveStation("개룡역");
        saveStation("거여역");
        saveStation("용암역");
        saveSection("개룡역", "거여역", 5, requestUri, false);
        saveSection("개룡역", "용암역", 2, requestUri, false);


        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(Map.of("name", "용암역"))
                .when().delete(requestUri + "/section")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("한 노선 위에 있는 두 역 사이의 거리를 측정할 수 있다.")
    void distanceOnSameLine() {
        // given
        final var lineIdSBD = saveLine("신분당선", "bg-red-600");
        saveStation("강남역");
        saveStation("양재역");
        saveStation("서울시민의 숲");
        saveSection("강남역", "양재역", 4, lineIdSBD + "/section", true);
        saveSection("양재역", "서울시민의 숲", 4, lineIdSBD + "/section", true);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new SectionRequest("강남역", "서울시민의 숲"))
                .when().get("/lines/section")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        final var sectionResponse = response.as(SectionResponse.class);
        assertThat(sectionResponse.getStartingStation().getName()).isEqualTo("강남역");
        assertThat(sectionResponse.getDestinationStation().getName()).isEqualTo("서울시민의 숲");
        assertThat(sectionResponse.getDistance()).isEqualTo(8);
        assertThat(sectionResponse.getShortestStationPath())
                .extracting(StationResponse::getName)
                .containsExactly("강남역", "양재역", "서울시민의숲");
    }

    @Test
    @DisplayName("다른 노선 위에 있는 두 역 사이의 거리를 측정할 수 있다.")
    void distanceOnDifferentLine() {
        // given
        final var line7Id = saveLine("7호선", "green");
        final var lineBDId = saveLine("분당선", "yellow");

        saveStation("반포역");
        saveStation("논현역");
        saveStation("학동역");
        saveStation("강남구청");
        saveStation("청담역");
        saveStation("압구정로데오");
        saveStation("서울숲");

        saveSection("반포역", "논현역", 3, line7Id + "/section", true);
        saveSection("논현역", "학동역", 4, line7Id + "/section", true);
        saveSection("학동역", "강남구청", 3, line7Id + "/section", true);
        saveSection("강남구청", "청담역", 4, line7Id + "/section", true);

        saveSection("강남구청", "압구정로데오", 4, lineBDId + "/section", true);
        saveSection("압구정로데오", "서울숲", 5, lineBDId + "/section", true);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new SectionRequest("논현역", "서울숲"))
                .when().get("/lines/section")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        final var sectionResponse = response.as(SectionResponse.class);
        assertThat(sectionResponse.getStartingStation().getName()).isEqualTo("강남역");
        assertThat(sectionResponse.getDestinationStation().getName()).isEqualTo("서울시민의 숲");
        assertThat(sectionResponse.getDistance()).isEqualTo(16);
        assertThat(sectionResponse.getShortestStationPath())
                .extracting(StationResponse::getName)
                .containsExactly("학동역", "강남구청", "압구정로데오");
    }

    private static String saveLine(String name, String color) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new LineRequest(name, color))
                .when().post("/lines")
                .then().log().all()
                .extract()
                .header("location");
    }

    private static void saveStation(String name) {
        RestAssured.given().log().all()
                .body(Map.of("name", name))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all();
    }

    private static ExtractableResponse<Response> saveSection(String previousStationName, String nextStationName, int distance, String requestUri, boolean isDown) {
        SectionSavingRequest sectionSavingRequest
                = new SectionSavingRequest(previousStationName, nextStationName, distance, isDown);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionSavingRequest)
                .when().post(requestUri + "/section")
                .then().log().all()
                .extract();
    }
}
