package subway.integration.station;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.integration.station.StationSteps.역_단건_삭제_요청;
import static subway.integration.station.StationSteps.역_등록_요청;
import static subway.integration.station.StationSteps.역_전체_조회_요청;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import subway.dto.StationSelectResponse;
import subway.integration.IntegrationTest;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayName("지하철역 관련 기능")
@ActiveProfiles("test")
public class StationIntegrationTest extends IntegrationTest {
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");

        // when
        ExtractableResponse<Response> response = 역_등록_요청(params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");
        역_등록_요청(params);

        // when
        ExtractableResponse<Response> response = 역_등록_요청(params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void getStations() {
        /// given
        Map<String, String> params1 = new HashMap<>();
        params1.put("name", "강남역");
        ExtractableResponse<Response> createResponse1 = 역_등록_요청(params1);

        Map<String, String> params2 = new HashMap<>();
        params2.put("name", "역삼역");
        ExtractableResponse<Response> createResponse2 = 역_등록_요청(params2);

        // when
        ExtractableResponse<Response> response = 역_전체_조회_요청();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<Long> expectedStationIds = Stream.of(createResponse1, createResponse2)
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());
        List<Long> resultStationIds = response.jsonPath().getList(".", StationSelectResponse.class).stream()
                .map(StationSelectResponse::getId)
                .collect(Collectors.toList());
        assertThat(resultStationIds).containsAll(expectedStationIds);
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");
        ExtractableResponse<Response> createResponse = 역_등록_요청(params);

        // when
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = 역_단건_삭제_요청(uri);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
