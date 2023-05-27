package subway.acceptance.step;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.MediaType;
import subway.dto.LineResponse;
import subway.dto.LineSaveRequest;

@SuppressWarnings("NonAsciiCharacters")
public class LineStep {

    public static ExtractableResponse<Response> 노선_생성_요청(final String 노선명, final String 노선색) {
        final LineSaveRequest request = new LineSaveRequest(노선명, 노선색);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선_전체_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    public static void 노선_전체_조회_결과를_확인한다(
            final ExtractableResponse<Response> 요청_결과,
            final LineResponse... 노선_정보
    ) {
        final List<LineResponse> 전체_노선_정보 = Arrays.stream(노선_정보).collect(toList());
        assertThat(요청_결과.jsonPath().getList(".", LineResponse.class))
                .usingRecursiveComparison()
                .isEqualTo(전체_노선_정보);
    }

    public static LineResponse 노선_정보(final String 노선명, final String 노선색, final String... 역) {
        final List<String> 전체_역 = Arrays.stream(역).collect(toList());
        return new LineResponse(노선명, 노선색, 전체_역);
    }
}
