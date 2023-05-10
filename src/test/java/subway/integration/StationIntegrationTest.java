package subway.integration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.StationRequest;

@DisplayName("지하철역 관련 기능")
public class StationIntegrationTest extends IntegrationTest {

    private StationRequest stationRequest1;

    @BeforeEach
    public void setUp() {
        super.setUp();

        stationRequest1 = new StationRequest("잠실역", "잠실새내역", 10, 1);
    }

    /*
    역이 등록될 때 양의 정수인 거리 정보가 포함되어야 한다.
  - [ ] 노선에 역이 하나도 없을 때 두 역을 동시에 등록해야 한다.
  - [ ] 하나의 역은 여러 개의 노선에 등록될 수 있다.
  - [ ] 존재하는 역의 다음 역을 추가하면 중간에 역이 등록될 수 있다.
  - [ ] 노선 사이에 역이 등록되는 경우 등록된 역을 포함한 전 역, 다음 역의 거리를 업데이트 한다.
  - [ ] 노선 사이에 역이 등록되는 경우 새로 추가되는 역과 이전 역의 거리는 기존 역 사이의 거리보다 작아야 한다.
  - [ ] 기준이 되는 역이 없으면 등록할 수 없다.
    * */

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(stationRequest1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.header("Location")).isNotBlank();
    }

}
