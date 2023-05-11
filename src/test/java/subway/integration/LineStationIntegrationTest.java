package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.dto.request.LineStationRequest;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선에 역 추가/삭제")
public class LineStationIntegrationTest extends IntegrationTest {

    @Sql("classpath:/testData.sql")
    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createLineStation() {
        // given
        LineStationRequest lineStationRequest = new LineStationRequest("잠실새내", "종합운동장", 4);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(lineStationRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/1/stations")
                .then()
                .log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Sql("classpath:/testData.sql")
    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 삭제한다.")
    @Test
    void removeLineStation() {
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/lines/1/stations/1")
                .then()
                .log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
