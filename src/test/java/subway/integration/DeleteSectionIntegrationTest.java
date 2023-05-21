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
import subway.util.Request;

public class DeleteSectionIntegrationTest extends IntegrationTest {
    
    private long lastStationId;
    private long lastLineId;
    private long lastSectionId;
    
    @BeforeEach
    void setSection() {
        final LineRequest lineRequest = new LineRequest("테스트 호선", "blue");
        this.lastLineId = Request.postLineRequest(lineRequest);
        
        final StationRequest stationRequest = new StationRequest("테스트역1");
        this.lastStationId = Request.postStationRequest(stationRequest);
        
        final StationRequest stationRequest2 = new StationRequest("테스트역2");
        final long nextStationId = Request.postStationRequest(stationRequest2);
        
        final SectionRequest sectionRequest = new SectionRequest(this.lastLineId, nextStationId, this.lastStationId,
                "DOWN",
                1);
        this.lastSectionId = Request.postSectionRequest(sectionRequest);
        
        final StationRequest stationRequest3 = new StationRequest("테스트역3");
        final long nextNextStationId = Request.postStationRequest(stationRequest3);
        
        final SectionRequest sectionRequest2 = new SectionRequest(this.lastLineId, nextNextStationId, nextStationId,
                "DOWN", 1);
        
        Request.postSectionRequest(sectionRequest2);
    }
    
    @DisplayName("해당 라인에 삭제할 역이 존재하지 않으면 예외")
    @Test
    void deleteSection() {
        final DeleteSectionRequest deleteSectionRequest = new DeleteSectionRequest(1, this.lastLineId);
        final ExtractableResponse<Response> response = RestAssured.
                given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(deleteSectionRequest)
                .when().delete("/sections")
                .then().log().all()
                .extract();
        
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        Assertions.assertThat(response.body().asString())
                .contains(1 + "는 라인 아이디 " + this.lastLineId + "에 존재하지 않는 역 아이디입니다.");
    }
    
    @DisplayName("삭제하려는 역이 종점인 경우 구역을 제거한다.")
    @Test
    void deleteSection2() {
        final int initialSize = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get("/lines/" + this.lastLineId + "/sections")
                .then().log().all()
                .extract()
                .jsonPath()
                .getList(".", SectionResponse.class)
                .size();
        
        final DeleteSectionRequest deleteSectionRequest = new DeleteSectionRequest(this.lastStationId, this.lastLineId);
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
                .get("/lines/" + this.lastLineId + "/sections")
                .then().log().all()
                .extract()
                .jsonPath()
                .getList(".", SectionResponse.class)
                .size();
        
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        Assertions.assertThat(initialSize - finalSize).isEqualTo(1);
    }
    
    @DisplayName("삭제하려는 역이 종점이 아닌 경우 두 구역을 제거하고 하나의 구역으로 만든다.")
    @Test
    void deleteSection3() {
        final int initialSize = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get("/lines/" + this.lastLineId + "/sections")
                .then().log().all()
                .extract()
                .jsonPath()
                .getList(".", SectionResponse.class)
                .size();
        
        final DeleteSectionRequest deleteSectionRequest = new DeleteSectionRequest(this.lastStationId + 1,
                this.lastLineId);
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
                .get("/lines/" + this.lastLineId + "/sections")
                .then().log().all()
                .extract()
                .jsonPath()
                .getList(".", SectionResponse.class)
                .size();
        
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        Assertions.assertThat(initialSize - finalSize).isEqualTo(1);
    }
    
    @DisplayName("삭제하려는 역의 구역이 하나인 경우 역 전부 삭제")
    @Test
    void deleteSection4() {
        
        final DeleteSectionRequest deleteSectionRequest = new DeleteSectionRequest(this.lastStationId + 2,
                this.lastLineId);
        RestAssured.
                given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(deleteSectionRequest)
                .when().delete("/sections")
                .then().log().all();
        
        final DeleteSectionRequest deleteSectionRequest2 = new DeleteSectionRequest(this.lastStationId + 1,
                this.lastLineId);
        RestAssured.
                given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(deleteSectionRequest2)
                .when().delete("/sections")
                .then().log().all();
        
        final int finalSize = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get("/lines/" + this.lastLineId + "/sections")
                .then().log().all()
                .extract()
                .jsonPath()
                .getList(".", SectionResponse.class)
                .size();
        
        Assertions.assertThat(finalSize).isEqualTo(0);
    }
    
    @DisplayName("구간 삭제 요청 검증 - 라인 아이디가 0 이하인 경우")
    @Test
    void deleteSection5() {
        final DeleteSectionRequest deleteSectionRequest = new DeleteSectionRequest(this.lastStationId + 2,
                0);
        final ExtractableResponse<Response> response = RestAssured.
                given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(deleteSectionRequest)
                .when().delete("/sections")
                .then().log().all()
                .extract();
        
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        Assertions.assertThat(response.body().asString())
                .contains("[Min] deleteSectionRequest lineId must be greater than or equal to 1");
    }
    
    @DisplayName("구간 삭제 요청 검증 - 역 아이디가 0 이하인 경우")
    @Test
    void deleteSection6() {
        final DeleteSectionRequest deleteSectionRequest = new DeleteSectionRequest(0,
                this.lastLineId);
        final ExtractableResponse<Response> response = RestAssured.
                given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(deleteSectionRequest)
                .when().delete("/sections")
                .then().log().all()
                .extract();
        
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        Assertions.assertThat(response.body().asString())
                .contains("[Min] deleteSectionRequest stationId must be greater than or equal to 1");
    }
    
}
