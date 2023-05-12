package subway.application.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.line.application.FindSingleLineService;
import subway.line.domain.Line;
import subway.line.domain.LineColor;
import subway.line.domain.LineName;
import subway.line.application.port.output.LineRepository;
import subway.section.domain.Section;
import subway.section.domain.Sections;
import subway.station.domain.Station;
import subway.station.domain.StationDistance;
import subway.ui.dto.response.LineResponse;


@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class FindSingleLineServiceTest {

    @Mock
    private LineRepository lineRepository;

    private FindSingleLineService findSingleLineService;

    @BeforeEach
    void setUp() {
        findSingleLineService = new FindSingleLineService(lineRepository);
    }

    @Test
    void 노선_단일_조회_테스트() {
        //given
        final Sections sections = new Sections(List.of(
                new Section(new Station("역삼"), new Station("선릉"), new StationDistance(2)),
                new Section(new Station("강남"), new Station("역삼"), new StationDistance(3))
        ));
        final Line line = new Line(sections, new LineName("2호선"), new LineColor("청록색"));
        given(lineRepository.findById(1L)).willReturn(line);

        //when
        final LineResponse lineResponse = findSingleLineService.findSingleLine(1L);

        //then
        assertThat(lineResponse.getName()).isEqualTo("2호선");
        assertThat(lineResponse.getStations()).extracting("stationName")
                .containsExactly("강남", "역삼", "선릉");
    }
}
