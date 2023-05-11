package subway.integration.station;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static subway.integration.common.LocationAsserter.location_헤더를_검증한다;
import static subway.integration.station.StationSteps.역_생성_요청;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("StationController 통합테스트")
@Sql("/truncate.sql")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class StationControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 역을_생성한다() {
        // when
        final ExtractableResponse<Response> response = 역_생성_요청("카프카역");

        // then
        assertThat(response.statusCode()).isEqualTo(CREATED.value());
        location_헤더를_검증한다(response, 1L);
    }

    @Test
    void 이미_존재하는_역_생성_요청시_예외() {
        // given
        역_생성_요청("오리역");

        // when
        final ExtractableResponse<Response> response = 역_생성_요청("오리역");

        // then
        assertThat(response.statusCode()).isEqualTo(CONFLICT.value());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 역_이름이_널이거나_공백이면_예외(final String nullAndEmpty) {
        // when
        final ExtractableResponse<Response> response = 역_생성_요청(nullAndEmpty);

        // then
        assertThat(response.statusCode()).isEqualTo(UNPROCESSABLE_ENTITY.value());
    }
}
