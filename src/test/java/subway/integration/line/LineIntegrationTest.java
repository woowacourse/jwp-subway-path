package subway.integration.line;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.integration.line.LineSteps.노선_단건_삭제_요청;
import static subway.integration.line.LineSteps.노선_단건_조회_요청;
import static subway.integration.line.LineSteps.노선_생성_요청;
import static subway.integration.line.LineSteps.노선_전체_조회_요청;
import static subway.integration.line.LineSteps.노선에_역_등록_요청;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.dto.ErrorResponse;
import subway.dto.LineRequest;
import subway.dto.LineSelectResponse;
import subway.dto.LinesSelectResponse;
import subway.dto.StationSaveRequest;
import subway.dto.StationSelectResponse;
import subway.integration.IntegrationTest;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayName("지하철 노선 관련 기능")
public class LineIntegrationTest extends IntegrationTest {
    private LineRequest lineRequest1;
    private LineRequest lineRequest2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        lineRequest1 = new LineRequest("신분당선", "강남역", "역삼역", 10);
        lineRequest2 = new LineRequest("구신분당선", "강남역", "구역삼역", 10);
    }

    @Nested
    @DisplayName("지하철 노선 생성")
    class create extends IntegrationTest {

        @Test
        void 지하철_노선을_생성한다() {
            // when
            ExtractableResponse<Response> response = 노선_생성_요청(lineRequest1);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            assertThat(response.header("Location")).isNotBlank();
        }

        @Test
        void 기존에_존재하는_이름으로_생성하면_400에러를_반환한다() {
            // given
            노선_생성_요청(lineRequest1);

            // when
            ExtractableResponse<Response> response = 노선_생성_요청(lineRequest1);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            ErrorResponse errorResponse = response.body().as(ErrorResponse.class);
            assertThat(errorResponse.getMessage()).isEqualTo(
                    "이미 존재하는 이름입니다. (입력값 : " + lineRequest1.getLineName() + ")");
        }

    }

    @DisplayName("지하철 노선 조회")
    @Nested
    class find extends IntegrationTest {

        @Test
        void 목록_전체를_조회한다() {
            // given
            lineRequest1 = new LineRequest("1호선", "서울역", "명동역", 7);
            lineRequest2 = new LineRequest("2호선", "강남역", "역삼역", 5);
            final ExtractableResponse<Response> createResponse1 = 노선_생성_요청(lineRequest1);
            final ExtractableResponse<Response> createResponse2 = 노선_생성_요청(lineRequest2);

            // when
            ExtractableResponse<Response> response = 노선_전체_조회_요청();
            final LinesSelectResponse linesSelectResponse = response.as(LinesSelectResponse.class);
            final List<LineSelectResponse> lines = linesSelectResponse.getLines();
            Long firstLineId = Long.parseLong(createResponse1.header("Location").split("/")[2]);
            Long secondLineId = Long.parseLong(createResponse2.header("Location").split("/")[2]);
            final LineSelectResponse firstLine = 노선_단건_조회_요청(firstLineId).as(LineSelectResponse.class);
            final LineSelectResponse secondLine = 노선_단건_조회_요청(secondLineId).as(LineSelectResponse.class);

            // then
            assertThat(lines).hasSize(2);
            assertThat(firstLine.getStations()).map(StationSelectResponse::getName)
                    .containsExactly("서울역", "명동역");
            assertThat(secondLine.getStations()).map(StationSelectResponse::getName)
                    .containsExactly("강남역", "역삼역");
        }

        @Test
        void 지하철_노선을_단건_조회한다() {
            lineRequest1 = new LineRequest("2호선", "교대역", "역삼역", 5);

            // given
            ExtractableResponse<Response> createResponse = 노선_생성_요청(lineRequest1);
            Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
            노선에_역_등록_요청(lineId, new StationSaveRequest("강남역", "역삼역", 2));

            // when
            ExtractableResponse<Response> response = 노선_단건_조회_요청(lineId);
            final LineSelectResponse lineSelectResponse = response.as(LineSelectResponse.class);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(lineSelectResponse.getLineId()).isEqualTo(lineId);
            assertThat(lineSelectResponse.getLineName()).isEqualTo("2호선");
            assertThat(lineSelectResponse.getStations()).map(StationSelectResponse::getName)
                    .containsExactly("교대역", "강남역", "역삼역");
        }
    }

    @DisplayName("지하철 노선 삭제")
    @Nested
    class delete extends IntegrationTest {

        @DisplayName("하나를 제거한다.")
        @Test
        void deleteLine() {
            // given
            ExtractableResponse<Response> createResponse = 노선_생성_요청(lineRequest1);

            // when
            Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
            ExtractableResponse<Response> response = 노선_단건_삭제_요청(lineId);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        }
    }

    @DisplayName("지하철 노선에 역 등록")
    @Nested
    class createStation extends IntegrationTest {

        @Test
        void 지하철_노선에_역을_등록한다() {
            lineRequest1 = new LineRequest("2호선", "교대역", "강남역", 10);
            // given
            ExtractableResponse<Response> createResponse = 노선_생성_요청(lineRequest1);

            // when
            Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
            final StationSaveRequest stationSaveRequest = new StationSaveRequest("교대역", "민트역", 5);
            ExtractableResponse<Response> response = 노선에_역_등록_요청(lineId, stationSaveRequest);
            final StationSelectResponse stationSelectResponse = response.as(StationSelectResponse.class);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(stationSelectResponse.getName()).isEqualTo("민트역");
        }

        @Test
        void 추가한_역이_기존의_노선과_연결되지_않는다면_예외가_발생한다() {
            lineRequest1 = new LineRequest("2호선", "교대역", "강남역", 10);
            // given
            ExtractableResponse<Response> createResponse = 노선_생성_요청(lineRequest1);

            // when
            Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
            final StationSaveRequest stationSaveRequest = new StationSaveRequest("역삼역", "선릉역", 5);
            ExtractableResponse<Response> response = 노선에_역_등록_요청(lineId, stationSaveRequest);
            final String errorMessage = response.jsonPath().getString("message");

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(errorMessage).isEqualTo("연결되지 않은 역이 있습니다");
        }

        @Test
        void 기존_노선이_가지고_있는_구간과_완전히_겹치는_구간을_추가하면_예외가_발생한다() {
            lineRequest1 = new LineRequest("2호선", "교대역", "강남역", 10);
            // given
            ExtractableResponse<Response> createResponse = 노선_생성_요청(lineRequest1);

            // when
            Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
            final StationSaveRequest stationSaveRequest = new StationSaveRequest("교대역", "강남역", 5);
            ExtractableResponse<Response> response = 노선에_역_등록_요청(lineId, stationSaveRequest);
            final String errorMessage = response.jsonPath().getString("message");

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(errorMessage).isEqualTo("이미 존재하는 지하철역입니다.");
        }

        @Test
        void AC_구간_사이로_들어간_BC_구간의_길이가_AC_구간의_길이보다_길다면_예외가_발생한다() {
            lineRequest1 = new LineRequest("2호선", "교대역", "역삼역", 20);
            // given
            ExtractableResponse<Response> createResponse = 노선_생성_요청(lineRequest1);

            // when
            Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
            final StationSaveRequest stationSaveRequest = new StationSaveRequest("강남역", "역삼역", 30);
            ExtractableResponse<Response> response = 노선에_역_등록_요청(lineId, stationSaveRequest);
            final String errorMessage = response.jsonPath().getString("message");

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(errorMessage).isEqualTo("거리는 음수가 될 수 없습니다 (입력값: -10)");
        }
    }

}
