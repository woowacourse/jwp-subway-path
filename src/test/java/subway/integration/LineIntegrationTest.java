package subway.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static fixtures.IntegrationFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineIntegrationTest extends IntegrationTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        super.setUp();
        objectMapper = super.objectMapper;
    }

    /**
     * integration-test-data.sql 로 현재 등록되어 있는 지하철 정보
     * 2호선 : 선릉역 - 잠실역 - 건대역 (선릉~잠실 거리 : 5, 잠실~건대 거리 : 10
     * 8호선 : 잠실역 - 암사역 (거리: 8)
     */

    @Test
    @DisplayName("지정한 노선을 조회한다.")
    void getLineStationsTest() throws JsonProcessingException {
        // given
        Long lineId = LINE2_ID;

        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .when()
                        .get("/lines/" + lineId)
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().asString()).isEqualTo(objectMapper.writeValueAsString(LINE2_노선도));
    }

    @Test
    @DisplayName("지하철 전체 노선을 조회한다.")
    void getAllLineStationsTest() throws JsonProcessingException {
        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .when()
                        .get("/lines/")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().asString()).isEqualTo(objectMapper.writeValueAsString(ALL_LINE_노선도));
    }
}