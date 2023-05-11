package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import subway.ui.dto.request.AttachStationRequest;

@Disabled
@ActiveProfiles("data")
@DisplayName("지하철역 관련 기능")
public class StationIntegrationTest extends IntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("지하철역 사이에 생성한다.")
    @Test
    void createStationBetween() throws JsonProcessingException {
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .get("/lines")
                .then().log().all()
                .extract();

        // given
        final AttachStationRequest request = new AttachStationRequest(
                "강남",
                "서초",
                1
        );

        // when
        final ExtractableResponse<Response> response2 = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsBytes(request))
                .when()
                .post("/lines/{lineId}/station/between", 1L)
                .then().log().all()
                .extract();

        // then
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }
}
