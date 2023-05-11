package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Sql({"/test-data.sql"})
@DisplayName("구간 관련 기능")
public class SectionIntegrationTest extends IntegrationTest {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        Map<String, String> lineParams = new HashMap<>();
        lineParams.put("name", "1호선");
        lineParams.put("color", "bg-red-100");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineParams)
                .when().post("/lines")
                .then().extract();

        Map<String, String> stationParams1 = new HashMap<>();
        stationParams1.put("name", "해운대역");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationParams1)
                .when().post("/stations")
                .then().extract();

        Map<String, String> stationParams2 = new HashMap<>();
        stationParams2.put("name", "동대구역");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationParams2)
                .when().post("/stations")
                .then().extract();
    }

    @DisplayName("구간을 생성한다.")
    @Test
    void createSection() {
        // given
        Map<String, String> sectionParams = new HashMap<>();
        sectionParams.put("lineId", "1");
        sectionParams.put("stationId", "1");
        sectionParams.put("upStationId", "2");
        sectionParams.put("distance", "3");

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionParams)
                .when().post("/sections")
                .then().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("구간을 제거한다.")
    @Test
    void deleteSection() {
        // given
        Map<String, String> sectionParams = new HashMap<>();
        sectionParams.put("lineId", "1");
        sectionParams.put("stationId", "1");
        sectionParams.put("upStationId", "2");
        sectionParams.put("distance", "3");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionParams)
                .when().post("/sections")
                .then().extract();
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
}
