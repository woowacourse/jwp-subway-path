package subway.acceptance.line;

import static subway.acceptance.common.CommonSteps.발생한_예외를_검증한다;
import static subway.acceptance.common.CommonSteps.요청_결과의_상태를_검증한다;
import static subway.acceptance.common.CommonSteps.정상_생성;
import static subway.acceptance.common.CommonSteps.정상_요청;
import static subway.acceptance.common.LocationAsserter.location_헤더를_검증한다;
import static subway.acceptance.line.LineStationSteps.노선에_역_추가_요청;
import static subway.acceptance.line.LineSteps.노선_생성_요청;
import static subway.acceptance.line.LineSteps.노선_생성하고_아이디_반환;
import static subway.acceptance.line.LineSteps.노선_전체_조회_결과;
import static subway.acceptance.line.LineSteps.노선_전체_조회_요청;
import static subway.acceptance.line.LineSteps.노선_조회_요청;
import static subway.acceptance.line.LineSteps.노선에_포함된_N번째_구간을_검증한다;
import static subway.acceptance.line.LineSteps.단일_노선의_가격을_검증한다;
import static subway.acceptance.line.LineSteps.단일_노선의_이름을_검증한다;
import static subway.acceptance.station.StationSteps.역_생성_요청;
import static subway.line.exception.line.LineExceptionType.SURCHARGE_IS_NEGATIVE;

import io.restassured.RestAssured;
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
import subway.line.application.dto.LineQueryResponse;
import subway.line.presentation.request.LineCreateRequest;

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

    @Test
    void 단일_노선을_조회한다() {
        // given
        역_생성_요청("잠실역");
        역_생성_요청("사당역");
        final UUID 생성된_노선_아이디 = 노선_생성하고_아이디_반환("1호선", 1000);
        노선에_역_추가_요청("1호선", "잠실역", "사당역", 5);

        // when
        final var 노선_조회_응답 = 노선_조회_요청(생성된_노선_아이디);

        // then
        요청_결과의_상태를_검증한다(노선_조회_응답, 정상_요청);
        단일_노선의_이름을_검증한다(노선_조회_응답, "1호선");
        단일_노선의_가격을_검증한다(노선_조회_응답, 1000);
        노선에_포함된_N번째_구간을_검증한다(노선_조회_응답, 0, "잠실역", "사당역", 5);
    }

    @Test
    void 모든_노선을_조회한다() {
        // given
        역_생성_요청("잠실역");
        역_생성_요청("사당역");
        노선_생성_요청("1호선", 600);
        노선에_역_추가_요청("1호선", "잠실역", "사당역", 5);

        역_생성_요청("건대역");
        역_생성_요청("홍대역");
        노선_생성_요청("2호선", 800);
        노선에_역_추가_요청("2호선", "건대역", "홍대역", 10);

        // when
        final var 노선_전체_조회_결과 = 노선_전체_조회_요청();

        // then
        요청_결과의_상태를_검증한다(노선_전체_조회_결과, 정상_요청);
        final var result = 노선_전체_조회_결과(노선_전체_조회_결과);

        final LineQueryResponse 일호선_응답 = result.get(0);
        단일_노선의_이름을_검증한다(일호선_응답, "1호선");
        단일_노선의_가격을_검증한다(일호선_응답, 600);
        노선에_포함된_N번째_구간을_검증한다(일호선_응답, 0, "잠실역", "사당역", 5);

        final LineQueryResponse 이호선_응답 = result.get(1);
        단일_노선의_이름을_검증한다(이호선_응답, "2호선");
        단일_노선의_가격을_검증한다(이호선_응답, 800);
        노선에_포함된_N번째_구간을_검증한다(이호선_응답, 0, "건대역", "홍대역", 10);
    }

    @Nested
    class 노선을_생성할_떄 {

        @Test
        void 두_종착역이_존재하는_경우_생성된다() {
            // given
            역_생성_요청("잠실역");
            역_생성_요청("사당역");
            final LineCreateRequest request = new LineCreateRequest("1호선", 0);

            // when
            final var 노선_생성_응답 = 노선_생성_요청(request);

            // then
            요청_결과의_상태를_검증한다(노선_생성_응답, 정상_생성);
            location_헤더를_검증한다(노선_생성_응답);
        }

        @Test
        void 추가_요금이_음수인_경우_예외() {
            // given
            역_생성_요청("잠실역");
            역_생성_요청("사당역");
            final LineCreateRequest request = new LineCreateRequest("1호선", -1);

            // when
            final var response = 노선_생성_요청(request);

            // then
            발생한_예외를_검증한다(response, SURCHARGE_IS_NEGATIVE);
        }
    }
}
