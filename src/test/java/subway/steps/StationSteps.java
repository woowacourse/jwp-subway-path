package subway.steps;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.dto.StationCreateRequest;
import subway.dto.StationResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static subway.fixture.StationFixture.EXPRESS_BUS_TERMINAL_STATION;
import static subway.fixture.StationFixture.SAPYEONG_STATION;

public class StationSteps {

    public static final StationCreateRequest 역_고속터미널 = new StationCreateRequest(EXPRESS_BUS_TERMINAL_STATION.getName());
    public static final StationCreateRequest 역_사평역 = new StationCreateRequest(SAPYEONG_STATION.getName());
    public static final StationCreateRequest 역_양재역 = new StationCreateRequest("양재역");
    public static final StationCreateRequest 역_교대역 = new StationCreateRequest("교대역");
    public static final StationCreateRequest 역_새역 = new StationCreateRequest("새 역");

    public static ExtractableResponse<Response> 역_생성_요청(final StationCreateRequest request) {
        final ExtractableResponse<Response> response = RestAssured
                .given()
                .log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(request)

                .when()
                .post("/stations")

                .then()
                .log().all()
                .extract();
        return response;
    }

    public static long 역_생성하고_아이디_반환(final StationCreateRequest request) {
        return 역_생성_요청(request).as(StationResponse.class).getId();
    }
}
