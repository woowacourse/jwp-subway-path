package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.LineRequest;
import subway.dto.SectionRequest;
import subway.dto.SectionResponse;
import subway.dto.StationRequest;
import subway.util.Request;

public class AddSectionIntegrationTest extends IntegrationTest {
    
    private long testLineId;
    private long testStation1Id;
    private long testStation2Id;
    
    @BeforeEach
    void setUpSection() {
        this.testLineId = Request.postLineRequest(new LineRequest("테스트선", "테스트색"));
        this.testStation1Id = Request.postStationRequest(new StationRequest("테스트역1"));
        this.testStation2Id = Request.postStationRequest(new StationRequest("테스트역2"));
    }
    
    @DisplayName("기존에 역이 없을 경우 새로운 구간을 등록한다.")
    @Test
    void addSectionWithNoStation() {
        // when
        final SectionRequest sectionRequest = new SectionRequest(this.testLineId, this.testStation1Id,
                this.testStation2Id,
                "DOWN", 3);
        
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType("application/json")
                .body(sectionRequest)
                .when().post("/sections")
                .then().log().all()
                .extract();
        
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }
    
    
    //
    @DisplayName("구역 테이블에 해당 라인의 기준역이 존재하지 않는 경우 예외처리")
    @Test
    void createSectionWithNoBaseStationInLine() {
        // given
        this.addBaseTestSection();
        final StationRequest stationRequest3 = new StationRequest("TestStation3");
        final long testStationId3 = Request.postStationRequest(stationRequest3);
        final StationRequest stationRequest4 = new StationRequest("TestStation4");
        final long testStationId4 = Request.postStationRequest(stationRequest4);
        // when
        final SectionRequest sectionRequest2 = new SectionRequest(this.testLineId, testStationId3,
                testStationId4,
                "DOWN", 3);
        
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest2)
                .when().post("/sections")
                .then().log().all().
                extract();
        
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).isEqualTo("기준역이 라인에 존재하지 않습니다.");
    }
    
    private void addBaseTestSection() {
        Request.postSectionRequest(new SectionRequest(this.testLineId, this.testStation2Id,
                this.testStation1Id,
                "DOWN", 3));
    }
    
    @DisplayName("새로운역이 상행 종점일 경우 생성한다.")
    @Test
    void createSectionWithNewStationAndUpEndPoint() {
        // when
        this.addBaseTestSection();
        final StationRequest stationRequest3 = new StationRequest("테스트역3");
        final long testStationId3 = Request.postStationRequest(stationRequest3);
        final SectionRequest sectionRequest2 = new SectionRequest(this.testLineId, testStationId3, this.testStation1Id,
                "UP", 3);
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest2)
                .when().post("/sections")
                .then().log().all().
                extract();
        
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        final List<SectionResponse> result = response.jsonPath().getList(".", SectionResponse.class);
        assertThat(result.get(0).getDistance()).isEqualTo(3);
        assertThat(result.size()).isEqualTo(1);
    }
    
    @DisplayName("새로운역이 하행 종점일 경우 생성한다.")
    @Test
    void createSectionWithNewStationAndDownEndPoint() {
        // when
        this.addBaseTestSection();
        final StationRequest stationRequest3 = new StationRequest("테스트역3");
        final long testStationId3 = Request.postStationRequest(stationRequest3);
        final SectionRequest sectionRequest2 = new SectionRequest(this.testLineId, testStationId3,
                this.testStation2Id,
                "DOWN", 3);
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest2)
                .when().post("/sections")
                .then().log().all().
                extract();
        
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        final List<SectionResponse> result = response.jsonPath().getList(".", SectionResponse.class);
        assertThat(result.get(0).getDistance()).isEqualTo(3);
        assertThat(result.size()).isEqualTo(1);
    }
    
    @DisplayName("갈래길 방지 - 기존 구간의 거리를 초과할 경우 예외처리")
    @Test
    void createSectionWithOverDistance() {
        // when
        this.addBaseTestSection();
        final StationRequest stationRequest3 = new StationRequest("테스트역3");
        final long testStationId3 = Request.postStationRequest(stationRequest3);
        final SectionRequest sectionRequest2 = new SectionRequest(this.testLineId, testStationId3,
                this.testStation2Id,
                "UP", 4);
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest2)
                .when().post("/sections")
                .then().log().all().
                extract();
        
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).isEqualTo("거리가 기존 구역 거리를 초과했습니다.");
    }
    
    @DisplayName("갈래길 방지 - 기존 구간의 거리보다 작은 경우 기존 구역 제거 하고 새로운 구역 2개 생성")
    @Test
    void createSectionWithLessDistance() {
        // when
        this.addBaseTestSection();
        final StationRequest stationRequest3 = new StationRequest("테스트역3");
        final long testStationId3 = Request.postStationRequest(stationRequest3);
        final SectionRequest sectionRequest2 = new SectionRequest(this.testLineId, testStationId3,
                this.testStation2Id,
                "UP", 2);
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest2)
                .when().post("/sections")
                .then().log().all().
                extract();
        
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        final List<SectionResponse> result = response.jsonPath().getList(".", SectionResponse.class);
        assertThat(result.get(0).getDistance()).isEqualTo(2);
        assertThat(result.get(1).getDistance()).isEqualTo(1);
        assertThat(result.size()).isEqualTo(2);
    }
    
    @DisplayName("새로운 역이 구역에 존재하는 경우 예외처리")
    @Test
    void createSectionWithNewStationAndExistInLine() {
        // when
        this.addBaseTestSection();
        final SectionRequest sectionRequest2 = new SectionRequest(this.testLineId, this.testStation1Id,
                this.testStation2Id,
                "DOWN", 3);
        
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest2)
                .when().post("/sections")
                .then().log().all().
                extract();
        
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).isEqualTo("새로운역이 라인에 이미 존재합니다.");
    }
    
    @DisplayName("라인테이블에 라인이 존재하지 않는 경우 예외 처리")
    @Test
    void createSectionWithNotExistLine() {
        // when
        final StationRequest stationRequest3 = new StationRequest("테스트역3");
        final long testStationId3 = Request.postStationRequest(stationRequest3);
        final SectionRequest sectionRequest2 = new SectionRequest(100L, testStationId3,
                this.testStation2Id,
                "DOWN", 3);
        
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest2)
                .when().post("/sections")
                .then().log().all().
                extract();
        
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).isEqualTo("존재하지 않는 라인입니다.");
    }
    
    @DisplayName("새로운 역이 존재하지 않는 경우 예외처리")
    @Test
    void createSectionWithNotExistStation() {
        // when
        final SectionRequest sectionRequest2 = new SectionRequest(this.testLineId, 100L,
                this.testStation2Id,
                "DOWN", 3);
        
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest2)
                .when().post("/sections")
                .then().log().all().
                extract();
        
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).isEqualTo("100는 존재하지 않는 역 아이디입니다.");
    }
    
    @DisplayName("구역 추가 요청 검증 - 방향이 UP, DOWN이 아닌 경우 예외처리")
    @Test
    void createSectionWithInvalidDirection() {
        // when
        final SectionRequest sectionRequest2 = new SectionRequest(this.testLineId, this.testStation1Id,
                this.testStation2Id,
                "INVALID", 3);
        
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest2)
                .when().post("/sections")
                .then().log().all().
                extract();
        
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).isEqualTo("[Pattern] sectionRequest direction must match \"UP|DOWN\"");
    }
    
    @DisplayName("구역 추가 요청 검증 - 거리가 1 이하인 경우 예외처리")
    @Test
    void createSectionWithInvalidDistance() {
        // when
        final SectionRequest sectionRequest2 = new SectionRequest(this.testLineId, this.testStation1Id,
                this.testStation2Id,
                "UP", 0);
        
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest2)
                .when().post("/sections")
                .then().log().all().
                extract();
        
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).isEqualTo(
                "[Min] sectionRequest distance must be greater than or equal to 1");
    }
}
