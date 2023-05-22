package subway.application.service.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.application.port.in.line.dto.response.LineQueryResponse;
import subway.application.port.out.line.LoadLinePort;
import subway.common.exception.NoSuchLineException;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.fixture.SectionFixture.이호선_삼성_잠실_2;
import subway.fixture.SectionFixture.이호선_역삼_삼성_3;
import subway.fixture.StationFixture.건대역;
import subway.fixture.StationFixture.삼성역;
import subway.fixture.StationFixture.역삼역;
import subway.fixture.StationFixture.잠실역;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class LineQueryServiceTest {

    private LoadLinePort loadLinePort;
    private LineQueryService lineQueryService;

    @BeforeEach
    void setUp() {
        loadLinePort = mock(LoadLinePort.class);
        lineQueryService = new LineQueryService(loadLinePort);
    }

    @Nested
    class 아이디로_라인_조회시_ {

        private final long lineId = 1L;

        @Test
        void 아이디에_해당하는_라인이_존재하지_않으면_예외() {
            // given
            given(loadLinePort.findById(lineId))
                    .willReturn(Optional.empty());

            // when then
            assertThatThrownBy(() -> lineQueryService.findLineById(lineId))
                    .isInstanceOf(NoSuchLineException.class);
        }

        @Test
        void 성공() {
            // given
            Line line = new Line(lineId, "2호선", "GREEN", 0,
                    List.of(이호선_역삼_삼성_3.SECTION, 이호선_삼성_잠실_2.SECTION));
            given(loadLinePort.findById(lineId))
                    .willReturn(Optional.of(line));

            // when
            LineQueryResponse response = lineQueryService.findLineById(lineId);

            // then
            assertThat(response)
                    .usingRecursiveComparison()
                    .isEqualTo(new LineQueryResponse(lineId, "2호선", "GREEN",
                            List.of(역삼역.RESPONSE, 삼성역.RESPONSE, 잠실역.RESPONSE)));
        }
    }

    @Test
    void 라인_전체_조회_테스트() {
        // given
        long lineId1 = 1L;
        long lineId2 = 2L;
        Line line1 = new Line(lineId1, "2호선", "GREEN", 0,
                List.of(이호선_역삼_삼성_3.SECTION, 이호선_삼성_잠실_2.SECTION));
        Line line2 = new Line(lineId2, "3호선", "ORANGE", 0,
                List.of(new Section(역삼역.STATION, 건대역.STATION, 1)));

        given(loadLinePort.findAll())
                .willReturn(List.of(line1, line2));

        // when
        List<LineQueryResponse> responses = lineQueryService.findAllLines();

        // then
        assertAll(
                () -> assertThat(responses).hasSize(2),
                () -> assertThat(responses)
                        .usingRecursiveComparison()
                        .ignoringCollectionOrder()
                        .isEqualTo(List.of(
                                new LineQueryResponse(lineId1, "2호선", "GREEN",
                                        List.of(역삼역.RESPONSE, 삼성역.RESPONSE, 잠실역.RESPONSE)),
                                new LineQueryResponse(lineId2, "3호선", "ORANGE",
                                        List.of(역삼역.RESPONSE, 건대역.RESPONSE))
                        ))
        );
    }
}
