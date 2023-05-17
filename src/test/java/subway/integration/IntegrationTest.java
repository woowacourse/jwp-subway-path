package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.dto.SectionDeleteRequest;
import subway.dto.SectionRequest;

@SuppressWarnings("NonAsciiCharacters")
@Sql({"classpath:/remove-station.sql", "classpath:/remove-section-line.sql"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class IntegrationTest {

    @BeforeEach
    public void setUp(@LocalServerPort int port) {
        RestAssured.port = port;
    }

    protected RequestSpecification given() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    public ExtractableResponse<Response> 구간을_추가한다(Long lineId, SectionRequest request) {
        return given()
                .body(request)
                .when()
                .post("/lines/{lineId}/stations/", lineId)
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> 노선에서_역을_삭제한다(final Long lineId, final Long stationId) {
        final SectionDeleteRequest request = new SectionDeleteRequest(lineId);
        return given()
                .body(request)
                .when()
                .delete("/lines/{lineId}/stations/{stationId}", lineId, stationId)
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> 노선을_조회한다(final Long lineId) {
        return given()
                .when()
                .get("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();
    }
}
