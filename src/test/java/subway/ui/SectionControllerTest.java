package subway.ui;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import subway.dto.LineSaveDto;
import subway.dto.SectionSaveDto;
import subway.integration.IntegrationTest;

class SectionControllerTest extends IntegrationTest {

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @DisplayName("구간을 생성한다.")
    @Test
    void createSection() {
        // 노선등록
        ExtractableResponse<Response> createLineResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new LineSaveDto("1호선"))
                .when().post("/subway/lines")
                .then().log().all().
                extract();

        Long lineId = Long.parseLong(createLineResponse.header("Location").split("/")[3]);

        // 구간등록
        ExtractableResponse<Response> createSectionResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new SectionSaveDto("잠실", "잠실나루", 3))
                .when().post("/sections/{lineId}", lineId)
                .then().log().all().
                extract();

        // 역 삭제(해당 역 구간 삭제까지)
        ExtractableResponse<Response> deleteSectionByStationResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new SectionSaveDto("잠실", "잠실나루", 3))
                .when().delete("/sections/{lineId}/stations/{stationId}", lineId, 1)
                .then().log().all().
                extract();

        assertThat(deleteSectionByStationResponse.statusCode()).isEqualTo(204);
    }
}