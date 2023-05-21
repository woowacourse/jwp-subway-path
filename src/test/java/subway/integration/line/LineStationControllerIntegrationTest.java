package subway.integration.line;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static subway.integration.line.LineStationSteps.노선에_역_제거_요청;
import static subway.integration.line.LineStationSteps.노선에_역_추가_요청;
import static subway.integration.line.LineSteps.노선_생성_요청;
import static subway.integration.line.LineSteps.노선_생성하고_아이디_반환;
import static subway.integration.line.LineSteps.노선_조회_요청;
import static subway.integration.line.LineSteps.노선에_포함된_N번째_구간을_검증한다;
import static subway.integration.station.StationSteps.역_생성_요청;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
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
import org.springframework.test.context.jdbc.Sql;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("LineStationController 통합테스트")
@Sql("/truncate.sql")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LineStationControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Nested
    class 노선에_역을_추가할_떄 {

        @Test
        void 기준역에_대해_요청한_방향으로_추가된다() {
            // given
            역_생성_요청("말랑역");
            역_생성_요청("경유역");
            역_생성_요청("오리역");
            final Long 생성된_노선_아이디 = 노선_생성하고_아이디_반환("1호선", "말랑역", "오리역", 10, 100);

            // when
            final ExtractableResponse<Response> response =
                    노선에_역_추가_요청("1호선", "말랑역", "경유역", 4);

            // then
            assertThat(response.statusCode()).isEqualTo(OK.value());
            final ExtractableResponse<Response> 노선_조회_응답 = 노선_조회_요청(생성된_노선_아이디);
            노선에_포함된_N번째_구간을_검증한다(노선_조회_응답, 0, "말랑역", "경유역", 4);
            노선에_포함된_N번째_구간을_검증한다(노선_조회_응답, 1, "경유역", "오리역", 6);
        }

        @Test
        void 중간_삽입시_추가할려는_역간_거리가_기존_역간_길이와_같거나_크다면_예외() {
            // given
            역_생성_요청("말랑역");
            역_생성_요청("경유역");
            역_생성_요청("오리역");
            노선_생성_요청("1호선", "말랑역", "오리역", 10, 100);

            // when
            final ExtractableResponse<Response> response =
                    노선에_역_추가_요청("1호선", "말랑역", "경유역", 10);

            // then
            assertThat(response.statusCode()).isEqualTo(CONFLICT.value());
        }

        @Test
        void 추가_할려는_두_역이_이미_노선에_존재하면_예외() {
            // given
            역_생성_요청("말랑역");
            역_생성_요청("오리역");
            노선_생성_요청("1호선", "말랑역", "오리역", 10, 100);

            // when
            final ExtractableResponse<Response> response =
                    노선에_역_추가_요청("1호선", "말랑역", "오리역", 20);

            // then
            assertThat(response.statusCode()).isEqualTo(CONFLICT.value());
        }

        @Test
        void 추가할려는_두_역이_모두_노선에_존재하지_않으면_예외() {
            // given
            역_생성_요청("말랑역");
            역_생성_요청("오리역");
            노선_생성_요청("1호선", "말랑역", "오리역", 10, 100);

            // when
            final ExtractableResponse<Response> response =
                    노선에_역_추가_요청("1호선", "말랑역", "경유역", 5);

            // then
            assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());
        }

        @Test
        void 역간의_거리가_0이하이면_예외() {
            // given
            역_생성_요청("말랑역");
            역_생성_요청("경유역");
            역_생성_요청("오리역");
            노선_생성_요청("1호선", "말랑역", "오리역", 10, 100);

            // when
            final ExtractableResponse<Response> response =
                    노선에_역_추가_요청("1호선", "말랑역", "경유역", 0);

            // then
            assertThat(response.statusCode()).isEqualTo(UNPROCESSABLE_ENTITY.value());
        }

        @Test
        void 두_역이_이미_모두_노선에_존재하는_경우_예외() {
            // given
            역_생성_요청("말랑역");
            역_생성_요청("경유역");
            역_생성_요청("오리역");
            노선_생성_요청("1호선", "말랑역", "오리역", 10, 100);
            노선에_역_추가_요청("1호선", "말랑역", "경유역", 3);

            // when
            final ExtractableResponse<Response> response =
                    노선에_역_추가_요청("1호선", "말랑역", "오리역", 3);

            // then
            assertThat(response.statusCode()).isEqualTo(CONFLICT.value());
        }

        @ParameterizedTest
        @NullAndEmptySource
        void 호선이_공백이거나_널이면_예외(final String nullAndEmpty) {
            // when
            final ExtractableResponse<Response> response =
                    노선에_역_추가_요청(nullAndEmpty, "말랑역", "오리역", 3);

            // then
            assertThat(response.statusCode()).isEqualTo(UNPROCESSABLE_ENTITY.value());
        }

        @ParameterizedTest
        @NullAndEmptySource
        void 상행역이_공백이거나_널이면_예외(final String nullAndEmpty) {
            // when
            final ExtractableResponse<Response> response =
                    노선에_역_추가_요청("1호선", nullAndEmpty, "오리역", 3);

            // then
            assertThat(response.statusCode()).isEqualTo(UNPROCESSABLE_ENTITY.value());
        }

        @ParameterizedTest
        @NullAndEmptySource
        void 하행역이_공백이거나_널이면_예외(final String nullAndEmpty) {
            // when
            final ExtractableResponse<Response> response =
                    노선에_역_추가_요청("1호선", "말랑역", nullAndEmpty, 3);

            // then
            assertThat(response.statusCode()).isEqualTo(UNPROCESSABLE_ENTITY.value());
        }

        @Test
        void 거리가_널이면_예외() {
            // when
            final ExtractableResponse<Response> response =
                    노선에_역_추가_요청("1호선", "말랑역", "오리역", null);

            // then
            assertThat(response.statusCode()).isEqualTo(UNPROCESSABLE_ENTITY.value());
        }
    }

    @Nested
    class 노선에서_역을_제거할_때 {

        @Test
        void 노선을_재배치한다() {
            // given
            역_생성_요청("잠실역");
            역_생성_요청("선릉역");
            역_생성_요청("사당역");
            final Long 노선_아이디 = 노선_생성하고_아이디_반환("1호선", "잠실역", "선릉역", 10, 100);
            노선에_역_추가_요청("1호선", "선릉역", "사당역", 5);

            // when
            final ExtractableResponse<Response> response = 노선에_역_제거_요청("1호선", "선릉역");

            // then
            assertThat(response.statusCode()).isEqualTo(OK.value());
            final ExtractableResponse<Response> 노선_조회_응답 = 노선_조회_요청(노선_아이디);
            노선에_포함된_N번째_구간을_검증한다(노선_조회_응답, 0, "잠실역", "사당역", 15);
        }

        @Test
        void 노선에_등록된_역이_2개_인_경우_하나의_역을_제거할_때_두_역이_모두_제거된다() {
            // given
            역_생성_요청("잠실역");
            역_생성_요청("선릉역");
            역_생성_요청("사당역");
            final Long 노선_아이디 = 노선_생성하고_아이디_반환("1호선", "잠실역", "선릉역", 10, 100);
            노선에_역_추가_요청("1호선", "선릉역", "사당역", 5);
            노선에_역_제거_요청("1호선", "선릉역");

            // when
            노선에_역_제거_요청("1호선", "사당역");

            // then
            final ExtractableResponse<Response> response = 노선_조회_요청(노선_아이디);
            assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());
        }

        @Test
        void 노선에_존재하지_않는_역을_제거_요청시_예외() {
            // given
            역_생성_요청("잠실역");
            역_생성_요청("선릉역");
            노선_생성_요청("1호선", "잠실역", "선릉역", 10, 100);

            // when
            final ExtractableResponse<Response> response = 노선에_역_제거_요청("1호선", "사당역");

            // then
            assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());
        }

        @ParameterizedTest
        @NullAndEmptySource
        void 호선이_공백이거나_널이면_예외(final String nullAndEmpty) {
            // when
            final ExtractableResponse<Response> response =
                    노선에_역_제거_요청(nullAndEmpty, "카프카역");

            // then
            assertThat(response.statusCode()).isEqualTo(UNPROCESSABLE_ENTITY.value());
        }

        @ParameterizedTest
        @NullAndEmptySource
        void 삭제할_역이_공백이거나_널이면_예외(final String nullAndEmpty) {
            // when
            final ExtractableResponse<Response> response =
                    노선에_역_제거_요청("1호선", nullAndEmpty);

            // then
            assertThat(response.statusCode()).isEqualTo(UNPROCESSABLE_ENTITY.value());
        }
    }
}
