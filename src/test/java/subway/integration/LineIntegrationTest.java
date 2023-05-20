package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.request.CreateLineRequest;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class LineIntegrationTest extends IntegrationTest {

    private CreateLineRequest createLineRequestOne;
    private CreateLineRequest createLineRequestTwo;

    @BeforeEach
    public void setUp() {
        super.setUp();
        createLineRequestOne = new CreateLineRequest("8호선", "분홍색");
        createLineRequestTwo = new CreateLineRequest("2호선", "초록색");
    }

    @Test
    void 지하철_노선을_생성한다() {
        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createLineRequestOne)
                .when().post("/lines")
                .then().log().all().
                extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @Test
    void 기존에_존재하는_지하철_노선_이름으로_지하철_노선을_생성한다() {
        // given
        노선_생성(createLineRequestOne);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createLineRequestOne)
                .when().post("/lines")
                .then().log().all().
                extract();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
                () -> assertThat(response.body().asString()).isEqualTo("해당 이름의 노선이 이미 존재합니다.")
        );
    }

    @Test
    void 지하철_노선을_조회한다() {
        // given
        final Long lineId = 노선_생성(createLineRequestOne);

        // expect
        RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", lineId)
                .then().log().all()
                .body("name", equalTo("8호선"))
                .body("color", equalTo("분홍색"))
                .statusCode(is(HttpStatus.OK.value()));
    }

    @Test
    void 지하철_노선_목록을_조회한다() {
        // given
        노선_생성(createLineRequestOne);
        노선_생성(createLineRequestTwo);

        // expect
        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .body("size", is(2))
                .body("[0].name", equalTo("8호선"))
                .body("[0].color", equalTo("분홍색"))
                .body("[1].name", equalTo("2호선"))
                .body("[1].color", equalTo("초록색"))
                .statusCode(is(HttpStatus.OK.value()));
    }


    private Long 노선_생성(final CreateLineRequest createLineRequest) {
        final ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createLineRequest)
                .when().post("/lines")
                .then().log().all()
                .extract();

        // expect
        return Long.parseLong(createResponse.header("Location").split("/")[2]);
    }
}
