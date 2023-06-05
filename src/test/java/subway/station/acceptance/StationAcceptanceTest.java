package subway.station.acceptance;

import static subway.common.acceptance.CommonSteps.비정상_요청;
import static subway.common.acceptance.CommonSteps.비정상_요청_요청한_리소스_찾을_수_없음;
import static subway.common.acceptance.CommonSteps.요청_결과가_반환하는_리소스의_위차가_존재하는지_확인한다;
import static subway.common.acceptance.CommonSteps.요청_결과의_상태를_검증한다;
import static subway.common.acceptance.CommonSteps.정상_생성;
import static subway.common.acceptance.CommonSteps.정상_요청;
import static subway.common.acceptance.CommonSteps.정상_요청이지만_반환값_없음;
import static subway.station.acceptance.StationSteps.역_id_조회_요청;
import static subway.station.acceptance.StationSteps.역_목록_조회_요청;
import static subway.station.acceptance.StationSteps.역_목록_크기_비교;
import static subway.station.acceptance.StationSteps.역_생성_결과에서_id_가져오기;
import static subway.station.acceptance.StationSteps.역_생성_요청;
import static subway.station.acceptance.StationSteps.역_이름_비교;
import static subway.station.acceptance.StationSteps.역_정보_변경_요청;
import static subway.station.acceptance.StationSteps.역_제거_요청;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.common.acceptance.IntegrationTest;
import subway.station.ui.dto.response.StationInfoResponse;

@SuppressWarnings("NonAsciiCharacters")
class StationAcceptanceTest extends IntegrationTest {

    @Nested
    class 역을_추가할_때 {

        @Test
        void 정상_요청의_경우_역이_정상적으로_추가된다() {
            var 요청_결과 = 역_생성_요청("name");

            요청_결과가_반환하는_리소스의_위차가_존재하는지_확인한다(요청_결과);
            요청_결과의_상태를_검증한다(요청_결과, 정상_생성);
        }

        @Test
        void 역이_이미_추가된_경우_예외가_발생한다() {
            역_생성_요청("name");
            var 요청_결과 = 역_생성_요청("name");

            요청_결과의_상태를_검증한다(요청_결과, 비정상_요청);
        }
    }

    @Nested
    class 역을_제거할_때 {

        @Test
        void 정상_요청의_경우_역이_정상적으로_제거된다() {
            var 역_생성_결과 = 역_생성_요청("name");
            long 역_id = 역_생성_결과.as(StationInfoResponse.class).getId();

            var 생성된_역_조회_결과 = 역_id_조회_요청(역_id);
            요청_결과의_상태를_검증한다(생성된_역_조회_결과, 정상_요청);

            var 역_제거_결과 = 역_제거_요청(역_id);
            요청_결과의_상태를_검증한다(역_제거_결과, 정상_요청이지만_반환값_없음);

            var 제거된_역_조회_결과 = 역_id_조회_요청(역_id);
            요청_결과의_상태를_검증한다(제거된_역_조회_결과, 비정상_요청_요청한_리소스_찾을_수_없음);
        }

        @Test
        void 없는_역을_제거하려고_시도해도_정상적으로_제거된다() {
            var 역_생성_결과 = 역_생성_요청("name");
            var 역_id = 역_생성_결과에서_id_가져오기(역_생성_결과);

            var 역_제거_결과 = 역_제거_요청(역_id);

            요청_결과의_상태를_검증한다(역_제거_결과, 정상_요청이지만_반환값_없음);
        }
    }

    @Nested
    class 역을_조회할_때 {

        @Test
        void 정상_요청의_경우_역이_정상적으로_조회된다() {
            var 역_생성_결과 = 역_생성_요청("name");
            var 역_id = 역_생성_결과에서_id_가져오기(역_생성_결과);

            var 역_조회_결과 = 역_id_조회_요청(역_id);

            요청_결과의_상태를_검증한다(역_조회_결과, 정상_요청);
        }

        @Test
        void 없는_역을_조회하려고_시도하면_예외가_발생한다() {
            var 역_조회_결과 = 역_id_조회_요청(1L);

            요청_결과의_상태를_검증한다(역_조회_결과, 비정상_요청_요청한_리소스_찾을_수_없음);
        }
    }

    @Nested
    class 역_목록을_조회할_때 {

        @Test
        void 정상_요청의_경우_역_목록이_정상적으로_조회된다() {
            var 생성_전_역_목록_조회_결과 = 역_목록_조회_요청();
            역_목록_크기_비교(생성_전_역_목록_조회_결과, 0);

            역_생성_요청("name");

            var 역_목록_조회_결과 = 역_목록_조회_요청();

            요청_결과의_상태를_검증한다(역_목록_조회_결과, 정상_요청);
            역_목록_크기_비교(역_목록_조회_결과, 1);
        }
    }

    @Nested
    class 역_정보를_변경할_때 {

        @Test
        void 정상_요청의_경우_역_정보가_정상적으로_변경된다() {
            var 역_생성_결과 = 역_생성_요청("name");
            var 역_id = 역_생성_결과에서_id_가져오기(역_생성_결과);

            var 역_조회_결과 = 역_id_조회_요청(역_id);
            요청_결과의_상태를_검증한다(역_조회_결과, 정상_요청);
            역_이름_비교(역_조회_결과, "name");

            var 역_정보_변경_결과 = 역_정보_변경_요청(역_id, "name2");

            요청_결과의_상태를_검증한다(역_정보_변경_결과, 정상_요청이지만_반환값_없음);

            var 변경된_역_조회_결과 = 역_id_조회_요청(역_id);
            요청_결과의_상태를_검증한다(변경된_역_조회_결과, 정상_요청);
            역_이름_비교(변경된_역_조회_결과, "name2");
        }
    }
}
