package subway.integration.line;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static subway.integration.common.LocationAsserter.location_헤더를_검증한다;
import static subway.integration.line.LineSteps.노선_생성_요청;
import static subway.integration.line.LineSteps.노선_생성하고_아이디_반환;
import static subway.integration.line.LineSteps.노선_전체_조회_요청;
import static subway.integration.line.LineSteps.노선_조회_요청;
import static subway.integration.line.LineSteps.노선에_포함된_N번째_구간을_검증한다;
import static subway.integration.line.LineSteps.단일_노선의_이름을_검증한다;
import static subway.integration.station.StationSteps.역_생성_요청;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.jdbc.Sql;
import subway.dto.response.LineQueryResponse;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("LineController 통합테스트")
@Sql("/truncate.sql")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LineControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Nested
    class 노선을_생성할_떄 {

        @Test
        void 두_종착역이_존재하는_경우_생성된다() {
            // given
            역_생성_요청("잠실역");
            역_생성_요청("사당역");

            // when
            final ExtractableResponse<Response> response = 노선_생성_요청("1호선", "잠실역", "사당역", 10, 100);

            // then
            assertThat(response.statusCode()).isEqualTo(CREATED.value());
            location_헤더를_검증한다(response, 1L);
        }

        @Test
        void 노선이_이미_존재하면_예외() {
            // given
            역_생성_요청("잠실역");
            역_생성_요청("사당역");
            노선_생성_요청("1호선", "잠실역", "사당역", 10, 100);

            // when
            final ExtractableResponse<Response> response = 노선_생성_요청("1호선", "잠실역", "사당역", 20, 100);

            // then
            assertThat(response.statusCode()).isEqualTo(CONFLICT.value());
        }

        @Test
        void 상행역이_존재하지_않으면_예외() {
            // given
            역_생성_요청("사당역");

            // when
            final ExtractableResponse<Response> response = 노선_생성_요청("1호선", "잠실역", "사당역", 10, 100);

            // then
            assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());
        }

        @Test
        void 하행역이_존재하지_않으면_예외() {
            // given
            역_생성_요청("잠실역");

            // when
            final ExtractableResponse<Response> response = 노선_생성_요청("1호선", "잠실역", "사당역", 10, 100);

            // then
            assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());
        }

        @Test
        void 역_사이의_거리가_양수가_아니면_오류이다() {
            // given
            역_생성_요청("잠실역");
            역_생성_요청("사당역");

            // when
            final ExtractableResponse<Response> response = 노선_생성_요청("1호선", "잠실역", "사당역", 0, 100);

            // then
            assertThat(response.statusCode()).isEqualTo(UNPROCESSABLE_ENTITY.value());
        }

        @ParameterizedTest
        @NullAndEmptySource
        void 호선이_공백이거나_널이면_예외(final String nullAndEmpty) {
            // when
            final ExtractableResponse<Response> response = 노선_생성_요청(nullAndEmpty, "잠실역", "사당역", 0, 100);

            // then
            assertThat(response.statusCode()).isEqualTo(UNPROCESSABLE_ENTITY.value());
        }


        @ParameterizedTest
        @NullAndEmptySource
        void 상행역이_공백이거나_널이면_예외(final String nullAndEmpty) {
            // when
            final ExtractableResponse<Response> response = 노선_생성_요청("1호선", nullAndEmpty, "사당역", 0, 100);

            // then
            assertThat(response.statusCode()).isEqualTo(UNPROCESSABLE_ENTITY.value());
        }

        @ParameterizedTest
        @NullAndEmptySource
        void 하행역이_공백이거나_널이면_예외(final String nullAndEmpty) {
            // when
            final ExtractableResponse<Response> response = 노선_생성_요청("1호선", "잠실역", nullAndEmpty, 0, 100);

            // then
            assertThat(response.statusCode()).isEqualTo(UNPROCESSABLE_ENTITY.value());
        }

        @Test
        void 거리가_널이면_예외() {
            // when
            final ExtractableResponse<Response> response = 노선_생성_요청("1호선", "잠실역", "사당역", null, 100);

            // then
            assertThat(response.statusCode()).isEqualTo(UNPROCESSABLE_ENTITY.value());
        }


        @Test
        void 추가요금이_널이면_예외() {
            // when
            final ExtractableResponse<Response> response = 노선_생성_요청("1호선", "잠실역", "사당역", 10, null);

            // then
            assertThat(response.statusCode()).isEqualTo(UNPROCESSABLE_ENTITY.value());
        }
    }

    @Nested
    class 단일_노선을_조회할때 {

        @Test
        void 성공한다() {
            // given
            역_생성_요청("잠실역");
            역_생성_요청("사당역");
            final Long 생성된_노선_아이디 =
                    노선_생성하고_아이디_반환("1호선", "잠실역", "사당역", 5, 100);

            // when
            final ExtractableResponse<Response> response = 노선_조회_요청(생성된_노선_아이디);

            // then
            assertThat(response.statusCode()).isEqualTo(OK.value());
            단일_노선의_이름을_검증한다(response, "1호선");
            노선에_포함된_N번째_구간을_검증한다(response, 0, "잠실역", "사당역", 5);
        }

        @Test
        void 존재하지_않는_노선의_id로_요청하면_실패한다() {
            // when
            final ExtractableResponse<Response> response = 노선_조회_요청(Long.MAX_VALUE);

            // then
            assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());
        }
    }

    @Test
    void 모든_노선을_조회한다() {
        // given
        역_생성_요청("잠실역");
        역_생성_요청("사당역");
        노선_생성_요청("1호선", "잠실역", "사당역", 5, 100);

        역_생성_요청("건대역");
        역_생성_요청("홍대역");
        노선_생성_요청("2호선", "건대역", "홍대역", 10, 100);

        // when
        final ExtractableResponse<Response> response = 노선_전체_조회_요청();

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
        final List<LineQueryResponse> result = response.as(
                new ParameterizedTypeReference<List<LineQueryResponse>>() {
                }.getType()
        );

        final LineQueryResponse 일호선_응답 = result.get(0);
        단일_노선의_이름을_검증한다(일호선_응답, "1호선");
        노선에_포함된_N번째_구간을_검증한다(일호선_응답, 0, "잠실역", "사당역", 5);

        final LineQueryResponse 이호선_응답 = result.get(1);
        단일_노선의_이름을_검증한다(이호선_응답, "2호선");
        노선에_포함된_N번째_구간을_검증한다(이호선_응답, 0, "건대역", "홍대역", 10);
    }
}
