package subway.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import subway.ui.dto.request.AttachStationRequest;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("data")
@DisplayName("지하철역 관련 기능")
class StationIntegrationTest extends IntegrationTest {
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @DisplayName("지하철역 사이에 생성한다.")
    @Test
    void createStationBetween() throws JsonProcessingException {
        // given
        final AttachStationRequest request = new AttachStationRequest("강남", "서초", 1);
        
        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsBytes(request))
                .when()
                .post("/lines/{lineId}/station/between", 1L)
                .then().log().all()
                .extract();
        
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
    
    @DisplayName("지하철역 맨 앞에 생성한다.")
    @Test
    void createStationFront() throws JsonProcessingException {
        // given
        final AttachStationRequest request = new AttachStationRequest("강남", "서초", 1);
        
        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsBytes(request))
                .when()
                .post("/lines/{lineId}/station/front", 1L)
                .then().log().all()
                .extract();
        
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
    
    @DisplayName("지하철역 맨 뒤에 생성한다.")
    @Test
    void createStationEnd() throws JsonProcessingException {
        // given
        final AttachStationRequest request = new AttachStationRequest("선릉", "삼성", 1);
        
        // when
        final ExtractableResponse<Response> response2 = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsBytes(request))
                .when()
                .post("/lines/{lineId}/station/end", 1L)
                .then().log().all()
                .extract();
        
        // then
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}
