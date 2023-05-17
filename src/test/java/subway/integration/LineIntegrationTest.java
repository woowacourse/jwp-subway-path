package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.integration.IntegrationTestFixture.구간_추가_요청;
import static subway.integration.IntegrationTestFixture.노선_삭제_요청;
import static subway.integration.IntegrationTestFixture.노선_생성_요청;
import static subway.integration.IntegrationTestFixture.노선_전체_조회_결과를_확인한다;
import static subway.integration.IntegrationTestFixture.노선_정보;
import static subway.integration.IntegrationTestFixture.노선_조회_결과;
import static subway.integration.IntegrationTestFixture.단일_노선_조회_요청;
import static subway.integration.IntegrationTestFixture.리소스를_찾을_수_없다는_응답인지_검증한다;
import static subway.integration.IntegrationTestFixture.반환값이_없는지_검증한다;
import static subway.integration.IntegrationTestFixture.비정상_요청이라는_응답인지_검증한다;
import static subway.integration.IntegrationTestFixture.역_삭제_요청;
import static subway.integration.IntegrationTestFixture.역_생성_요청;
import static subway.integration.IntegrationTestFixture.전체_노선_조회_요청;
import static subway.integration.IntegrationTestFixture.정상_응답인지_검증한다;
import static subway.integration.IntegrationTestFixture.최단_거리_정보를_확인한다;
import static subway.integration.IntegrationTestFixture.최단_거리_조회_요청;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.dto.LineSaveResponse;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class LineIntegrationTest extends IntegrationTest {

    @Nested
    public class 노선을_추가할_때 {

        @Test
        void 정상적으로_추가된다() {
            ExtractableResponse<Response> response = 노선_생성_요청("2호선", "강남역", "역삼역", 10);

            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            assertThat(response.header("Location")).isNotEmpty();
        }

        @Test
        void 이미_존재하는_노선의_이름을_추가하면_오류가_발생한다() {
            노선_생성_요청("2호선", "강남역", "역삼역", 10);

            ExtractableResponse<Response> response = 노선_생성_요청("2호선", "역삼역", "잠실역", 10);

            비정상_요청이라는_응답인지_검증한다(response);
        }
    }

    @Nested
    public class 단일_노선을_조회할_때 {

        @Test
        void 노선_ID를_받아_상행부터_하행까지의_역을_정렬하여_반환한다() {
            Long 저장된_노선_ID = 노선_생성_요청("2호선", "강남역", "역삼역", 4).as(LineSaveResponse.class).getId();

            ExtractableResponse<Response> response = 단일_노선_조회_요청(저장된_노선_ID);

            노선_조회_결과(response, 노선_정보("2호선", "강남역", "역삼역"));
        }
    }

    @Nested
    public class 전체_노선을_조회할_떄 {

        @Test
        void 노선별로_상행부터_하행까지의_역을_정렬하여_반환한다() {
            노선_생성_요청("1호선", "서울역", "시청역", 10);
            노선_생성_요청("2호선", "강남역", "역삼역", 5);

            ExtractableResponse<Response> response = 전체_노선_조회_요청();

            노선_전체_조회_결과를_확인한다(response, 노선_정보("1호선", "서울역", "시청역"), 노선_정보("2호선", "강남역", "역삼역"));
        }
    }

    @Nested
    public class 노선을_삭제할_때 {

        @Test
        void 정상_제거된다() {
            Long 저장된_노선_ID = 노선_생성_요청("2호선", "강남역", "역삼역", 5).as(LineSaveResponse.class).getId();

            ExtractableResponse<Response> response = 노선_삭제_요청(저장된_노선_ID);

            반환값이_없는지_검증한다(response);
            노선_전체_조회_결과를_확인한다(전체_노선_조회_요청());
        }

        @Test
        void 사용되지_않는_역만_제거한다() {
            노선_생성_요청("1호선", "서울역", "강남역", 10);
            Long 저장된_노선_ID = 노선_생성_요청("2호선", "강남역", "역삼역", 5).as(LineSaveResponse.class).getId();

            ExtractableResponse<Response> response = 노선_삭제_요청(저장된_노선_ID);

            반환값이_없는지_검증한다(response);
            노선_전체_조회_결과를_확인한다(전체_노선_조회_요청(), 노선_정보("1호선", "서울역", "강남역"));
        }
    }

    @Nested
    public class 노선에_구간을_등록할_때 {

        @Test
        void 정상_추가_한다() {
            Long 저장된_노선_ID = 노선_생성_요청("2호선", "강남역", "역삼역", 5).as(LineSaveResponse.class).getId();
            역_생성_요청("잠실역");

            ExtractableResponse<Response> response = 구간_추가_요청(저장된_노선_ID, "강남역", "잠실역", 2);

            정상_응답인지_검증한다(response);
            노선_전체_조회_결과를_확인한다(전체_노선_조회_요청(), 노선_정보("2호선", "강남역", "잠실역", "역삼역"));
        }

        @Test
        void 구간이_연결되지_않으면_예외가_발생한다() {
            Long 저장된_노선_ID = 노선_생성_요청("2호선", "강남역", "역삼역", 5).as(LineSaveResponse.class).getId();
            역_생성_요청("서울역");
            역_생성_요청("시청역");

            ExtractableResponse<Response> response = 구간_추가_요청(저장된_노선_ID, "서울역", "시청역", 3);

            비정상_요청이라는_응답인지_검증한다(response);
        }

        @Test
        void 구간_사이에_추가하는_경우_구간_사이의_길이보다_추가할_구간의_거리가_같거나_긴경우_예외가_발생한다() {
            Long 저장된_노선_ID = 노선_생성_요청("2호선", "강남역", "역삼역", 5).as(LineSaveResponse.class).getId();
            역_생성_요청("잠실역");

            ExtractableResponse<Response> response = 구간_추가_요청(저장된_노선_ID, "강남역", "잠실역", 5);

            비정상_요청이라는_응답인지_검증한다(response);
        }

        @Test
        void 이미_존재하는_구간을_등록하면_예외가_발생한다() {
            Long 저장된_노선_ID = 노선_생성_요청("2호선", "강남역", "역삼역", 5).as(LineSaveResponse.class).getId();

            ExtractableResponse<Response> response = 구간_추가_요청(저장된_노선_ID, "강남역", "역삼역", 3);

            비정상_요청이라는_응답인지_검증한다(response);
        }

        @Test
        void 없는_노선에_구간을_등록하면_예외가_발생한다() {
            노선_생성_요청("2호선", "강남역", "역삼역", 5);

            ExtractableResponse<Response> response = 구간_추가_요청(Long.MAX_VALUE, "강남역", "잠실역", 2);

            리소스를_찾을_수_없다는_응답인지_검증한다(response);
        }
    }

    @Nested
    public class 노선에서_역을_삭제할_떄 {

        @Test
        void 정상적으로_삭제된다() {
            Long 저장된_노선_ID = 노선_생성_요청("2호선", "강남역", "역삼역", 5).as(LineSaveResponse.class).getId();
            역_생성_요청("잠실역");
            구간_추가_요청(저장된_노선_ID, "역삼역", "잠실역", 10);

            역_삭제_요청(저장된_노선_ID, "역삼역");

            노선_전체_조회_결과를_확인한다(전체_노선_조회_요청(), 노선_정보("2호선", "강남역", "잠실역"));
        }

        @Test
        void 노선에_역이_2개만_존재할_때_역은_전체가_삭제된다() {
            Long 저장된_노선_ID = 노선_생성_요청("2호선", "강남역", "역삼역", 5).as(LineSaveResponse.class).getId();

            역_삭제_요청(저장된_노선_ID, "역삼역");

            노선_전체_조회_결과를_확인한다(전체_노선_조회_요청(), 노선_정보("2호선"));
        }

        @Test
        void 존재하지_않는_노선의_역을_삭제하면_예외가_발생한다() {
            Long 저장된_노선_ID = 노선_생성_요청("2호선", "강남역", "역삼역", 5).as(LineSaveResponse.class).getId();

            ExtractableResponse<Response> response = 역_삭제_요청(저장된_노선_ID, "서면역");

            리소스를_찾을_수_없다는_응답인지_검증한다(response);
        }

        @Test
        void 환승역을_삭제하면_한_노선에서만_삭제되고_다른_노선에는_존재한다() {
            노선_생성_요청("1호선", "강남역", "시청역", 10);
            Long 저장된_노선_ID = 노선_생성_요청("2호선", "강남역", "역삼역", 5).as(LineSaveResponse.class).getId();

            ExtractableResponse<Response> response = 역_삭제_요청(저장된_노선_ID, "강남역");

            반환값이_없는지_검증한다(response);
            노선_전체_조회_결과를_확인한다(전체_노선_조회_요청(), 노선_정보("1호선", "강남역", "시청역"), 노선_정보("2호선"));
        }

        @Test
        void 노선에_존재하지_않는_역을_삭제하면_예외가_발생한다() {
            노선_생성_요청("2호선", "강남역", "역삼역", 5);

            ExtractableResponse<Response> response = 역_삭제_요청(Long.MAX_VALUE, "강남역");

            리소스를_찾을_수_없다는_응답인지_검증한다(response);
        }
    }

    @Nested
    public class 최단_거리를_조회할_때 {

        @Test
        void 요금과_지나가는_역을_반환한다() {
            노선_생성_요청("1호선", "강남역", "시청역", 10);
            Long 저장된_노선_ID = 노선_생성_요청("2호선", "강남역", "역삼역", 5).as(LineSaveResponse.class).getId();
            구간_추가_요청(저장된_노선_ID, "역삼역", "시청역", 3);

            ExtractableResponse<Response> response = 최단_거리_조회_요청("강남역", "시청역");

            최단_거리_정보를_확인한다(response, 1250, 8, "강남역", "역삼역", "시청역");
        }

        @Test
        void 출발지와_도착지가_같으면_예외가_발생한다() {
            노선_생성_요청("1호선", "강남역", "시청역", 10);
            노선_생성_요청("2호선", "강남역", "역삼역", 5);

            ExtractableResponse<Response> response = 최단_거리_조회_요청("강남역", "강남역");

            비정상_요청이라는_응답인지_검증한다(response);
        }

        @Test
        void 출발지나_도착지가_존재하지_않는_역이면_예외가_발생한다() {
            노선_생성_요청("1호선", "강남역", "시청역", 10);
            노선_생성_요청("2호선", "강남역", "역삼역", 5);

            ExtractableResponse<Response> response = 최단_거리_조회_요청("서면역", "강남역");

            리소스를_찾을_수_없다는_응답인지_검증한다(response);
        }
    }
}
