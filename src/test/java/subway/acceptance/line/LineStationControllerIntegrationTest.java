package subway.acceptance.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static subway.acceptance.line.LineStationSteps.노선에_역_제거_요청;
import static subway.acceptance.line.LineStationSteps.노선에_역_추가_요청;
import static subway.acceptance.line.LineSteps.노선_생성_요청;
import static subway.acceptance.line.LineSteps.노선_생성하고_아이디_반환;
import static subway.acceptance.line.LineSteps.노선_조회_요청;
import static subway.acceptance.line.LineSteps.노선에_포함된_N번째_구간을_검증한다;
import static subway.acceptance.station.StationSteps.역_생성_요청;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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
            final UUID 생성된_노선_아이디 = 노선_생성하고_아이디_반환("1호선");
            노선에_역_추가_요청("1호선", "말랑역", "오리역", 10);

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
            노선_생성_요청("1호선");
            노선에_역_추가_요청("1호선", "말랑역", "오리역", 10);

            // when
            final ExtractableResponse<Response> response =
                    노선에_역_추가_요청("1호선", "말랑역", "경유역", 10);

            // then
            assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
        }

        @Test
        void 역이_존재하지_않으면_예외() {
            // given
            역_생성_요청("말랑역");
            역_생성_요청("오리역");
            노선_생성_요청("1호선");
            노선에_역_추가_요청("1호선", "말랑역", "오리역", 10);

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
            노선_생성_요청("1호선");
            노선에_역_추가_요청("1호선", "말랑역", "오리역", 10);

            // when
            final ExtractableResponse<Response> response =
                    노선에_역_추가_요청("1호선", "말랑역", "경유역", 0);

            // then
            assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
        }

        @Test
        void 두_역이_이미_모두_노선에_존재하는_경우_예외() {
            // given
            역_생성_요청("말랑역");
            역_생성_요청("경유역");
            역_생성_요청("오리역");
            노선_생성_요청("1호선");
            노선에_역_추가_요청("1호선", "말랑역", "오리역", 10);

            // when
            final ExtractableResponse<Response> response =
                    노선에_역_추가_요청("1호선", "말랑역", "오리역", 3);

            // then
            assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
        }

        @Test
        void 구간이_이미_다른_노선에_존재하며_해당_노선과_거리가_일치하지_않는_경우_예외() {
            // given
            역_생성_요청("역1");
            역_생성_요청("역2");
            역_생성_요청("역3");
            노선_생성_요청("1호선");
            노선_생성_요청("2호선");
            노선에_역_추가_요청("1호선", "역1", "역2", 1);
            노선에_역_추가_요청("2호선", "역2", "역3", 2);

            // when
            final ExtractableResponse<Response> response =
                    노선에_역_추가_요청("2호선", "역1", "역2", 3);

            // then
            assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
        }

        @Test
        void 구간이_이미_다른_노선에_존재하며_해당_노선의_역과_상하관계가_일치하지_않는_경우_예외() {
            // given
            역_생성_요청("역1");
            역_생성_요청("역2");
            역_생성_요청("역3");
            노선_생성_요청("1호선");
            노선_생성_요청("2호선");
            노선에_역_추가_요청("1호선", "역1", "역2", 1);
            노선에_역_추가_요청("2호선", "역2", "역3", 10);

            // when
            final ExtractableResponse<Response> response =
                    노선에_역_추가_요청("2호선", "역2", "역1", 1);

            // then
            assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
        }

        @Test
        void 구간이_이미_다른_노선에_존재하더라도_정보가_일치하면_생성() {
            // given
            역_생성_요청("역1");
            역_생성_요청("역2");
            역_생성_요청("역3");
            노선_생성_요청("1호선");
            노선에_역_추가_요청("1호선", "역1", "역2", 2);

            final UUID 생성된_노선_아이디 = 노선_생성하고_아이디_반환("2호선");
            노선에_역_추가_요청("2호선", "역2", "역3", 10);

            // when
            final ExtractableResponse<Response> response =
                    노선에_역_추가_요청("2호선", "역1", "역2", 2);

            // then
            assertThat(response.statusCode()).isEqualTo(OK.value());
            final ExtractableResponse<Response> 노선_조회_응답 = 노선_조회_요청(생성된_노선_아이디);
            노선에_포함된_N번째_구간을_검증한다(노선_조회_응답, 0, "역1", "역2", 2);
            노선에_포함된_N번째_구간을_검증한다(노선_조회_응답, 1, "역2", "역3", 10);
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
            final UUID 노선_아이디 = 노선_생성하고_아이디_반환("1호선");
            노선에_역_추가_요청("1호선", "잠실역", "선릉역", 10);
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
            final UUID 노선_아이디 = 노선_생성하고_아이디_반환("1호선");
            노선에_역_추가_요청("1호선", "잠실역", "선릉역", 10);
            노선에_역_추가_요청("1호선", "선릉역", "사당역", 5);
            노선에_역_제거_요청("1호선", "선릉역");

            // when
            노선에_역_제거_요청("1호선", "사당역");

            // then
            final ExtractableResponse<Response> response = 노선_조회_요청(노선_아이디);
            assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());
        }
    }
}
