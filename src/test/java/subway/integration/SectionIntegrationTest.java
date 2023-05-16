package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.AddOneSectionRequest;
import subway.dto.LineRequest;
import subway.dto.StationRequest;

public class SectionIntegrationTest extends IntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    private void postStation(final String body) {
        RestAssured.given()
                .body(body)
                .contentType(ContentType.JSON)
                .when()
                .post("/stations");
    }

    private void postLine(final String body) {
        RestAssured.given()
                .body(body)
                .contentType(ContentType.JSON)
                .when()
                .post("/lines");
    }

    @Test
    void 빈_노선에_2개의_역을_추가한다() throws JsonProcessingException {
        //given
        String 신림역 = objectMapper.writeValueAsString(new StationRequest("신림"));
        String 봉천역 = objectMapper.writeValueAsString(new StationRequest("봉천"));
        String _2호선 = objectMapper.writeValueAsString(new LineRequest("2호선", "초록색"));
        postStation(신림역);
        postStation(봉천역);
        postLine(_2호선);

        AddOneSectionRequest addOneSectionRequest = new AddOneSectionRequest(1L, 2L, 10);
        String request = objectMapper.writeValueAsString(addOneSectionRequest);

        //when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/lines/{lineId}", 1L)
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isEqualTo("/lines/" + 1L);
    }
}
