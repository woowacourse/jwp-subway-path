package subway.common.steps;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.MediaType;
import subway.dto.LineAddRequest;
import subway.dto.LineResponse;
import subway.dto.LineUpdateRequest;

@SuppressWarnings("NonAsciiCharacters")
public class LineSteps {

    public static ExtractableResponse<Response> 노선_생성_요청(final String 노선명, final String 색상, final Integer 추가운임) {
        final LineAddRequest request = new LineAddRequest(노선명, 색상, 추가운임);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선_조회_요청(final String 노선_번호) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/" + 노선_번호)
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

    public static ExtractableResponse<Response> 노선_수정_요청(final String 노선_번호, final String 노선명, final String 색상) {
        final LineUpdateRequest request = new LineUpdateRequest(노선명, 색상);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().patch("/lines/" + 노선_번호)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 노선_삭제_요청(final String 노선_번호) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/" + 노선_번호)
                .then().log().all()
                .extract();
    }

    public static String 노선_번호를_구한다(final ExtractableResponse<Response> 요청_결과) {
        final int index = 2;
        return 요청_결과.header("Location").split("/")[index];
    }

    public static void 노선_조회_결과를_확인한다(
            final ExtractableResponse<Response> 요청_결과,
            final String 노선명,
            final String 색상,
            final Integer 추가운임,
            final String... 역
    ) {
        final List<String> 역_모음 = Arrays.stream(역).collect(Collectors.toList());
        assertThat(요청_결과.jsonPath().getObject(".", LineResponse.class))
                .usingRecursiveComparison()
                .isEqualTo(new LineResponse(노선명, 색상, 추가운임, 역_모음));
    }

    public static LineResponse 노선_정보(final String 노선명, final String 색상, final Integer 추가운임, final String... 역) {
        final List<String> 역_모음 = Arrays.stream(역).collect(Collectors.toList());
        return new LineResponse(노선명, 색상, 추가운임, 역_모음);
    }

    public static void 노선_전체_조회_결과를_확인한다(
            final ExtractableResponse<Response> 요청_결과,
            final LineResponse... 노선_정보
    ) {
        final List<LineResponse> 전체_노선_정보 = Arrays.stream(노선_정보).collect(Collectors.toList());
        assertThat(요청_결과.jsonPath().getList(".", LineResponse.class))
                .usingRecursiveComparison()
                .isEqualTo(전체_노선_정보);
    }
}
