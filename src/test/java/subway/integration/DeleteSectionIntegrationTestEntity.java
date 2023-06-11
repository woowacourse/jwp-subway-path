package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.DeleteSectionRequest;
import subway.dto.LineRequest;
import subway.dto.SectionRequest;
import subway.dto.SectionResponse;
import subway.dto.StationRequest;

public class DeleteSectionIntegrationTestEntity extends IntegrationTest {
    
    @BeforeEach
    void setSection() {
        final LineRequest lineRequest1 = new LineRequest("1호선", "blue");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all();
        final LineRequest lineRequest2 = new LineRequest("2호선", "green");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest2)
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
        
        final SectionRequest sectionRequest = new SectionRequest(2, 2, 1, "DOWN", 3);
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/sections")
                .then().log().all();
        
    }
    
    @DisplayName("해당 라인에 삭제할 역이 존재하지 않으면 예외")
    @Test
    void deleteSection() {
        final DeleteSectionRequest deleteSectionRequest = new DeleteSectionRequest(1, 1);
        final ExtractableResponse<Response> response = RestAssured.
                given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(deleteSectionRequest)
                .when().delete("/sections")
                .then().log().all()
                .extract();
        
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        Assertions.assertThat(response.body().asString()).contains(1 + "는 라인 아이디 " + 1 + "에 존재하지 않는 역 아이디입니다.");
    }
    
    @DisplayName("삭제하려는 역이 종점인 경우 구역을 제거한다.")
    @Test
    void deleteSection2() {
        final int initialSize = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get("/sections/2")
                .then().log().all()
                .extract()
                .jsonPath()
                .getList(".", SectionResponse.class)
                .size();
        
        final DeleteSectionRequest deleteSectionRequest = new DeleteSectionRequest(2, 2);
        final ExtractableResponse<Response> response = RestAssured.
                given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(deleteSectionRequest)
                .when().delete("/sections")
                .then().log().all()
                .extract();
        
        final int finalSize = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get("/sections/2")
                .then().log().all()
                .extract()
                .jsonPath()
                .getList(".", SectionResponse.class)
                .size();
        
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(initialSize - finalSize).isEqualTo(1);
    }
    
    @DisplayName("삭제하려는 역이 종점이 아닌 경우 두 구역을 제거하고 하나의 구역으로 만든다.")
    @Test
    void deleteSection3() {
        final int initialSize = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get("/sections/2")
                .then().log().all()
                .extract()
                .jsonPath()
                .getList(".", SectionResponse.class)
                .size();
        
        final StationRequest stationRequest = new StationRequest("삼성역");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationRequest)
                .when().post("/station")
                .then().log().all();
        
        final SectionRequest sectionRequest = new SectionRequest(2, 3, 2, "DOWN", 3);
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/sections")
                .then().log().all();
        
        final DeleteSectionRequest deleteSectionRequest = new DeleteSectionRequest(2, 2);
        final ExtractableResponse<Response> response = RestAssured.
                given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(deleteSectionRequest)
                .when().delete("/sections")
                .then().log().all()
                .extract();
        
        final int finalSize = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get("/sections/2")
                .then().log().all()
                .extract()
                .jsonPath()
                .getList(".", SectionResponse.class)
                .size();
        
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(initialSize - finalSize).isEqualTo(1);
    }
}
