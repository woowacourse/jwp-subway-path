package subway.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.dto.AddPathRequest;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class PathIntegrationTest extends IntegrationTest {

    @DisplayName("역이 두개 있는 노선의 역을 삭제했을 때 두 역을 삭제한다.")
    @Test
    void removeStationTest() {
        given().log().all()
                .when()
                .delete("/lines/1/stations/1")
                .then().log().all()
                .statusCode(SC_OK);

        given().log().all()
                .when()
                .get("/lines/1")
                .then().log().all()
                .assertThat()
                .body("stations", empty());
    }

    @DisplayName("역이 세개있는 노선의 마지막 역을 삭제한다.")
    @Test
    void removeStationTest2() {
        given().log().all()
                .when()
                .delete("/lines/2/stations/4")
                .then().log().all()
                .statusCode(SC_OK);

        given().log().all()
                .when()
                .get("/lines/2")
                .then().log().all()
                .assertThat()
                .body("stations", hasSize(2))
                .body("stations.name", containsInAnyOrder("의왕", "수원"));
    }

    @DisplayName("경로에 역을 추가한다.")
    @Test
    void addStationsOnPathTest() throws JsonProcessingException {
        final AddPathRequest request = new AddPathRequest(2L, 4L, 10, "down");
        final String json = objectMapper.writeValueAsString(request);
        given().log().all()
                .body(json)
                .contentType(JSON)
                .when()
                .post("/lines/1/stations")
                .then().log().all()
                .statusCode(SC_OK);

        given().log().all()
                .when()
                .get("/lines/1")
                .then().log().all()
                .assertThat()
                .body("name", is("1호선"))
                .body("stations", hasSize(3))
                .body("stations[2].name", is("선릉"));

    }
}
