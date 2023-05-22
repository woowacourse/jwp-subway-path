package subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.line.Line;
import subway.domain.line.LineColor;
import subway.domain.line.LineName;
import subway.domain.section.Distance;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.dto.request.LineRequest;
import subway.dto.request.SectionRequest;
import subway.dto.response.LineResponse;
import subway.fixture.LineFixture.Line1;
import subway.fixture.LineFixture.Line2;
import subway.fixture.StationFixture.STATION_A;
import subway.fixture.StationFixture.STATION_B;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @InjectMocks
    private LineService lineService;

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationRepository stationRepository;


    @Test
    void request_를_받아서_호선을_저장한다() {
        // given
        final LineRequest request = new LineRequest("2호선", "초록");
        when(lineRepository.save(
                Mockito.argThat(
                        line -> line.getLineName().name().equals("2호선") &&
                                line.getLineColor().color().equals("초록")
                )
        )).thenReturn(1L);

        // when, then
        assertThat(lineService.saveLine(request)).isEqualTo(1L);
    }

    @Test
    void id_를_받아_해당_호선을_조회한다() {
        // given
        Long id = 1L;
        final Line response = new Line(id, new LineName("2호선"), new LineColor("초록"));
        doReturn(response).when(lineRepository).findById(id);

        // when
        final LineResponse result = lineService.findLineById(id);

        // then
        AssertionsForClassTypes.assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(
                        new LineResponse(
                                id,
                                response.getLineName().name(),
                                response.getLineColor().color(),
                                Collections.emptyList())
                );
    }

    @Test
    void 전체_호선을_조회한다() {
        // given
        final Line line1 = new Line(1L, Line1.name, Line1.color);
        final Line line2 = new Line(2L, Line2.name, Line2.color);
        doReturn(List.of(line1, line2)).when(lineRepository).findAllLine();

        // when
        final List<LineResponse> allStationResponses = lineService.findAllLine();

        // then
        AssertionsForClassTypes.assertThat(allStationResponses)
                .usingRecursiveComparison()
                .isEqualTo(List.of(
                        new LineResponse(
                                line1.getId(),
                                line1.getLineName().name(),
                                line1.getLineColor().color(),
                                Collections.emptyList()),
                        new LineResponse(
                                line2.getId(),
                                line2.getLineName().name(),
                                line2.getLineColor().color(),
                                Collections.emptyList())
                ));
    }

    @Test
    void id_와_request_를_받아_호선을_업데이트_한다() {
        // given
        final Long id = 1L;
        final LineRequest request = new LineRequest("2호선", "검정");

        // when
        lineService.updateLineById(id, request);

        // then
        ArgumentCaptor<Line> argCaptor = ArgumentCaptor.forClass(Line.class);
        verify(lineRepository, times(1)).update(argCaptor.capture());

        assertAll(
                () -> assertThat(argCaptor.getValue().getLineName().name()).isEqualTo("2호선"),
                () -> assertThat(argCaptor.getValue().getLineColor().color()).isEqualTo("검정")
        );

    }

    @Test
    void id를_받아_호선을_삭제한다() {
        // given
        final Long id = 1L;

        // when
        lineService.deleteLineById(id);

        // then
        verify(lineRepository, times(1)).delete(id);
    }

    @Test
    void id와_section_request_를_받아_해당_호선에_새로운_구간을_추가한다() {
        //given
        doReturn(new Line(1L, Line1.name, Line1.color)).when(lineRepository).findById(1L);
        doReturn(new Station(1L, STATION_A.stationA.getName())).when(stationRepository).findById(1L);
        doReturn(new Station(2L, STATION_B.stationB.getName())).when(stationRepository).findById(2L);

        // when
        lineService.insertSectionToLine(1L, new SectionRequest(1L, 2L, 5));

        // then
        ArgumentCaptor<Line> argCaptor = ArgumentCaptor.forClass(Line.class);

        verify(lineRepository, times(1)).updateSections(argCaptor.capture());
        final Line result = argCaptor.getValue();

        assertThat(result.getSections().sections()).hasSize(1);
    }

    @Test
    void id와_station_request_를_받아_해당_호선에서_역을_제거한다() {
        //given
        final Station stationA = new Station(1L, STATION_A.stationA.getName());
        final Station stationB = new Station(2L, STATION_B.stationB.getName());
        final Section section = new Section(stationA, stationB, new Distance(5));
        final Line line = new Line(1L, Line1.name, Line1.color, new Sections(List.of(section)));

        doReturn(line).when(lineRepository).findById(1L);
        doReturn(stationB).when(stationRepository).findById(2L);

        // when
        lineService.deleteStationFromLine(1L, stationB.getId());

        // then
        ArgumentCaptor<Line> argCaptor = ArgumentCaptor.forClass(Line.class);
        verify(lineRepository, times(1)).updateSections(argCaptor.capture());
    }
}
