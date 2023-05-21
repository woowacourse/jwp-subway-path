package subway.common.steps;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.MediaType;
import subway.dto.LineDto;
import subway.dto.SectionDto;
import subway.dto.ShortestPathRequest;
import subway.dto.ShortestPathResponse;

@SuppressWarnings("NonAsciiCharacters")
public class PathSteps {

    public static ExtractableResponse<Response> 최단거리_경로_조회_요청(final String 시작역, final String 도착역, final Integer 나이) {
        final ShortestPathRequest request = new ShortestPathRequest(시작역, 도착역, 나이);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("start", request.getStart())
                .queryParam("end", request.getEnd())
                .queryParam("age", request.getAge())
                .when().get("/shortest-path")
                .then().log().all()
                .extract();
    }

    public static void 최단거리_경로_조회_결과를_확인한다(
            final ExtractableResponse<Response> 요청_결과,
            final Integer 거리,
            final Integer 운임,
            final LineDto... 경로
    ) {
        final List<LineDto> 경로_모음 = Arrays.stream(경로).collect(Collectors.toList());
        assertThat(요청_결과.jsonPath().getObject(".", ShortestPathResponse.class))
                .usingRecursiveComparison()
                .isEqualTo(new ShortestPathResponse(경로_모음, 거리, 운임));
    }

    public static LineDto 최단거리_경로(
            final String 노선명,
            final String 노선색,
            final SectionDto... 구간) {
        final List<SectionDto> 구간_모음 = Arrays.stream(구간).collect(Collectors.toList());
        return new LineDto(노선명, 노선색, 구간_모음);
    }

    public static SectionDto 최단거리_구간(
            final String 상행역,
            final String 하행역,
            final int 거리
    ) {
        return new SectionDto(상행역, 하행역, 거리);
    }
}
