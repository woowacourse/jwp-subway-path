package subway.integration;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import subway.ui.dto.request.CreationSectionRequest;
import subway.ui.dto.request.CreationLineRequest;
import subway.ui.dto.request.CreationStationRequest;
import subway.ui.dto.request.ReadPathPriceRequest;
import subway.ui.dto.response.ReadStationResponse;

import java.util.List;

@DisplayName("경로 관련 기능")
@SuppressWarnings("NonAsciiCharacters")
public class PathIntegrationTest extends IntegrationTest {

    Long lineOneId;
    Long lineTwoId;
    Long stationOneId;
    Long stationTwoId;
    Long stationThreeId;
    Long stationFourId;
    Long stationFiveId;
    Long stationSixId;


    /**
     * 신분당선: 양재시민의숲역 - 청계산입구역 - 판교역 - 정자역 - 미금역
     * <br/>
     * 수인분당선: 정자역 - 선릉역 - 미금역
     */
    @BeforeEach
    void 초기_데이터_세팅() {
        // 노선, 역 요청 데이터 세팅
        final CreationLineRequest lineRequestOne = CreationLineRequest.of("신분당선", "bg-red-600");
        final CreationLineRequest lineRequestTwo = CreationLineRequest.of("수인분당선", "bg-yellow-600");
        final CreationStationRequest stationRequestOne = CreationStationRequest.from("양재시민의숲역");
        final CreationStationRequest stationRequestTwo = CreationStationRequest.from("청계산입구역");
        final CreationStationRequest stationRequestThree = CreationStationRequest.from("판교역");
        final CreationStationRequest stationRequestFour = CreationStationRequest.from("정자역");
        final CreationStationRequest stationRequestFive = CreationStationRequest.from("미금역");
        final CreationStationRequest stationRequestSix = CreationStationRequest.from("선릉역");

        // 노선 등록
        final ExtractableResponse<Response> createLineResponseOne = postLine(lineRequestOne);
        final ExtractableResponse<Response> createLineResponseTwo = postLine(lineRequestTwo);

        // 지하철역 등록
        final ExtractableResponse<Response> createStationResponseOne = postStation(stationRequestOne);
        final ExtractableResponse<Response> createStationResponseTwo = postStation(stationRequestTwo);
        final ExtractableResponse<Response> createStationResponseThree = postStation(stationRequestThree);
        final ExtractableResponse<Response> createStationResponseFour = postStation(stationRequestFour);
        final ExtractableResponse<Response> createStationResponseFive = postStation(stationRequestFive);
        final ExtractableResponse<Response> createStationResponseSix = postStation(stationRequestSix);

        // id값 확인
        lineOneId = getParseId(createLineResponseOne);
        lineTwoId = getParseId(createLineResponseTwo);
        stationOneId = getParseId(createStationResponseOne);
        stationTwoId = getParseId(createStationResponseTwo);
        stationThreeId = getParseId(createStationResponseThree);
        stationFourId = getParseId(createStationResponseFour);
        stationFiveId = getParseId(createStationResponseFive);
        stationSixId = getParseId(createStationResponseSix);

        // 구간 요청 데이터 세팅
        final CreationSectionRequest sectionRequestOneLineOne = CreationSectionRequest.of(stationOneId, stationTwoId, 10);
        final CreationSectionRequest sectionRequestOneLineTwo = CreationSectionRequest.of(stationTwoId, stationThreeId, 20);
        final CreationSectionRequest sectionRequestOneLineThree = CreationSectionRequest.of(stationThreeId, stationFourId, 20);
        final CreationSectionRequest sectionRequestOneLineFour = CreationSectionRequest.of(stationFourId, stationFiveId, 40);
        final CreationSectionRequest sectionRequestTwoLineOne = CreationSectionRequest.of(stationFourId, stationSixId, 8);
        final CreationSectionRequest sectionRequestTwoLineTwo = CreationSectionRequest.of(stationSixId, stationFiveId, 8);

        // 구간 등록
        postSection(lineOneId, sectionRequestOneLineOne);
        postSection(lineOneId, sectionRequestOneLineTwo);
        postSection(lineOneId, sectionRequestOneLineThree);
        postSection(lineOneId, sectionRequestOneLineFour);
        postSection(lineTwoId, sectionRequestTwoLineOne);
        postSection(lineTwoId, sectionRequestTwoLineTwo);
    }

    @Test
    void 기본_구간의_경로와_요금을_가져오는지_확인한다() {
        final ReadPathPriceRequest request = ReadPathPriceRequest.of(stationOneId, stationTwoId);

        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().get("/path")
                .then().log().all()
                .extract();

        final JsonPath responseJsonData = response.body().jsonPath();
        final List<Object> stationResponses = responseJsonData.getList("stations");
        final ReadStationResponse responseStationOne = responseJsonData.getObject("stations[0]", ReadStationResponse.class);
        final ReadStationResponse responseStationTwo = responseJsonData.getObject("stations[1]", ReadStationResponse.class);
        final int responsePriceOne = responseJsonData.getInt("price");

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(stationResponses).hasSize(2);
            softAssertions.assertThat(responseStationOne.getName()).isEqualTo("양재시민의숲역");
            softAssertions.assertThat(responseStationTwo.getName()).isEqualTo("청계산입구역");
            softAssertions.assertThat(responsePriceOne).isEqualTo(1250);
        });
    }

    @Test
    void 구간_10km_50km의_경로와_과금을_포함한_요금을_가져오는지_확인한다() {
        final ReadPathPriceRequest request = ReadPathPriceRequest.of(stationOneId, stationFourId);

        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().get("/path")
                .then().log().all()
                .extract();

        final JsonPath responseJsonData = response.body().jsonPath();
        final List<Object> stationResponses = responseJsonData.getList("stations");
        final ReadStationResponse responseStationOne = responseJsonData.getObject("stations[0]", ReadStationResponse.class);
        final ReadStationResponse responseStationTwo = responseJsonData.getObject("stations[1]", ReadStationResponse.class);
        final ReadStationResponse responseStationThree = responseJsonData.getObject("stations[2]", ReadStationResponse.class);
        final ReadStationResponse responseStationFour = responseJsonData.getObject("stations[3]", ReadStationResponse.class);
        final int responsePriceOne = responseJsonData.getInt("price");

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(stationResponses).hasSize(4);
            softAssertions.assertThat(responseStationOne.getName()).isEqualTo("양재시민의숲역");
            softAssertions.assertThat(responseStationTwo.getName()).isEqualTo("청계산입구역");
            softAssertions.assertThat(responseStationThree.getName()).isEqualTo("판교역");
            softAssertions.assertThat(responseStationFour.getName()).isEqualTo("정자역");
            softAssertions.assertThat(responsePriceOne).isEqualTo(2050);
        });
    }

    @Test
    void 구간_10km_50km의_과금과_50km이상의_과금을_포함한_경로와_요금을_가져오는지_확인한다() {
        final ReadPathPriceRequest request = ReadPathPriceRequest.of(stationOneId, stationFiveId);

        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().get("/path")
                .then().log().all()
                .extract();

        final JsonPath responseJsonData = response.body().jsonPath();
        final List<Object> stationResponses = responseJsonData.getList("stations");
        final ReadStationResponse responseStationOne = responseJsonData.getObject("stations[0]", ReadStationResponse.class);
        final ReadStationResponse responseStationTwo = responseJsonData.getObject("stations[1]", ReadStationResponse.class);
        final ReadStationResponse responseStationThree = responseJsonData.getObject("stations[2]", ReadStationResponse.class);
        final ReadStationResponse responseStationFour = responseJsonData.getObject("stations[3]", ReadStationResponse.class);
        final ReadStationResponse responseStationFive = responseJsonData.getObject("stations[4]", ReadStationResponse.class);
        final ReadStationResponse responseStationSix = responseJsonData.getObject("stations[5]", ReadStationResponse.class);
        final int responsePriceOne = responseJsonData.getInt("price");

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(stationResponses).hasSize(6);
            softAssertions.assertThat(responseStationOne.getName()).isEqualTo("양재시민의숲역");
            softAssertions.assertThat(responseStationTwo.getName()).isEqualTo("청계산입구역");
            softAssertions.assertThat(responseStationThree.getName()).isEqualTo("판교역");
            softAssertions.assertThat(responseStationFour.getName()).isEqualTo("정자역");
            softAssertions.assertThat(responseStationFive.getName()).isEqualTo("선릉역");
            softAssertions.assertThat(responseStationSix.getName()).isEqualTo("미금역");
            softAssertions.assertThat(responsePriceOne).isEqualTo(2250);
        });
    }

    private ExtractableResponse<Response> postLine(final CreationLineRequest lineRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> postStation(final CreationStationRequest stationRequest) {
        return RestAssured.given().log().all()
                .body(stationRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }

    private long getParseId(final ExtractableResponse<Response> response) {
        return Long.parseLong(response.header("Location").split("/")[2]);
    }

    private void postSection(final Long lineId, final CreationSectionRequest request) {
        RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{lineId}/sections", lineId)
                .then().log().all()
                .extract();
    }
}
