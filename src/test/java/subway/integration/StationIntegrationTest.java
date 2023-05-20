package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.predicate;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.dto.request.CreateLineRequest;
import subway.dto.request.CreateStationRequest;
import subway.dto.request.DeleteStationRequest;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StationIntegrationTest extends IntegrationTest {

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Test
    void 역을_생성한다() {
        // given
        노선_등록("8호선", "분홍색");

        // expected
        RestAssured.given().log().all()
                .body(new CreateStationRequest(1L, "잠실역", "석촌역", 10))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }


    @Test
    void 기존에_모두_존재하는_이름으로_역을_생성한다() {
        // given
        노선_등록("8호선", "분홍색");
        역_등록("잠실역", "석촌역");

        // expected
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(new CreateStationRequest(1L, "잠실역", "석촌역", 10))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then()
                .log().all().extract();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.body().asString()).contains("두 역 모두 노선에 존재하는 역입니다.")
        );
    }


    @Test
    void 역을_제거한다() {
        // given
        노선_등록("8호선", "분홍색");
        역_등록("잠실역", "석촌역");

        // expected
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new DeleteStationRequest(1L, "석촌역"))
                .when()
                .delete("/stations")
                .then().log().all()
                .statusCode(HttpStatus.ACCEPTED.value());

    }

    @Test
    void 존재하지_않는_역을_제거한다() {
        // given
        노선_등록("8호선", "분홍색");
        역_등록("잠실역", "석촌역");

        // expected
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new DeleteStationRequest(1L, "석촌역"))
                .when()
                .delete("/stations")
                .then().log().all()
                .statusCode(HttpStatus.ACCEPTED.value());
    }

    private void 노선_등록(final String name, final String color) {
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new CreateLineRequest(name, color))
                .when().post("/lines");
    }

    private void 역_등록(final String source, final String target) {
        RestAssured.given().log().all()
                .body(new CreateStationRequest(1L, source, target, 10))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all();
    }
}
