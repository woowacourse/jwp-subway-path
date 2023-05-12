package subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.mock;
import static subway.domain.fixture.SectionFixtures.포함된_구간들을_검증한다;
import static subway.domain.fixture.StationFixture.선릉;
import static subway.domain.fixture.StationFixture.역1;
import static subway.domain.fixture.StationFixture.역2;
import static subway.domain.fixture.StationFixture.역3;
import static subway.domain.fixture.StationFixture.잠실;
import static subway.domain.fixture.StationFixture.잠실나루;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.application.dto.AddStationToLineCommand;
import subway.application.dto.DeleteStationFromLineCommand;
import subway.application.dto.LineCreateCommand;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.StationRepository;
import subway.domain.service.RemoveStationFromLineService;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("LineService 은(는)")
class LineServiceTest {

    private final LineRepository lineRepository = mock(LineRepository.class);
    private final StationRepository stationRepository = mock(StationRepository.class);
    private final RemoveStationFromLineService removeStationFromLineService = new RemoveStationFromLineService(
            lineRepository);
    private final LineService lineService =
            new LineService(lineRepository, stationRepository, removeStationFromLineService);

    @Test
    void 노선을_생성한다() {
        // given
        given(lineRepository.findByName("1호선"))
                .willReturn(Optional.empty());
        역을_저장한다(잠실);
        역을_저장한다(선릉);
        given(lineRepository.save(any()))
                .willReturn(1L);

        final LineCreateCommand command = new LineCreateCommand(
                "1호선",
                "잠실",
                "선릉",
                10);

        // when
        final Long id = lineService.create(command);

        // then
        assertThat(id).isEqualTo(1L);
    }

    @Test
    void 노선에_역을_추가한다() {
        // given
        역을_저장한다(잠실);
        역을_저장한다(선릉);
        역을_저장한다(잠실나루);
        final Section section = new Section(잠실, 선릉, 10);
        final Line line = new Line("1호선", new Sections(section));
        given(lineRepository.findByName("1호선"))
                .willReturn(Optional.of(line));
        final AddStationToLineCommand command = new AddStationToLineCommand(
                "1호선",
                "잠실",
                "잠실나루",
                6);

        // when
        lineService.addStation(command);

        // then
        verify(lineRepository, times(1)).update(line);
        assertThat(line.sections()).hasSize(2);
    }

    @Test
    void 노선에서_역을_제거한다() {
        // given
        final Sections sections = new Sections(List.of(
                new Section(역1, 역2, 10),
                new Section(역2, 역3, 10)
        ));
        final DeleteStationFromLineCommand command = new DeleteStationFromLineCommand("1호선", "역2");
        final Line line = new Line("1호선", sections);
        given(lineRepository.findByName("1호선"))
                .willReturn(Optional.of(line));
        역을_저장한다(역2);

        // when
        lineService.removeStation(command);

        // then
        verify(lineRepository, times(1)).update(line);
        assertThat(line.sections()).hasSize(1);
        포함된_구간들을_검증한다(line.sections(), "역1-[20km]-역3");
    }

    @Test
    void 노션에_역이_두개일떄_노선에서_역_제거시_노선도_제거된다() {
        // given
        final Sections sections = new Sections(
                new Section(역1, 역2, 10)
        );
        final DeleteStationFromLineCommand command = new DeleteStationFromLineCommand("1호선", "역2");
        final Line line = new Line("1호선", sections);
        given(lineRepository.findByName("1호선"))
                .willReturn(Optional.of(line));
        역을_저장한다(역2);

        // when
        lineService.removeStation(command);

        // then
        verify(lineRepository, times(0)).update(line);
        verify(lineRepository, times(1)).delete(line);
        assertThat(line.sections()).hasSize(0);
    }

    private void 역을_저장한다(final Station station) {
        given(stationRepository.findByName(station.name()))
                .willReturn(Optional.of(station));
    }
}
