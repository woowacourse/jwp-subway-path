package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.LineRequest;
import subway.dto.SectionRequest;
import subway.dto.StationRequest;

public class SectionIntegrationTest extends IntegrationTest {
    
    @DisplayName("지하철 구역을 생성한다.")
    @Test
    void createLine() {
        // when
        final SectionRequest sectionRequest = new SectionRequest(1, 2, 1, true, 3);
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/section")
                .then().log().all().
                extract();
        
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }
    
    @DisplayName("라인테이블에 라인이 존재하지 않는 경우 예외 처리")
    @Test
    void createLineWithNoLine() {
        // when
        final SectionRequest sectionRequest = new SectionRequest(1, 2, 1, true, 3);
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/section")
                .then().log().all().
                extract();
        
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).isEqualTo("존재하지 않는 라인입니다.");
    }
    
    @DisplayName("새로운 역이 존재하지 않는 경우 예외처리")
    @Test
    void createLineWithNoStation() {
        // when
        final LineRequest lineRequest = new LineRequest("2호선", "green");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .when().post("/lines")
                .then().log().all();
        
        final StationRequest stationRequest = new StationRequest("강남역");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationRequest)
                .when().post("/stations")
                .then().log().all();
        
        final SectionRequest sectionRequest = new SectionRequest(1, 2, 1, true, 3);
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/section")
                .then().log().all().
                extract();
        
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).isEqualTo("2는 존재하지 않는 역 아이디입니다.");
    }
    
    @DisplayName("구역테이블에 라인이 존재하지 않는 경우 신규 등록")
    @Test
    void createLineWithNoSection() {
        // when
        final LineRequest lineRequest = new LineRequest("2호선", "green");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .when().post("/lines")
                .then().log().all();
        
        final StationRequest stationRequest = new StationRequest("강남역");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationRequest)
                .when().post("/stations")
                .then().log().all();
        
        final StationRequest stationRequest2 = new StationRequest("잠실역");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationRequest2)
                .when().post("/stations")
                .then().log().all();
        
        final SectionRequest sectionRequest = new SectionRequest(1, 2, 1, true, 3);
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/section")
                .then().log().all().
                extract();
        
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}
