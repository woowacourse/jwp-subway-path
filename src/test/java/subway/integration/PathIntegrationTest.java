package subway.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fixtures.IntegrationFixtures;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.PathRequest;

import static fixtures.IntegrationFixtures.REQUEST_PATH_선릉역_TO_암사역;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("경로 및 요금 조회 기능")
class PathIntegrationTest extends IntegrationTest {

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
    @DisplayName("경로와 요금을 조회할 수 있다.")
    void findPathTest() throws JsonProcessingException {
        // given
        PathRequest request = REQUEST_PATH_선릉역_TO_암사역;

        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .get("/path")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().asString()).isEqualTo(objectMapper.writeValueAsString(IntegrationFixtures.PATH_선릉역_TO_암사역));
    }
}
