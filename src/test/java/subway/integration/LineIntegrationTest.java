package subway.integration;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.dto.request.LineCreateRequest;
import subway.dto.response.LineResponse;

@DisplayName("지하철 노선 관련 기능")
public class LineIntegrationTest extends IntegrationTest {
   private LineCreateRequest lineCreateRequest1;
   private LineCreateRequest lineCreateRequest2;

   @BeforeEach
   public void setUp() {
       super.setUp();

       lineCreateRequest1 = new LineCreateRequest("신분당선");
       lineCreateRequest2 = new LineCreateRequest("구신분당선");
   }

   @DisplayName("지하철 노선을 생성한다")
   @Test
   void createLine() {
       // when
       ExtractableResponse<Response> response = RestAssured
               .given().log().all()
               .contentType(MediaType.APPLICATION_JSON_VALUE)
               .body(lineCreateRequest1)
               .when().post("/lines")
               .then().log().all().
               extract();

       // then
       assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
       assertThat(response.header("Location")).isNotBlank();
   }

   @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다")
   @Test
   void createLineWithDuplicateName() {
       // given
       RestAssured
               .given().log().all()
               .contentType(MediaType.APPLICATION_JSON_VALUE)
               .body(lineCreateRequest1)
               .when().post("/lines")
               .then().log().all().
               extract();

       // when
       ExtractableResponse<Response> response = RestAssured
               .given().log().all()
               .contentType(MediaType.APPLICATION_JSON_VALUE)
               .body(lineCreateRequest1)
               .when().post("/lines")
               .then().log().all().
               extract();

       // then
       assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
   }

   @DisplayName("지하철 노선 목록을 조회한다")
   @Test
   void getLines() {
       // given
       ExtractableResponse<Response> createResponse1 = RestAssured
               .given().log().all()
               .contentType(MediaType.APPLICATION_JSON_VALUE)
               .body(lineCreateRequest1)
               .when().post("/lines")
               .then().log().all().
               extract();

       ExtractableResponse<Response> createResponse2 = RestAssured
               .given().log().all()
               .contentType(MediaType.APPLICATION_JSON_VALUE)
               .body(lineCreateRequest2)
               .when().post("/lines")
               .then().log().all().
               extract();

       // when
       ExtractableResponse<Response> response = RestAssured
               .given().log().all()
               .accept(MediaType.APPLICATION_JSON_VALUE)
               .when().get("/lines")
               .then().log().all()
               .extract();

       // then
       assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
       List<Long> expectedLineIds = Stream.of(createResponse1, createResponse2)
               .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
           .collect(Collectors.toList());
       List<Long> resultLineIds = response.jsonPath().getList(".", LineResponse.class).stream()
               .map(LineResponse::getId)
               .collect(Collectors.toList());
       assertThat(resultLineIds).containsAll(expectedLineIds);
   }

   @DisplayName("지하철 노선을 조회한다")
   @Test
   void getLine() {
       // given
       ExtractableResponse<Response> createResponse = RestAssured
               .given().log().all()
               .contentType(MediaType.APPLICATION_JSON_VALUE)
               .body(lineCreateRequest1)
               .when().post("/lines")
               .then().log().all().
               extract();

       // when
       Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
       ExtractableResponse<Response> response = RestAssured
               .given().log().all()
               .accept(MediaType.APPLICATION_JSON_VALUE)
               .when().get("/lines/{lineId}", lineId)
               .then().log().all()
               .extract();

       // then
       assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
       LineResponse resultResponse = response.as(LineResponse.class);
       assertThat(resultResponse.getId()).isEqualTo(lineId);
   }

   @DisplayName("지하철 노선을 수정한다")
   @Test
   void updateLine() {
       // given
       ExtractableResponse<Response> createResponse = RestAssured
               .given().log().all()
               .contentType(MediaType.APPLICATION_JSON_VALUE)
               .body(lineCreateRequest1)
               .when().post("/lines")
               .then().log().all().
               extract();

       // when
       Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
       ExtractableResponse<Response> response = RestAssured
               .given().log().all()
               .contentType(MediaType.APPLICATION_JSON_VALUE)
               .body(lineCreateRequest2)
               .when().put("/lines/{id}", lineId)
               .then().log().all()
               .extract();

       // then
       assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
   }

   @DisplayName("지하철 노선을 제거한다")
   @Test
   void deleteLine() {
       // given
       ExtractableResponse<Response> createResponse = RestAssured
               .given().log().all()
               .contentType(MediaType.APPLICATION_JSON_VALUE)
               .body(lineCreateRequest1)
               .when().post("/lines")
               .then().log().all().
               extract();

       // when
       Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
       ExtractableResponse<Response> response = RestAssured
               .given().log().all()
               .when().delete("/lines/{lineId}", lineId)
               .then().log().all()
               .extract();

       // then
       assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
   }
}
